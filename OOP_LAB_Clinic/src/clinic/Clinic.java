package clinic;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;


/**
 * Represents a clinic with patients and doctors.
 * 
 */
public class Clinic {
	
	
	public class Patient {
		private String nome;
		private String cognome;
		private String ssn;
		private int docID;
		public Patient(String first, String last, String ssn) {
			this.nome=first;
			this.cognome=last;
			this.ssn=ssn;
		}
		public void setDoctor(int docID) {
			this.docID=docID;
		}
		public int getDoctor() {
			return docID;
		}
		@Override
		public String toString() {
			return cognome+" "+nome+" ("+ssn+")";
		}
		public String strRep() {
			return cognome+" "+nome+" ("+ssn+")";
		}
	}
	
	public class Doctor extends Patient{
		private int ID;
		private String specialization;
		private List<String> assP=new ArrayList<>();
		public Doctor(String first, String last, String ssn, int docID, String specialization){
			super(first,last,ssn);
			this.ID=docID;
			this.specialization=specialization;
		}
		public void setPatient(String ssn) {
			assP.add(ssn);
		}
		public Collection<String> getPatients(){
			return this.assP;
		}
		public int getID() {
			return this.ID;
		}
		public String getName() {
			return ((Patient)this).nome;
		}
		public String getSurname() {
			return ((Patient)this).cognome;
		}
		public int nPat() {
			return assP.size();
		}
		public String getSpec() {
			return specialization;
		}
		@Override
		public String toString() {
			return ((Patient)this).strRep()+" ["+ID+"]: "+specialization;
		}
	}
	private Map <String,Patient> rPatients= new TreeMap<>();
	private Map <Integer,Doctor> rDoctors= new TreeMap<>();
	/**
	 * Add a new clinic patient.
	 * 
	 * @param first first name of the patient
	 * @param last last name of the patient
	 * @param ssn SSN number of the patient
	 */
	public void addPatient(String first, String last, String ssn) {
		rPatients.put(ssn, new Patient(first,last,ssn));
	}


	/**
	 * Retrieves a patient information
	 * 
	 * @param ssn SSN of the patient
	 * @return the object representing the patient
	 * @throws NoSuchPatient in case of no patient with matching SSN
	 */
	public String getPatient(String ssn) throws NoSuchPatient {
		if(!rPatients.containsKey(ssn))
			throw new NoSuchPatient();
		return rPatients.get(ssn).toString();
	}

	/**
	 * Add a new doctor working at the clinic
	 * 
	 * @param first first name of the doctor
	 * @param last last name of the doctor
	 * @param ssn SSN number of the doctor
	 * @param docID unique ID of the doctor
	 * @param specialization doctor's specialization
	 */
	public void addDoctor(String first, String last, String ssn, int docID, String specialization) {
		rDoctors.put(docID, new Doctor(first, last, ssn, docID, specialization));
	}

	/**
	 * Retrieves information about a doctor
	 * 
	 * @param docID ID of the doctor
	 * @return object with information about the doctor
	 * @throws NoSuchDoctor in case no doctor exists with a matching ID
	 */
	public String getDoctor(int docID) throws NoSuchDoctor {
		if(!rDoctors.containsKey(docID))
			throw new NoSuchDoctor();
		return rDoctors.get(docID).toString();
	}
	
	/**
	 * Assign a given doctor to a patient
	 * 
	 * @param ssn SSN of the patient
	 * @param docID ID of the doctor
	 * @throws NoSuchPatient in case of not patient with matching SSN
	 * @throws NoSuchDoctor in case no doctor exists with a matching ID
	 */
	public void assignPatientToDoctor(String ssn, int docID) throws NoSuchPatient, NoSuchDoctor {
		if(!rDoctors.containsKey(docID))
			throw new NoSuchDoctor();
		if(!rPatients.containsKey(ssn))
			throw new NoSuchPatient();
		rDoctors.get(docID).setPatient(ssn);
		rPatients.get(ssn).setDoctor(docID);
	}
	
