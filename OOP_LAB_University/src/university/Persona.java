package university;
import java.util.Arrays;

public class Persona {
	private String nameSurname;
	private int[] course=new int[University.MAXC_PERS];
	private int cc=0;
	private Esami[] exams=new Esami[0];
	
	public Persona(String nameSurname) {
		this.nameSurname=nameSurname;
	}
	public String getPerson(){
		return (this.nameSurname);
	}
	public void addCourse(int courseCode) {
		this.course[this.cc++]=courseCode;
	}
	public String studyPlan(Corso[] course) {
		String s="";
		for(int i=0;i<this.cc;i++) {
			s=s+this.course[i]+","+course[this.course[i]-University.COURSE_OFFSET].getCourse()+"\n";
		}
		return s;
	}
	public void addExam(int sc,int grade,int cc) {
		Esami tempE=new Esami(sc,grade,cc);
		exams=Arrays.copyOf(exams,exams.length+1);
		exams[exams.length-1]=tempE;
	}
	public double calcAvg(){
		double sumAvg=0;
		if(exams.length!=0) {
			for(int i=0;i<exams.length;i++) {
				sumAvg+=exams[i].getGrades();
			}
			sumAvg/=exams.length;
			return (double)(Math.round(sumAvg * 10d) / 10d);
		}else {
			return -1;
		}
	}
	private int calculateBonus() {
		return this.exams.length/cc*10;
	}
	public int calculateScore() {
		return (int)(this.calcAvg())+this.calculateBonus();
	}
}
