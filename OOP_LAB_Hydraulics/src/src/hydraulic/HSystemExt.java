package src.hydraulic;

import java.util.Arrays;

/**
 * Main class that act as a container of the elements for
 * the simulation of an hydraulics system 
 * 
 */
public class HSystemExt extends HSystem{
	
	/**
	 * Prints the layout of the system starting at each Source
	 */
	public String layout(){
		//Element[] temp=new Element[vElement.length];
		StringBuffer res=new StringBuffer(0);
		for(Element e:vElement) {
			if(e instanceof Source) {
				this.dfs(e,res);
			}
		}
		return res.toString();
	}
	
	private StringBuffer dfs(Element e,StringBuffer res) {
		
		if(e instanceof Source) {
			res.append("["+e.getName()+"]Source -> ");
			Element next=e.getOutput();
			if(next!=null) {
				this.dfs(next,res);
			}else {
				res.append("*");
			}
		}
		if((e instanceof Split)||(e instanceof Multisplit)){
			res.append("["+e.getName()+"]Split ");
			Element []next;
			int last;
			if(e instanceof Split) {
				next=((Split) e).getOutputs();
			}else {
				next=((Multisplit) e).getOutputs();
			}
			int l=res.length();
			for(last=l-1;(last>0)&&(res.charAt(last)!='\n');last--);
			l-=last;
			if(next[0]!=null) {
				res.append("+-> ");
				this.dfs(next[0],res);
			}else{
				res.append("+-> *");
			}
			for(int i=1;i<next.length;i++) {
				res.append("\n");
				for(int j=0;j<l;j++) {
					res.append(" ");
				}
				res.append("|");
				res.append("\n");
				for(int j=0;j<l;j++) {
					res.append(" ");
				}
				if(next[i]!=null) {
					res.append("+-> ");
					this.dfs(next[i],res);
				}else{
					res.append("+-> *");
				}
			}
		}
		if(e instanceof Tap) {
			res.append("["+e.getName()+"]Source -> ");
			Element next=e.getOutput();
			if(next!=null) {
				this.dfs(next,res);
			}else {
				res.append("*");
			}
		}
		if(e instanceof Sink) {
			res.append("["+e.getName()+"]Sink");
		}
		return res;
	}
	
	/**
	 * Deletes a previously added element with the given name from the system
	 */
	public boolean deleteElement (String name) {
		for(Element e:vElement) {
			if(e.getName()==name) {
				if((e instanceof Split)||(e instanceof Multisplit)) {
					Element []outputs;
					Element pNext=null;
					if(e instanceof Split) {
						outputs=((Split)e).getOutputs();
					}else {
						outputs=((Multisplit)e).getOutputs();
					}
					int count=0;
					for(Element o:outputs) {
						if(o!=null) {
							count++;
							pNext=o;
						}
						if(count>1) {
							return false;
						}
					}
					for(Element allE:vElement) {
						if((allE instanceof Source)||(allE instanceof Tap)) {
							if(allE.getOutput().getName()==name) {
								allE.connect(pNext);
								e=vElement[vElement.length-1];
								vElement=Arrays.copyOf(vElement,vElement.length-1);
								return true;
							}
						}else if(!(allE instanceof Sink)){
							Element []prevOutputs;
							if(e instanceof Split) {
								prevOutputs=((Split)e).getOutputs();
							}else {
								prevOutputs=((Multisplit)e).getOutputs();
							}
							for(Element o:prevOutputs) {
								if(o.getName()==name) {
									o.connect(pNext);
									e=vElement[vElement.length-1];
									vElement=Arrays.copyOf(vElement,vElement.length-1);
									return true;
								}
							}
						}
						
					}
				}
				for(Element allE:vElement) {
					if((allE instanceof Source)||(allE instanceof Tap)) {
						if(allE.getOutput().getName()==name) {
							allE.connect(e.getOutput());
							e=vElement[vElement.length-1];
							vElement=Arrays.copyOf(vElement,vElement.length-1);
							return true;
						}
					}else if(!(allE instanceof Sink)){
						Element []prevOutputs;
						if(e instanceof Split) {
							prevOutputs=((Split)e).getOutputs();
						}else {
							prevOutputs=((Multisplit)e).getOutputs();
						}
						for(Element o:prevOutputs) {
							if(o.getName()==name) {
								o.connect(e.getOutput());
								e=vElement[vElement.length-1];
								vElement=Arrays.copyOf(vElement,vElement.length-1);
								return true;
							}
						}
					}
					
				}
			}
		}
		return false;
	}

	/**
	 * starts the simulation of the system; if enableMaxFlowCheck is true,
	 * checks also the elements maximum flows against the input flow
	 */
	public void simulate(SimulationObserverExt observer, boolean enableMaxFlowCheck) {
		for(Element e:vElement) {
			if(e instanceof Source) {
				this.calculateFlow(e,e.flow,observer,enableMaxFlowCheck);
			}
		}
	}
	private void calculateFlow(Element e,double flow,SimulationObserver obs, boolean eMFC) {
		double []outFlow;
		Element[] elv=new Element[2];
		if((eMFC)&&(flow>((ElementExt)e).getMaxFlow()))
			((SimulationObserverExt)obs).notifyFlowError(e.getClass().toString(),e.getName(),flow,((ElementExt)e).getMaxFlow());
		if(e instanceof Multisplit) {
			outFlow=new double[((Multisplit) e).nOut()];
			elv=((Multisplit) e).getOutputs();
			for(int i=0;i<outFlow.length;i++)
			{
				outFlow[i]=flow*((Multisplit) e).getFlow(i);
			}
			obs.notifyFlow(e.getClass().toString(),e.getName() , flow, outFlow);
			for(int i=0;i<outFlow.length;i++)
			{
				calculateFlow(elv[i],outFlow[i],obs,eMFC);
			}
			return;
		}
		if(e instanceof Split) {
			outFlow=new double[2];
			outFlow[0]=flow/2;
			outFlow[1]=flow/2;
			obs.notifyFlow(e.getClass().toString(),e.getName() , flow, outFlow);
			elv=((Split) e).getOutputs();
			calculateFlow(elv[0],outFlow[0],obs,eMFC);
			calculateFlow(elv[1],outFlow[1],obs,eMFC);
		}else{
			outFlow=new double[1];
			if(e instanceof Sink) {
				outFlow[0]=SimulationObserver.NO_FLOW;
			}else {
				outFlow[0]=flow;
			}
			if(e instanceof Tap) {
					if(!(((Tap) e).open))
					outFlow[0]=0.0;
				}
			if(e instanceof Source) {
				obs.notifyFlow(e.getClass().toString(),e.getName() , SimulationObserver.NO_FLOW, outFlow);
			}else{
				obs.notifyFlow(e.getClass().toString(),e.getName() , flow, outFlow);
			}
			if(!(e instanceof Sink))
				calculateFlow(e.getOutput(),outFlow[0],obs,eMFC);
		}
	}
	
}
