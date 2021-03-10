package liste;

class Nodo {
	private Nodo next;
	private int value;
	
	public Nodo(int value,Nodo next) {
		this.value=value;
		this.next=next;
	}
	
	public Nodo getNext() {
		return next;
	}
	public void setNext(Nodo next) {
		this.next = next;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public String likeStringNode(){
		if(next!=null) {
			return value+", "+next.likeStringNode();
		}else {
			return ""+value;//se faccio un return value senza "" ritorna un errore perchè value è intero, sennò In
		}
	}
}
