

public class Stackmain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		stack.Stack s;
		s=new stack.Stack();
		int r;
		for(int i=0;i<10;i++){
			s.push(i);
			s.push(0);
		}
		for(int i=0;i<10;i++) {
			if(!s.isEmpty()) {
				r=s.pop();		
				System.out.println(" "+r);
			}else {
				System.out.println("lista vuota");
			}
			
		}
		for(int i=15;i>0;i--) {
			if(!s.isEmpty()) {
				r=s.pop();
				System.out.println(" "+r);
			}else {
				System.out.println("lista vuota");
			}
		}
	}

}
