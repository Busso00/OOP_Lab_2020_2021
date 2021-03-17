package course;
import person.*;

public class Corso {
	private String courseName;
	private Persona professor;
	private int []student=new int[100];
	private int sc=0;
	
	public Corso(String courseName,String professor) {
		this.courseName=courseName;
		this.professor=new Persona(professor);
		
	}
	public String getCourse() {
		return courseName+","+professor.getPerson();
	}
	public void addStudent(int studentID) {
		student[this.sc++]=studentID;
	}
	public String listAttendees(Persona[] student) {
		String s="";
		for(int i=0;i<this.sc;i++) {
			s=s+this.student[i]+" "+student[this.student[i]-10000].getPerson()+"\n";
		}
		return s;
	}
}
