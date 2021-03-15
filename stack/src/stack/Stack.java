package stack;

import java.util.Arrays;

public class Stack {
	private int[] st;
	
	public void push(int n) {
		st=Arrays.copyOf(st,st.length+1);
		st[st.length-1]=n;
	}
	public int pop() {
		int r=st[st.length-1];
		st=Arrays.copyOf(st,st.length-1);
		return r;
	}
	
	public boolean isEmpty() {
		if(st.length>0)
			return false;
		return true;
	}
	
	public Stack() {
		st=new int[0];
	}
}
