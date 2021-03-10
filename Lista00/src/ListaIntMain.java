import liste.ListaInt;

public class ListaIntMain {
	public static void main(String[] args) {
		int elemento = 0;
		//R1. lista corrisponde all'oggetto listainteri
		ListaInt l= new ListaInt();
		//R2. aggiungere elementi alla lista
		
		l.add(elemento);
		//R3. ritorna elemento in testa
		int t = l.testa();
		//R4. numero di elementi
		int n = l.card();
		//R5. rappresentazione stringa della lista come stringa
		String s = l.likeString();
		String sPriv=l.likeStringPriv();
		
		System.out.println("testa :"+t);
		System.out.println("n° elementi "+n);
		System.out.println("valori: "+s);
		System.out.println("valori metodi privati: "+sPriv);
	}
}
