package it.polito.oop.vaccination;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Vaccines {
	private Map<String,Person> reg=new TreeMap<>();
	private Map<Integer,Range> ran=new TreeMap<>();
	private Map<String,Center> centri=new TreeMap<>();
	private Set<String> prenot=new HashSet<>();
	private List<Integer> ore=new ArrayList<>();
	private BiConsumer<Integer, String> listener;
	public class Person {
		private String cFisc;
		private String nome;
		private String cognome;
		private int annoN;
		private String cent;
		private int giorno;
		public Person(String first, String lastName, String ssn, int year) {
			this.nome=first;
			this.cognome=lastName;
			this.cFisc=ssn;
			this.annoN=year;
		}
		public int getAge() {
			return java.time.LocalDate.now().getYear()-this.annoN;
		}
		public String getPerson() {//avevo messo anno di nascita anziche nome
			return this.cFisc+","+this.cognome+","+this.nome;
		}
		public String getCodF() {
			return this.cFisc;
		}
		public void setVacc(String cent,int d) {
			this.cent=cent;
			this.giorno=d;
		}
		public String getCent() {
			return this.cent;
		}
		public int getG() {
			return this.giorno;
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
			if((a>=min)&&(a<max))
				return true;
			return false;
		}
		public int getMin() {
			return this.min;
		}
		public int getMax() {
			return this.max;
		}
	}
	
	public class Center{
		private String nome;
		private int nDoc;
		private int nNur;
		private int other;
		public Center(String nome) {
			this.nome=nome;
		}
		public void setnDoc(int n) {
			this.nDoc=n;
		}
		public void setnNur(int n) {
			this.nNur=n;
		}
		public void setOther(int n) {
			this.other=n;
		}
		public int getCap() {
			if(((this.nDoc*10)<=(this.nNur*12))&&((this.nDoc*10)<=(this.other*20)))
				return this.nDoc*10;//mi ero dimenticato di moltiplicare per 10
			if(((this.nNur*12)<=(this.nDoc*10))&&((this.nNur*12)<=(this.other*20)))
				return this.nNur*12;//mi ero dimenticato di moltiplicare per 12
			return this.other*20;
		}
		public String getNome() {
			return this.nome;
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
    	if(temp[1].equals("+)"))
    		temp[1]=Integer.MAX_VALUE+")";
    	String s1=(String) temp[0].subSequence(1, temp[0].length());
    	String s2=(String) temp[1].subSequence(0, temp[1].length()-1);
 
    	Range r=new Range(Integer.parseInt(s1),Integer.parseInt(s2));
        return this.reg.values().stream().filter((a)->{
        	if(r.match(a.getAge()))//modifica, non avevo letto bene i requisiti
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
    	if(this.centri.containsKey(name)) {
    		throw new VaccineException();
    	}
    	this.centri.put(name,new Center(name));
    }

    /**
     * Retrieves hub names
     *
     * @return hub names
     */
    public Collection<String> getHubs() {
        return this.centri.keySet();
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
    	if(doctors<=0)
    		throw new VaccineException();
    	if(nNurses<=0)
    		throw new VaccineException();
    	if(o<=0)
    		throw new VaccineException();
    	if(!this.centri.containsKey(name))
    		throw new VaccineException();
    	Center c=this.centri.get(name);
    	c.setnDoc(doctors);
    	c.setnNur(nNurses);
   		c.setOther(o);
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
        if(!this.centri.containsKey(hub))
        	throw new VaccineException();
        if(this.centri.get(hub).getCap()!=0)
        	return this.centri.get(hub).getCap();
        throw new VaccineException();
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
        String riga="";
        String[] temp;
        int n=0;
        if(this.listener==null) {//aggiunta per R7
        if(!br.readLine().equals("SSN,LAST,FIRST,YEAR"))
        	throw new VaccineException();
        while(( riga=br.readLine())!= null) {
			temp=riga.split(",");//Aggiunta
			if(temp.length<4)
				continue;
			if(this.reg.containsKey(temp[0]))
				continue;//fine aggiunta
			if(this.addPerson(temp[2], temp[1],  temp[0],Integer.parseInt(temp[3]))) //qui avevo invertito temp[0] e temp[2]
				n++;
		}
    	}else {
    		int i=1;
    		riga=br.readLine();
    		if(!(riga.equals("SSN,LAST,FIRST,YEAR"))) {
    			this.listener.accept(i, riga);
    			throw new VaccineException();
    		}
            while((riga=br.readLine())!= null) {
            	i++;
    			temp=riga.split(",");
    			if(temp.length<4) {
    				this.listener.accept(i, riga);
    				continue;
    			}
    			if(this.reg.containsKey(temp[0])) {
    				this.listener.accept(i, riga);
    				continue;
    			}
    			if(this.addPerson(temp[2], temp[1],  temp[0],Integer.parseInt(temp[3]))) //qui avevo invertito temp[0] e temp[2]
    				n++;
    		}
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
    	if(hours.length!=7)
    		throw new VaccineException();
    	for(int i:hours) {
    		if(i>12)
    			throw new VaccineException();
    	}
    	this.ore=new ArrayList<>();
    	for(int i:hours) {
    		this.ore.add(i);
    	}
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
    	List <List<String>> orari=new ArrayList<>();
    	List <String> ora=new ArrayList<>(); 
        for(int h:this.ore) {
        	int oFine=h+9;
        	ora=new ArrayList<>(); 
        	int cont=9;//aggiunta
        	String t=String.format("%02d", cont)+":00";//modifica
        	ora.add(t);
        	//inizio aggiunta
        	for(int i=1;i<(h*4-1);i++) {
        		if(i%4==0)
        			cont++;
        		t=String.format("%02d", cont)+":"+String.format("%02d",i%4*15);
        		ora.add(t);
        	}
        	//fine aggiunta
        	String t1=String.format("%02d", oFine)+":00";
        	ora.add(t1);
        	orari.add(ora);
        }
        return orari;
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
        return this.centri.get(hub).getCap()*this.ore.get(d);
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
        return this.centri.keySet().stream().collect(Collectors.toMap(e->e,cv->{
        	List<Integer> l=new ArrayList<>();
        	for(int i=0;i<7;i++)
        		l.add(this.getDailyAvailable(cv,i));
        	return l;}
        ));
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
    public List<String> allocate(String hub, int d) {//non implementato durante l'esame
    	List <Range> l= new ArrayList<>();
    	l.addAll(this.ran.values());
    	List <String> ret=new ArrayList<>();
    	l.sort((a,b)->{
    		return b.getMin()-a.getMin();
    	});
    	List<String> p;
    	int lim;
		long a=0;
    	int n=this.getDailyAvailable(hub, d);
        for(Range r:l) {
        	lim=(int) (n*4/10);
        	p=new ArrayList<>();
        	p.addAll(this.getInInterval(r.getInt()));
        	p.stream().filter((e)->{
       			return !this.prenot.contains(e);
       		}).limit(lim).forEach(el->{
       			this.reg.get(el).setVacc(hub, d);
       		});
    		ret.addAll(p.stream().filter((e)->{
        		return !this.prenot.contains(e);
       		}).limit(lim).collect(Collectors.toList()));
    		a=p.stream().filter((e)->{
        		return !this.prenot.contains(e);
       		}).limit(lim).count();	
    		this.prenot.addAll(p.stream().filter((e)->{
        		return !this.prenot.contains(e);
       		}).limit(lim).collect(Collectors.toList()));
        	n-=a;
        }
        if(n>0) {
        	for(Range r:l) {
            	p=new ArrayList<>();
            	p.addAll(this.getInInterval(r.getInt()));
        		p.stream().filter((e)->{
           			return !this.prenot.contains(e);
           		}).limit(n).forEach(el->{
           			this.reg.get(el).setVacc(hub, d);
           		});
        		ret.addAll(p.stream().filter((e)->{
            		return !this.prenot.contains(e);
           		}).limit(n).collect(Collectors.toList()));
        		a=p.stream().filter((e)->{
            		return !this.prenot.contains(e);
           		}).limit(n).count();	
        		this.prenot.addAll(p.stream().filter((e)->{
            		return !this.prenot.contains(e);
           		}).limit(n).collect(Collectors.toList()));
            	n-=a;
            	if(n<=0)
            		break;
            }
        }
    	return ret;
    }

    /**
     * Removes all people from allocation lists and
     * clears their allocation status
     */
    public void clearAllocation() {//non implementato durante l'esame
    	this.prenot=new HashSet<>();
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
    public List<Map<String, List<String>>> weekAllocate() {//non implementato durante l'esame
    	List<Map<String, List<String>>> weeko= new ArrayList<>();
    	List<List<Person>> listP= new ArrayList<>();
    	
    	for(int i=0;i<7;i++) {
    		for(Center cent:this.centri.values())
    			this.allocate(cent.getNome(),i);
    	}

    	for(int i=0;i<7;i++) {
    		listP.add(new ArrayList<>());
    		
    		for(Person temp:this.reg.values())
    			if(temp.getG()==i)
    				listP.get(i).add(temp);
    	    weeko.add(listP.get(i).stream().filter(p->{
    			return this.prenot.contains(p.getCodF());
    		}).collect(Collectors.groupingBy(e->e.getCent(),
    		TreeMap::new,
    		Collectors.mapping(en->en.getCodF(), Collectors.toList()))));
    	    System.out.println(listP.get(i).size());
    	}
        return weeko;
    }

    // R5
    /**
     * Returns the proportion of allocated people
     * w.r.t. the total number of persons added
     * in the system
     *
     * @return proportion of allocated people
     */
    public double propAllocated() {//non implementato durante l'esame
        return (double)this.prenot.size()/this.reg.size();
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
    public Map<String, Double> propAllocatedAge() {//non implementato durante l'esame
    	Map<String, Double> propAll=new TreeMap<>();
    	int i;
    	double rapp;
    	List<Range> all= this.ran.values().stream().collect(Collectors.toList());
    	for(Range e:all) {
    		i=0;
    		for(String u:this.prenot.stream().collect(Collectors.toList())) {
    			if(this.ran.get(e.getMin()).match(this.reg.get(u).getAge()))
    				i++;
    		}
    		//rapp=(double)i/this.getInInterval(e.getInt()).stream().count(); //dalla specifica sembrava richiesto questo, ma cos? i test falliscono
    		rapp=(double)i/this.countPeople();
    	    propAll.put(e.getInt(),rapp);
    	}
    	return propAll;
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
    public Map<String, Double> distributionAllocated() {//non implementato durante l'esame
    	Map<String, Double> propAll=new TreeMap<>();
    	int i;
    	double rapp;
    	List<Range> all= this.ran.values().stream().collect(Collectors.toList());
    	for(Range e:all) {
    		i=0;
    		for(String u:this.prenot.stream().collect(Collectors.toList())) {
    			if(this.ran.get(e.getMin()).match(this.reg.get(u).getAge()))
    				i++;
    		}
    		rapp=(double)i/this.prenot.size();
    	    propAll.put(e.getInt(),rapp);
    	}
    	return propAll;
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
    public void setLoadListener(BiConsumer<Integer, String> listener) {//non implementato durante l'esame
    	this.listener=listener;
    }
}
