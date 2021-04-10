package university;

import java.util.logging.Logger;

/**
 * This class represents a university education system.
 * 
 * It manages students and courses.
 *
 */

public class University {
	static final int COURSE_OFFSET = 10;
	static final int STUDENT_OFFSET = 10000;
	static final int N_COURSE = 50;
	static final int N_STUDENT = 1000;
	static final int MAXC_PERS = 25;
	static final int MAXS_PERC =100;
	private final static Logger logger = Logger.getLogger("University");
	private String name;
	private Persona rector;
	protected Persona[] student= new Persona [N_STUDENT];
	protected Corso[] course=new Corso[N_COURSE];
	protected int sc=0;
	private int cc=0;
	
	/**
	 * Constructor
	 * @param name name of the university
	 */
	public University(String name){
		this.name=name;
	}
	
	/**
	 * Getter for the name of the university
	 * 
	 * @return name of university
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Defines the rector for the university
	 * 
	 * @param first
	 * @param last
	 */
	public void setRector(String first, String last) {
		this.rector=new Persona(first+" "+last);
	}
	
	/**
	 * Retrieves the rector of the university
	 * 
	 * @return name of the rector
	 */
	public String getRector() {
		return this.rector.getPerson();
	}
	
	/**
	 * Enroll a student in the university
	 * 
	 * @param first first name of the student
	 * @param last last name of the student
	 * 
	 * @return unique ID of the newly enrolled student
	 */
	public int enroll(String first, String last){
		student[sc++]=new Persona(first+" "+last);
		logger.info("New student enrolled: "+(sc+STUDENT_OFFSET)+" "+first+" "+last);
		return (sc-1+STUDENT_OFFSET);
	}
	
	/**
	 * Retrieves the information for a given student
	 * 
	 * @param id the ID of the student
	 * 
	 * @return information about the student
	 */
	public String student(int id){
		return (id+" "+student[id-STUDENT_OFFSET].getPerson());
	}
	
	/**
	 * Activates a new course with the given teacher
	 * 
	 * @param title title of the course
	 * @param teacher name of the teacher
	 * 
	 * @return the unique code assigned to the course
	 */
	public int activate(String title, String teacher){
		course[cc++]=new Corso(title,teacher);
		logger.info("New course activated: "+(cc+COURSE_OFFSET)+","+title+" "+teacher);
		return (cc-1+COURSE_OFFSET);
	}
	
	/**
	 * Retrieve the information for a given course.
	 * 
	 * The course information is formatted as a string containing 
	 * code, title, and teacher separated by commas, 
	 * e.g., {@code "10,Object Oriented Programming,James Gosling"}.
	 * 
	 * @param code unique code of the course
	 * 
	 * @return information about the course
	 */
	public String course(int code){
		return code+","+course[code-COURSE_OFFSET].getCourse();
	}
	
	/**
	 * Register a student to attend a course
	 * @param studentID id of the student
	 * @param courseCode id of the course
	 */
	public void register(int studentID, int courseCode){
		if((student[studentID-STUDENT_OFFSET]!=null)&&(course[courseCode-COURSE_OFFSET]!=null)) {
			student[studentID-STUDENT_OFFSET].addCourse(courseCode);
			course[courseCode-COURSE_OFFSET].addStudent(studentID);
			logger.info("Student "+studentID+" signed up for course "+courseCode);
		}else {
			System.out.println("lo studente e/o il corso non esiste/ono");
		}
	}
	
	/**
	 * Retrieve a list of attendees
	 * 
	 * @param courseCode unique id of the course
	 * @return list of attendees separated by "\n"
	 */
	public String listAttendees(int courseCode){
		return course[courseCode-COURSE_OFFSET].listAttendees(student);
	}

	/**
	 * Retrieves the study plan for a student.
	 * 
	 * The study plan is reported as a string having
	 * one course per line (i.e. separated by '\n').
	 * The courses are formatted as describe in method {@link #course}
	 * 
	 * @param studentID id of the student
	 * 
	 * @return the list of courses the student is registered for
	 */
	public String studyPlan(int studentID){
		return student[studentID-STUDENT_OFFSET].studyPlan(course);
	}
}