	/**
	 * Retrieves the id of the doctor assigned to a given patient.
	 * 
	 * @param ssn SSN of the patient
	 * @return id of the doctor
	 * @throws NoSuchPatient in case of not patient with matching SSN
	 * @throws NoSuchDoctor in case no doctor has been assigned to the patient
	 */
	public int getAssignedDoctor(String ssn) throws NoSuchPatient, NoSuchDoctor {
		if(!rPatients.containsKey(ssn))
			throw new NoSuchPatient();
		if(!rDoctors.containsKey(rPatients.get(ssn).getDoctor()))
			throw new NoSuchDoctor();
		return rPatients.get(ssn).getDoctor();
	}
	
	/**
	 * Retrieves the patients assigned to a doctor
	 * 
	 * @param id ID of the doctor
	 * @return collection of patient SSNs
	 * @throws NoSuchDoctor in case the {@code id} does not match any doctor 
	 */
	public Collection<String> getAssignedPatients(int id) throws NoSuchDoctor {
		if(!rDoctors.containsKey(id))
			throw new NoSuchDoctor();
		return rDoctors.get(id).getPatients();
	}


	/**
	 * Loads data about doctors and patients from the given stream.
	 * <p>
	 * The text file is organized by rows, each row contains info about
	 * either a patient or a doctor.</p>
	 * <p>
	 * Rows containing a patient's info begin with letter {@code "P"} followed by first name,
	 * last name, and SSN. Rows containing doctor's info start with letter {@code "M"},
	 * followed by badge ID, first name, last name, SSN, and specialization.<br>
	 * The elements on a line are separated by the {@code ';'} character possibly
	 * surrounded by spaces that should be ignored.</p>
	 * <p>
	 * In case of error in the data present on a given row, the method should be able
	 * to ignore the row and skip to the next one.<br>

	 * 
	 * @param readed linked to the file to be read
	 * @throws IOException in case of IO error
	 */
	public int loadData(Reader reader) throws IOException{
		String line=null;
		String [] fields=new String [0];
		String regexp = "\\s*;\\s*";
		int nval=0;
		try {
			line=Clinic.readLine(reader);
		}catch(IOException e) {
			System.err.println("Impossibile leggere il file!!!");
		}finally {
			System.out.println("Metodo terminato!");
		}
		
		try {
		while(line!=null) {
			try {
				fields=line.split(regexp);
			}catch (PatternSyntaxException e) {
				continue;
			}
			if(fields[0].equals("P")) 
			{
				if(fields.length!=4)
					continue;
				this.addPatient(fields[1], fields[2], fields[3]);
				nval++;
			}
			else if(fields[0].equals("M")){
				if(fields.length!=6)
					continue;
				try {
					this.addDoctor(fields[2], fields[3], fields[4] ,Integer.parseInt(fields[1]), fields[5]);	
					nval++;
				}catch(NumberFormatException e) {
					
				}
			}
			line=Clinic.readLine(reader);
		}
		}catch(IOException e) {
			System.err.println("Impossibile leggere il file!!!");
		}finally {
			System.out.println("Metodo terminato!");
		}
		return nval;	
	}
	
	static String readLine(Reader r) throws IOException {
		String riga = "";
		
		int ch;
		ch=r.read();
		if(ch==-1) return null; // EOF
		
		while( ch != -1 && ch!='\n') {
			riga+=(char)ch;
			ch=r.read();
		}
		return riga;
	}

