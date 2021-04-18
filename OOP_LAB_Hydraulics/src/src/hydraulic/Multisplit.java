package src.hydraulic;

/**
 * Represents a multisplit element, an extension of the Split that allows many outputs
 * 
 * During the simulation each downstream element will
 * receive a stream that is determined by the proportions.
 */

public class Multisplit extends Split {
	private Element [] connEl= new Element[0];
	private double []flow=new double[0];
	/**
	 * Constructor
	 * @param name
	 * @param numOutput
	 */
	public Multisplit(String name, int numOutput) {
		super(name);
		this.connEl=new Element[numOutput];
		this.flow=new double[numOutput];
	}
    
	/**
	 * returns the downstream elements
	 * @return array containing the two downstream element
	 */
	@Override
    public Element[] getOutputs(){
    	if(connEl.length==0) {
    		return null;
    	}
        return this.connEl;
    }

    /**
     * connect one of the outputs of this split to a
     * downstream component.
     * 
     * @param elem  the element to be connected downstream
     * @param noutput the output number to be used to connect the element
     */
	@Override
	public void connect(Element elem, int noutput){
		this.connEl[noutput]=elem;
	}
	
	/**
	 * Define the proportion of the output flows w.r.t. the input flow.
	 * 
	 * The sum of the proportions should be 1.0 and 
	 * the number of proportions should be equals to the number of outputs.
	 * Otherwise a check would detect an error.
	 * 
	 * @param proportions the proportions of flow for each output
	 */
	public void setProportions(double... proportions) {
		int i=0;
		for(double d:proportions) {
			this.flow[i]=d;
			i++;
		}
	}
	public int nOut() {
		return this.connEl.length;
	}
	public double getFlow(int i) {
		return flow[i];
	}
}
