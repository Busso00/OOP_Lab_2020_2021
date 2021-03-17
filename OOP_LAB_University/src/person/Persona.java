package person;
import course.*;


public class Persona {
	private String nameSurname;
	private int[] course=new int[25];
	private int cc=0;
	
	public Persona(String nameSurname) {
		this.nameSurname=nameSurname;
	}
/*	public String getName() {
		return this.name;
	}
 	public String getSurname() {
		return this.surname;
	}*/
	public String getPerson(){
		return (this.nameSurname);
	}
	public void addCourse(int courseCode) {
		this.course[this.cc++]=courseCode;
	}
	public String studyPlan(Corso[] course) {
		String s="";
		for(int i=0;i<this.cc;i++) {
			s=s+this.course[i]+" "+course[this.course[i]-10].getCourse()+"\n";
		}
		return s;
	}
}
