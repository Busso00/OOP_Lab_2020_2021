package hydraulic;

import java.util.Arrays;

/**
 * Main class that act as a container of the elements for
 * the simulation of an hydraulics system 
 * 
 */
public class HSystem {
	private Element[] vElement= new Element[0];
	/**
	 * Adds a new element to the system
	 * @param elem
	 */
	public void addElement(Element elem){
			vElement=Arrays.copyOf(vElement,vElement.length+1);
			vElement[vElement.length-1]= elem;
	}
	
	/**
	 * returns the element added so far to the system.
	 * If no element is present in the system an empty array (length==0) is returned.
	 * 
	 * @return an array of the elements added to the hydraulic system
	 */
	public Element[] getElements(){
		return this.vElement;
	}
	
	/**
	 * starts the simulation of the system
	 */
	public void simulate(SimulationObserver observer){
		for(Element e:vElement) {
			if(e instanceof Source) {
				this.calculateFlow(e,e.flow,observer);
			}
		}
	}
	private void calculateFlow(Element e,double flow,SimulationObserver obs) {
		double []outFlow;
		Element[] elv=new Element[2];
		if(e instanceof Split) {
			outFlow=new double[2];
			outFlow[0]=flow/2;
			outFlow[1]=flow/2;
			obs.notifyFlow(e.getClass().toString(),e.getName() , flow, outFlow);
			elv=((Split) e).getOutputs();
			calculateFlow(elv[0],outFlow[0],obs);
			calculateFlow(elv[1],outFlow[1],obs);
		}else{
			outFlow=new double[1];
			if(e instanceof Sink) {
				outFlow[0]=obs.NO_FLOW;
			}else {
				outFlow[0]=flow;
			}
			if(e instanceof Tap) {
					if(!(((Tap) e).open))
					outFlow[0]=obs.NO_FLOW;
				}
			if(e instanceof Source) {
				obs.notifyFlow(e.getClass().toString(),e.getName() , obs.NO_FLOW, outFlow);
			}else{
				obs.notifyFlow(e.getClass().toString(),e.getName() , flow, outFlow);
			}
			if(!(e instanceof Sink))
				calculateFlow(e.getOutput(),outFlow[0],obs);
		}
	}
}