	/**
	 * Loads data about doctors and patients from the given stream.
	 * <p>
	 * The text file is organized by rows, each row contains info about
	 * either a patient or a doctor.</p>
	 * <p>
	 * Rows containing a patient's info begin with letter {@code "P"} followed by first name,
	 * last name, and SSN. Rows containing doctor's info start with letter {@code "M"},
	 * followed by badge ID, first name, last name, SSN, and speciality.<br>
	 * The elements on a line are separated by the {@code ';'} character possibly
	 * surrounded by spaces that should be ignored.</p>
	 * <p>
	 * In case of error in the data present on a given row, the method calls the
	 * {@link ErrorListener#offending} method passing the line itself,
	 * ignores the row, and skip to the next one.<br>

	 * 
	 * @param reader reader linked to the file to be read
	 * @param listener listener used for wrong line notifications
	 * @throws IOException in case of IO error
	 */
	public int loadData(Reader reader, ErrorListener listener) throws IOException {
		String line=null;
		String [] fields=new String [0];
		String regexp = "\\s*;\\s*";
		int nval=0;
		try {
			line=Clinic.readLine(reader);
		}catch(IOException e) {
			System.err.println("Impossibile leggere il file!!!");
		}finally {
			System.out.println("Metodo terminato!");
		}
		int i=0;
		try {
		while(line!=null) {
			i++;
			try {
				fields=line.split(regexp);
			}catch (PatternSyntaxException e) {
				listener.offending(""+i);
				continue;
			}
			if(fields[0].equals("P")) 
			{
				if(fields.length!=4) {
					listener.offending(""+i);
					continue;
				}
				this.addPatient(fields[1], fields[2], fields[3]);
				nval++;
			}
			else if(fields[0].equals("M")){
				if(fields.length!=6) {
					listener.offending(""+i);
					continue;
				}
				try {
					this.addDoctor(fields[2], fields[3], fields[4] ,Integer.parseInt(fields[1]), fields[5]);	
					nval++;
				}catch(NumberFormatException e) {
					listener.offending(""+i);
				}
			}
			line=Clinic.readLine(reader);
		}
		}catch(IOException e) {
			System.err.println("Impossibile leggere il file!!!");
		}finally {
			System.out.println("Metodo terminato!");
		}
		return nval;	
	}

		
	/**
	 * Retrieves the collection of doctors that have no patient at all.
	 * The doctors are returned sorted in alphabetical order
	 * 
	 * @return the collection of doctors' ids
	 */
	public Collection<Integer> idleDoctors(){
		return rDoctors.values().stream().filter((d)->{
			if(d.getPatients().isEmpty())
				return true;
			return false;
		}).sorted((d0,d1)->{
			if(d0.getSurname().compareTo(d1.getSurname())!=0)
				return d0.getSurname().compareTo(d1.getSurname());
			return d0.getName().compareTo(d1.getName());
		}).map(Clinic.Doctor::getID).collect(Collectors.toList());
	}

	/**
	 * Retrieves the collection of doctors having a number of patients larger than the average.
	 * 
	 * @return  the collection of doctors' ids
	 */
	public Collection<Integer> busyDoctors(){
		return rDoctors.values().stream().filter((d)->{
			if(d.getPatients().size()>rDoctors.values().stream().collect(Collectors.averagingDouble(Doctor::nPat)))
				return true;
			return false;
		}).map(Clinic.Doctor::getID).collect(Collectors.toList());
	}

	/**
	 * Retrieves the information about doctors and relative number of assigned patients.
	 * <p>
	 * The method returns list of strings formatted as "{@code ### : ID SURNAME NAME}" where {@code ###}
	 * represent the number of patients (printed on three characters).
	 * <p>
	 * The list is sorted by decreasing number of patients.
	 * 
	 * @return the collection of strings with information about doctors and patients count
	 */
	public Collection<String> doctorsByNumPatients(){
		return rDoctors.values().stream().sorted((d0,d1)->{
			return (int) Math.signum(d0.nPat()-d1.nPat());
		}).map((d)->{
			return String.format("%3d",d.nPat())+" : "+d.getID()+" "+d.getSurname()+" "+d.getName();
		}).collect(Collectors.toList());
	}
	
	/**
	 * Retrieves the number of patients per (their doctor's)  speciality
	 * <p>
	 * The information is a collections of strings structured as {@code ### - SPECIALITY}
	 * where {@code SPECIALITY} is the name of the speciality and 
	 * {@code ###} is the number of patients cured by doctors with such speciality (printed on three characters).
	 * <p>
	 * The elements are sorted first by decreasing count and then by alphabetic speciality.
	 * 
	 * @return the collection of strings with speciality and patient count information.
	 */
	public Collection<String> countPatientsPerSpecialization(){
		return rDoctors.values().stream().collect(Collectors.groupingBy(
				Doctor::getSpec,
					Collectors.summingInt(Doctor::nPat)
				)).entrySet().stream().map(a->{
					return String.format("%3d", a.getValue())+" - "+ a.getKey();
				}).collect(Collectors.toList());
		
	}
	
}
