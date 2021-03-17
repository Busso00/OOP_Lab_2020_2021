package university;

public class Persona {
	private String nameSurname;
	private int[] course=new int[University.MAXC_PERS];
	private int cc=0;
	
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
			s=s+this.course[i]+" "+course[this.course[i]-University.COURSE_OFFSET].getCourse()+"\n";
		}
		return s;
	}
}
