package university;
import java.util.logging.Logger;

/**
 * This class is an extended version of the {@Link University} class.
 * 
 *
 */
public class UniversityExt extends University {
	
	private final static Logger logger = Logger.getLogger("University");

	public UniversityExt(String name) {
		super(name);
		// Example of logging
		logger.info("Creating extended university object");
	}

	/**
	 * records the grade (integer 0-30) for an exam can 
	 * 
	 * @param studentId the ID of the student
	 * @param courseID	course code 
	 * @param grade		grade ( 0-30)
	 */
	public void exam(int studentId, int courseID, int grade) {
		student[studentId-University.STUDENT_OFFSET].addExam(studentId, grade,courseID);
		course[courseID-University.COURSE_OFFSET].addExam(studentId, grade,courseID);
		logger.info("Student "+studentId+" took an exam in course "+courseID+" with grade "+grade);
	}

	/**
	 * Computes the average grade for a student and formats it as a string
	 * using the following format 
	 * 
	 * {@code "Student STUDENT_ID : AVG_GRADE"}. 
	 * 
	 * If the student has no exam recorded the method
	 * returns {@code "Student STUDENT_ID hasn't taken any exams"}.
	 * 
	 * @param studentId the ID of the student
	 * @return the average grade formatted as a string.
	 */
	public String studentAvg(int studentId) {
		Persona s=student[studentId-University.STUDENT_OFFSET];
		if(s.calcAvg()!=-1) {
			return "Student "+studentId+" : "+s.calcAvg();
		}
		return "Student "+studentId+" hasn't taken any exams";
	}
	
	/**
	 * Computes the average grades of all students that took the exam for a given course.
	 * 
	 * The format is the following: 
	 * {@code "The average for the course COURSE_TITLE is: COURSE_AVG"}.
	 * 
	 * If no student took the exam for that course it returns {@code "No student has taken the exam in COURSE_TITLE"}.
	 * 
	 * @param courseId	course code 
	 * @return the course average formatted as a string
	 */
	public String courseAvg(int courseId) {
		Corso c=course[courseId-University.COURSE_OFFSET];
		if(c.calcAvg()!=-1) {
			return "The average for the course "+c.getCourseName()+" is: "+c.calcAvg();
		}
		return "No student has taken the exam in "+c.getCourseName();
	}
	
	/**
	 * Retrieve information for the best students to award a price.
	 * 
	 * The students' score is evaluated as the average grade of the exams they've taken. 
	 * To take into account the number of exams taken and not only the grades, 
	 * a special bonus is assigned on top of the average grade: 
	 * the number of taken exams divided by the number of courses the student is enrolled to, multiplied by 10.
	 * The bonus is added to the exam average to compute the student score.
	 * 
	 * The method returns a string with the information about the three students with the highest score. 
	 * The students appear one per row (rows are terminated by a new-line character {@code '\n'}) 
	 * and each one of them is formatted as: {@code "STUDENT_FIRSTNAME STUDENT_LASTNAME : SCORE"}.
	 * 
	 * @return info of the best three students.
	 */
	
	public String topThreeStudents(){
		int j=0;
		int P=0;
		int temp=0;
		int[] points= {0,0,0};
		int[] studMatr= {-1,-1,-1};//noOFFSET
		String res="";
		for(int i=0;i<sc;i++){
			P=student[i].calculateScore();
			for(j=2;(j>=0)&&(P>points[j]);j--) {
				if(j==2) {
					points[j]=P;
					studMatr[j]=i;
				}else {
					temp=points[j+1];
					points[j+1]=points[j];
					points[j]=temp;
					
					temp=studMatr[j+1];
					studMatr[j+1]=studMatr[j];
					studMatr[j]=temp;
				}
			}
			
		}
		for(j=0;j<3;j++) {
			if(studMatr[j]>=0) {
				res+=student[studMatr[j]].getPerson()+" : "+points[j]+"\n";
			}
		}
		return res;
	}
}
