package it.polito.oop.vaccination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Vaccines {
	private Map<String,Person> reg=new TreeMap<>();
	private Map<Integer,Range> ran=new TreeMap<>();
	public class Person {
		private String cFisc;
		private String nome;
		private String cognome;
		private int annoN;
		public Person(String first, String lastName, String ssn, int year) {
			this.nome=first;
			this.cognome=lastName;
			this.cFisc=ssn;
			this.annoN=year;
		}
		public int getAge() {
			return this.annoN;
		}
		public String getPerson() {
			return this.cFisc+","+this.cognome+","+this.annoN;
		}
		public String getCodF() {
			return this.cFisc;
		}
	}
	
	public class Range{
		private int min;
		private int max;
		public Range(int min,int max) {
			this.min=min;
			this.max=max;
		}
		public String getInt() {
			if(this.max==Integer.MAX_VALUE)
				return "["+this.min+","+"+)";
			return "["+this.min+","+this.max+")";
		}
		public boolean match(int a) {
			if((a>min)&&(a<max))
				return true;
			return false;
		}
	}
    public final static int CURRENT_YEAR = java.time.LocalDate.now().getYear();

    // R1
    /**
     * Add a new person to the vaccination system.
     *
     * Persons are uniquely identified by SSN (italian "codice fiscale")
     *
     * @param first first name
     * @param lastName last name
     * @param ssn italian "codice fiscale"
     * @param year birth year
     * @return {@code false} if ssn is duplicate,
     */
    public boolean addPerson(String first, String lastName, String ssn, int year) {
    	if(reg.containsKey(ssn))
    		return false;
    	reg.put(ssn, new Person(first,lastName,ssn,year));
    	return true;
    }

    /**
     * Count the number of people added to the system
     *
     * @return person count
     */
    public int countPeople() {
        return this.reg.size();
    }

    /**
     * Retrieves information about a person.
     * Information is formatted as ssn, last name, and first name
     * separate by {@code ','} (comma).
     *
     * @param ssn "codice fiscale" of person searched
     * @return info about the person
     */
    public String getPerson(String ssn) {
        return this.reg.get(ssn).getPerson();
    }

    /**
     * Retrieves of a person given their SSN (codice fiscale).
     *
     * @param ssn "codice fiscale" of person searched
     * @return age of person (in years)
     */
    public int getAge(String ssn) {
        return this.reg.get(ssn).getAge();
    }

    /**
     * Define the age intervals by providing the breaks between intervals.
     * The first interval always start at 0 (non included in the breaks)
     * and the last interval goes until infinity (not included in the breaks).
     * All intervals are closed on the lower boundary and open at the upper one.
     * <p>
     * For instance {@code setAgeIntervals(40,50,60)}
     * defines four intervals {@code "[0,40)", "[40,50)", "[50,60)", "[60,+)"}.
     *
     * @param breaks the array of breaks
     */
    public void setAgeIntervals(int... breaks) {
    	int prev=0;
    	for(int i:breaks) {
    		this.ran.put(prev, new Range(prev,i));
    		prev=i;
    	}
    	this.ran.put(prev,new Range(prev, Integer.MAX_VALUE));
    }

    /**
     * Retrieves the labels of the age intervals defined.
     *
     * Interval labels are formatted as {@code "[0,10)"},
     * if the upper limit is infinity {@code '+'} is used
     * instead of the number.
     *
     * @return labels of the age intervals
     */
    public Collection<String> getAgeIntervals() {
    	List <String> l=new ArrayList<>();
        for(Range r:this.ran.values()) {
        	l.add(r.getInt());
        }
        return l;
    }

    /**
     * Retrieves people in the given interval.
     *
     * The age of the person is computed by subtracting
     * the birth year from current year.
     *
     * @param range age interval label
     * @return collection of SSN of person in the age interval
     */
    public Collection<String> getInInterval(String range) {
    	String []temp=range.split(",");
    	String s1=(String) temp[0].subSequence(1, temp[0].length());
    	String s2=(String) temp[1].subSequence(0, temp[1].length()-1);
    	Range r=new Range(Integer.parseInt(s1),Integer.parseInt(s2));
        return this.reg.values().stream().filter((a)->{
        	if(r.match(java.time.LocalDate.now().getYear()-a.annoN))
        		return true;
        	return false;
        }).map((m)->{
        	return m.getCodF();
        }
        ).collect(Collectors.toList());
    }

    // R2
    /**
     * Define a vaccination hub
     *
     * @param name name of the hub
     * @throws VaccineException in case of duplicate name
     */
    public void defineHub(String name) throws VaccineException {
    	
    }

    /**
     * Retrieves hub names
     *
     * @return hub names
     */
    public Collection<String> getHubs() {
        return null;
    }

    /**
     * Define the staffing of a hub in terms of
     * doctors, nurses and other personnel.
     *
     * @param name name of the hub
     * @param doctors number of doctors
     * @param nNurses number of nurses
     * @param o number of other personnel
     * @throws VaccineException in case of undefined hub, or any number of personnel not greater than 0.
     */
    public void setStaff(String name, int doctors, int nNurses, int o) throws VaccineException {
    }

    /**
     * Estimates the hourly vaccination capacity of a hub
     *
     * The capacity is computed as the minimum among
     * 10*number_doctor, 12*number_nurses, 20*number_other
     *
     * @param hub name of the hub
     * @return hourly vaccination capacity
     * @throws VaccineException in case of undefined or hub without staff
     */
    public int estimateHourlyCapacity(String hub) throws VaccineException {
        return -1;
    }

    // R3
    /**
     * Load people information stored in CSV format.
     *
     * The header must start with {@code "SSN,LAST,FIRST"}.
     * All lines must have at least three elements.
     *
     * In case of error in a person line the line is skipped.
     *
     * @param people {@code Reader} for the CSV content
     * @return number of correctly added people
     * @throws IOException in case of IO error
     * @throws VaccineException in case of error in the header
     */
    public long loadPeople(Reader people) throws IOException, VaccineException {
        // Hint:
        BufferedReader br = new BufferedReader(people);
        String riga;
        String[] temp;
        int n=0;
        if(!br.readLine().equals("SSN,LAST,FIRST,YEAR"))
        	throw new VaccineException();
        while( ( riga=br.readLine() ) != null) {
			temp=riga.split(",");
			this.addPerson(temp[0], temp[1], temp[2],Integer.parseInt(temp[3]));
			n++;
		}
        br.close();
        return n;
    }

    // R4
    /**
     * Define the amount of working hours for the days of the week.
     *
     * Exactly 7 elements are expected, where the first one correspond to Monday.
     *
     * @param hours workings hours for the 7 days.
     * @throws VaccineException if there are not exactly 7 elements or if the sum of all hours is less than 0 ore greater than 24*7.
     */
    public void setHours(int... hours) throws VaccineException {
    	
    }

    /**
     * Returns the list of standard time slots for all the days of the week.
     *
     * Time slots start at 9:00 and occur every 15 minutes (4 per hour) and
     * they cover the number of working hours defined through method {@link #setHours}.
     * <p>
     * Times are formatted as {@code "09:00"} with both minuts and hours on two
     * digits filled with leading 0.
     * <p>
     * Returns a list with 7 elements, each with the time slots of the corresponding day of the week.
     *
     * @return the list hours for each day of the week
     */
    public List<List<String>> getHours() {
        return null;
    }

    /**
     * Compute the available vaccination slots for a given hub on a given day of the week
     * <p>
     * The availability is computed as the number of working hours of that day
     * multiplied by the hourly capacity (see {@link #estimateCapacity} of the hub.
     *
     * @return
     */
    public int getDailyAvailable(String hub, int d) {
        return -1;
    }

    /**
     * Compute the available vaccination slots for each hub and for each day of the week
     * <p>
     * The method returns a map that associates the hub names (keys) to the lists
     * of number of available hours for the 7 days.
     * <p>
     * The availability is computed as the number of working hours of that day
     * multiplied by the capacity (see {@link #estimateCapacity} of the hub.
     *
     * @return
     */
    public Map<String, List<Integer>> getAvailable() {
        return null;
    }

    /**
     * Computes the general allocation plan a hub on a given day.
     * Starting with the oldest age intervals 40%
     * of available places are allocated
     * to persons in that interval before moving the the next
     * interval and considering the remaining places.
     * <p>
     * The returned value is the list of SSNs (codice fiscale) of the
     * persons allocated to that day
     * <p>
     * <b>N.B.</b> no particular order of allocation is guaranteed
     *
     * @param hub name of the hub
     * @param d day of week index (0 = Monday)
     * @return the list of daily allocations
     */
    public List<String> allocate(String hub, int d) {
        return null;
    }

    /**
     * Removes all people from allocation lists and
     * clears their allocation status
     */
    public void clearAllocation() {
    }

    /**
     * Computes the general allocation plan for the week.
     * For every day, starting with the oldest age intervals
     * 40% available places are allocated
     * to persons in that interval before moving the the next
     * interval and considering the remaining places.
     * <p>
     * The returned value is a list with 7 elements, one
     * for every day of the week, each element is a map that
     * links the name of each hub to the list of SSNs (codice fiscale)
     * of the persons allocated to that day in that hub
     * <p>
     * <b>N.B.</b> no particular order of allocation is guaranteed
     * but the same invocation (after {@link #clearAllocation}) must return the same
     * allocation.
     *
     * @return the list of daily allocations
     */
    public List<Map<String, List<String>>> weekAllocate() {
        return null;
    }

    // R5
    /**
     * Returns the proportion of allocated people
     * w.r.t. the total number of persons added
     * in the system
     *
     * @return proportion of allocated people
     */
    public double propAllocated() {
        return -1.0;
    }

    /**
     * Returns the proportion of allocated people
     * w.r.t. the total number of persons added
     * in the system, divided by age interval.
     * <p>
     * The map associates the age interval label
     * to the proportion of allocates people in that interval
     *
     * @return proportion of allocated people by age interval
     */
    public Map<String, Double> propAllocatedAge() {
        return null;
    }

    /**
     * Retrieves the distribution of allocated persons
     * among the different age intervals.
     * <p>
     * For each age intervals the map reports the
     * proportion of allocated persons in the corresponding
     * interval w.r.t the total number of allocated persons
     *
     * @return
     */
    public Map<String, Double> distributionAllocated() {
        return null;
    }

    // R6
    /**
     * Defines a listener for the file loading method.
     * The {@ accept()} method of the listener is called
     * passing the line number and the offending line.
     * <p>
     * Lines start at 1 with the header line.
     *
     * @param listener the listener for load errors
     */
    public void setLoadListener(BiConsumer<Integer, String> listener) {
    }
}
