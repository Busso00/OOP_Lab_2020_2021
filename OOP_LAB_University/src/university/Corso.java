package university;

import java.util.Arrays;

public class Corso {
	private String courseName;
	private Persona professor;
	private int []student=new int[University.MAXS_PERC];
	private int sc=0;
	private Esami[] exams;
	
	public Corso(String courseName,String professor) {
		this.courseName=courseName;
		this.professor=new Persona(professor);
		
	}
	public String getCourse() {
		return courseName+","+professor.getPerson();
	}
	public String getCourseName() {
		return courseName;
	}
	public void addStudent(int studentID) {
		student[this.sc++]=studentID;
	}
	public String listAttendees(Persona[] student) {
		String s="";
		for(int i=0;i<this.sc;i++) {
			s=s+this.student[i]+" "+student[this.student[i]-University.STUDENT_OFFSET].getPerson()+"\n";
		}
		return s;
	}
	
	public void addExam(int sc,int grade, int cc) {
		Esami tempE=new Esami(sc,grade,cc);
		if(exams==null) {
			exams=new Esami[1];
			exams[0]=tempE;
		}else {
			exams=Arrays.copyOf(exams,exams.length+1);
			exams[exams.length-1]=tempE;
		}
	}
	public double calcAvg(){
		double sumAvg=0;
		if(!(exams==null)) {
			for(int i=0;i<exams.length;i++) {
				sumAvg+=exams[i].getGrades();
			}
			sumAvg=sumAvg/exams.length;
			return (double)(Math.round(sumAvg * 10d) / 10d);
		}else {
			return -1;
		}
	}
}
