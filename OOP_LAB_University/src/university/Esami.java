package university;

public class Esami {
	private int sc;
	private int grades;
	private int cc;
	
	public Esami(int sc,int grades,int cc) {
		this.grades=grades;
		this.cc=cc;
		this.sc=sc;
	}
	public int getGrades() {
		return this.grades;
	}
}
