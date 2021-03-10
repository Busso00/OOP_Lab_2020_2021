package liste;

public class ListaInt {
	private Nodo head=null;
	private int card=0;
	
	public void add(int val) {
		/*
		nodo nuovo=new nodo();
		nuovo.setNext(testa);
		nuovo.setValue(val);
		testa=nuovo;
		*/
		head = new Nodo(val,head);
		card++;
		
	}
	
	public int testa() {
		return head.getValue();
	}
	
	public int card() {
		return card;
	}
	
	public String likeString() {
		String stringRes="[";
		for(Nodo n = head; n != null; n=n.getNext()) {
			stringRes += (n==head?"":", ");
			stringRes += n.getValue();
		}
		stringRes += "]";
		return stringRes;
	}
	
	public String likeStringPriv() {
		String stringRes="[";
		stringRes += head.likeStringNode();
		stringRes += "]";
		return stringRes;
	}
}
