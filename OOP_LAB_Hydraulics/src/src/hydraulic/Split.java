package src.hydraulic;

/**
 * Represents a split element, a.k.a. T element
 * 
 * During the simulation each downstream element will
 * receive a stream that is half the input stream of the split.
 */

public class Split extends ElementExt {
	private Element connEl2;
	/**
	 * Constructor
	 * @param name
	 */
	public Split(String name) {
		super(name);
	}
    
	/**
	 * returns the downstream elements
	 * @return array containing the two downstream element
	 */
    public Element[] getOutputs(){
    	Element[] v=new Element[2];
    	v[0]=this.getOutput();
    	v[1]=this.connEl2;
        return v;
    }

    /**
     * connect one of the outputs of this split to a
     * downstream component.
     * 
     * @param elem  the element to be connected downstream
     * @param noutput the output number to be used to connect the element
     */
	public void connect(Element elem, int noutput){
		if(noutput==0) {
			this.connect(elem);
		}else {
			this.connEl2=elem;
		}
	}
}
