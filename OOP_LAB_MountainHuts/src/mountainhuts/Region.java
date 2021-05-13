package mountainhuts;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.*;
import java.util.function.Predicate;

/**
 * Class {@code Region} represents the main facade
 * class for the mountains hut system.
 * 
 * It allows defining and retrieving information about
 * municipalities and mountain huts.
 *
 */
public class Region {
	private String name;
	private List<Altitude> ranges=new ArrayList<>();
	private Map<String,Municipality> city=new TreeMap<>();
	private Map<String,MountainHut> mHut=new TreeMap<>();

	private class Altitude{
		private int min;
		private int max;
		//public String toString;
		public Altitude(int min,int max) {
			this.min=min;
			this.max=max;
			//this.toString=this.toString();
		}
		public boolean inRange(int alt) {
			if((this.min<alt)&&(this.max>=alt))
				return true;
			return false;
		}
		@Override
		public String toString() {
			return this.min+"-"+this.max;
		}
	}
	 
	/**
	 * Create a region with the given name.
	 * 
	 * @param name
	 *            the name of the region
	 */
	public Region(String name) {
		this.name=name;
	}

	/**
	 * Return the name of the region.
	 * 
	 * @return the name of the region
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Create the ranges given their textual representation in the format
	 * "[minValue]-[maxValue]".
	 * 
	 * @param ranges
	 *            an array of textual ranges
	 */
	public void setAltitudeRanges(String... ranges) {
		Stream.of(ranges).forEach(a->{
			String [] val=a.split("-");
			this.ranges.add(new Altitude(Integer.parseInt(val[0]),Integer.parseInt(val[1])));
		});
	}

	/**
	 * Return the textual representation in the format "[minValue]-[maxValue]" of
	 * the range including the given altitude or return the default range "0-INF".
	 * 
	 * @param altitude
	 *            the geographical altitude
	 * @return a string representing the range
	 */
	public String getAltitudeRange(Integer altitude) {
		Predicate<Altitude> p1 = a -> a.inRange(altitude); 
		boolean b=this.ranges.stream().noneMatch(p1);
		if(b)
			return "0-INF";
		return this.ranges.stream().filter(p1).map(a->a.toString()).reduce("",(s,a)->{
			return s+=a;
		});
	}

	/**
	 * Create a new municipality if it is not already available or find it.
	 * Duplicates must be detected by comparing the municipality names.
	 * 
	 * @param name
	 *            the municipality name
	 * @param province
	 *            the municipality province
	 * @param altitude
	 *            the municipality altitude
	 * @return the municipality
	 */
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {
		if(city.containsKey(name))
			return city.get(name);
		Municipality nuovo=new Municipality(name,province,altitude);
		city.put(name, nuovo);
		return nuovo;
	}

	/**
	 * Return all the municipalities available.
	 * 
	 * @return a collection of municipalities
	 */
	public Collection<Municipality> getMunicipalities() {
		return this.city.values();
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return the mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, String category, Integer bedsNumber,Municipality municipality) {
		if(mHut.containsKey(name))
			return mHut.get(name);
		MountainHut nuovo=new MountainHut(name,category,bedsNumber,municipality);
		mHut.put(name, nuovo);
		return nuovo;
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 * 
	 * @param name
	 *            the mountain hut name
	 * @param altitude
	 *            the mountain hut altitude
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return a mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, Integer altitude, String category, Integer bedsNumber,Municipality municipality) {
		if(mHut.containsKey(name))
			return mHut.get(name);
		MountainHut nuovo=new MountainHut(name,category,bedsNumber,municipality,altitude);
		mHut.put(name, nuovo);
		return nuovo;
	}

	/**
	 * Return all the mountain huts available.
	 * 
	 * @return a collection of mountain huts
	 */
	public Collection<MountainHut> getMountainHuts() {
		return this.mHut.values();
	}

	/**
	 * Factory methods that creates a new region by loadomg its data from a file.
	 * 
	 * The file must be a CSV file and it must contain the following fields:
	 * <ul>
	 * <li>{@code "Province"},
	 * <li>{@code "Municipality"},
	 * <li>{@code "MunicipalityAltitude"},
	 * <li>{@code "Name"},
	 * <li>{@code "Altitude"},
	 * <li>{@code "Category"},
	 * <li>{@code "BedsNumber"}
	 * </ul>
	 * 
	 * The fields are separated by a semicolon (';'). The field {@code "Altitude"}
	 * may be empty.
	 * 
	 * @param name
	 *            the name of the region
	 * @param file
	 *            the path of the file
	 */
	public static Region fromFile(String name, String file) {
		Region r=new Region(name);
		List<String> l=Region.readData(file);
		l.stream().skip(1).forEach(s->{
			String []temp;
			temp=((String) s).split(";");
			Municipality m=r.createOrGetMunicipality(temp[1],temp[0],Integer.parseInt(temp[2]));
			if(temp[4].equals(""))
				r.createOrGetMountainHut(temp[3], temp[5],Integer.parseInt(temp[6]), m);
			else
				r.createOrGetMountainHut(temp[3], Integer.parseInt(temp[4]),temp[5],Integer.parseInt(temp[6]), m);
		});
		return r;
	}

	/**
	 * Internal class that can be used to read the lines of
	 * a text file into a list of strings.
	 * 
	 * When reading a CSV file remember that the first line
	 * contains the headers, while the real data is contained
	 * in the following lines.
	 * 
	 * @param file the file name
	 * @return a list containing the lines of the file
	 */
	@SuppressWarnings("unused")
	private static List<String> readData(String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			return in.lines().collect(toList());
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	class cMPP {
		Map<String, Long> map=new TreeMap<>();
		public void accumulator(Municipality m){
		if(!this.map.containsKey(m.getProvince()))
			this.map.put(m.getProvince(), Long.parseLong("1"));
		else {
			this.map.get(m.getProvince());
			this.map.replace(m.getProvince(),Long.sum(this.map.get(m.getProvince()), 1));
			}
		}
		public Map<String, Long> finisher(){
			return this.map;
		}
		public cMPP combiner(cMPP c){
			c.map.forEach((k,v)->{
				if(this.map.containsKey(k))
					this.map.replace(k,Long.sum(this.map.get(k), v));
				else
					this.map.put(k, v);
				});
			return this;}
		}
	/**
	 * Count the number of municipalities with at least a mountain hut per each
	 * province.
	 * 
	 * @return a map with the province as key and the number of municipalities as
	 *         value
	 */
	public Map<String, Long> countMunicipalitiesPerProvince() {
		Collector<Municipality,cMPP,Map<String, Long>> c = Collector.of(
			cMPP::new,cMPP::accumulator,cMPP::combiner,cMPP::finisher);
		return this.city.values().stream().collect(c);
	}
	class cMHPMPP {
		Map<String, Long> map=new TreeMap<>();//mappa di mountainhuts pe municipality
		public void accumulator(MountainHut mh){
			if(this.map.containsKey(mh.getMunicipality().getName()))
				this.map.put(mh.getMunicipality().getName(), Long.parseLong("1"));
			else {
				this.map.get(mh.getMunicipality().getName());
				this.map.replace(mh.getMunicipality().getName(),Long.sum(this.map.get(mh.getMunicipality().getName()), 1));
			}
		}
		public cMHPMPP combiner(cMHPMPP c){
			c.map.forEach((k,v)->{
				if(this.map.containsKey(k))
					this.map.replace(k,Long.sum(this.map.get(k), v));
				else
					this.map.put(k, v);
				});
			return this;
		}
		public Map<String, Long> finisher(){
			return this.map;
		}
	}
	/**
	 * Count the number of mountain huts per each municipality within each province.
	 * 
	 * @return a map with the province as key and, as value, a map with the
	 *         municipality as key and the number of mountain huts as value
	 */
	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
		/*Collector<MountainHut,cMHPMPP,Map<String, Long>> c = Collector.of(
				cMHPMPP::new,cMHPMPP::accumulator,cMHPMPP::combiner,cMHPMPP::finisher);
		/*
		Map<String, Map<String, Long>> mapres=new TreeMap<>();
		this.city.values().stream().collect(Collectors.groupingBy(Municipality::getProvince,toList())).forEach((k,l)->{
			l.forEach((m)->{
				Map<String,Long> mtemp=new TreeMap<>();
				mtemp.put(m.getName(),this.mHut.values().stream().collect(c).get(m.getName()));
				mapres.put(k,mtemp);
			});
		});*/
		return this.city.values().stream().collect(
				Collectors.groupingBy(Municipality::getProvince,
						Collectors.groupingBy(Municipality::getName,
								Collectors.counting())));
	}
	
	public String altitudeRange(MountainHut a) {
		if(a.getAltitude()==null)
			return this.getAltitudeRange(a.getMunicipality().getAltitude());
		return this.getAltitudeRange(a.getAltitude().get());
	}

	/**
	 * Count the number of mountain huts per altitude range. If the altitude of the
	 * mountain hut is not available, use the altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the number of mountain huts
	 *         as value
	 */
	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		/*Map <String,Long> resmap=new TreeMap<>();
		this.ranges.forEach(r->{
			Long cnt=this.mHut.values().stream().filter(mh->{
					return r.inRange(mh.getAltitude().orElse(mh.getMunicipality().getAltitude()));
			}).count();
			resmap.put(r.toString, cnt);
		});*/
		Map <String,Long> resmap=this.mHut.values().stream().map(a->{
			return altitudeRange(a);
		})
		.collect(Collectors.groupingBy(a -> a,
		Collectors.counting()
		));
		this.ranges.stream().forEach(ran->resmap.putIfAbsent(ran.toString(), (long) 0));
		return resmap;
	}
	
	
	
	/**
	 * Compute the total number of beds available in the mountain huts per each
	 * province.
	 * 
	 * @return a map with the province as key and the total number of beds as value
	 */
	public Map<String, Integer> totalBedsNumberPerProvince() {
		//Map <String,Integer> resmap=new TreeMap<>();
		/*this.city.values().stream().collect(Collectors.groupingBy(Municipality::getProvince,toList())).forEach((k,l)->{
			int count=0;
			for(Municipality m:l) {
				count+=this.mHut.values().stream().filter((mh)->{
					if(mh.getMunicipality().equals(m))
						return true;
					return false;
				}).count();
			}
			resmap.put(k, count);
		});*/
		return this.city.values().stream().collect(Collectors.groupingBy(Municipality::getProvince,
			Collectors.summingInt(a->{
				return this.mHut.values().stream().filter(mh->{
					if(mh.getMunicipality().getName().equals(((Municipality)a).getName()))
						return true;
					return false;})
				.collect(Collectors.summingInt(x->x.getBedsNumber()));
				})));
		/*return this.mHut.values().stream().collect(Collectors.groupingBy(
					mh->mh.getMunicipality().getProvince(),
					Collectors.summingInt(MountainHut::getBedsNumber)
				));*/
	}

	/**
	 * Compute the maximum number of beds available in a single mountain hut per
	 * altitude range. If the altitude of the mountain hut is not available, use the
	 * altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the maximum number of beds
	 *         as value
	 */
	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		/*Comparator<?> c=(Object a,Object b)->{
			return ((MountainHut)a).getBedsNumber()-((MountainHut)b).getBedsNumber();
		};
		Map<String, Optional<Integer>> resmap=new TreeMap<>();
		this.ranges.stream().forEach(ar->{
			resmap.put(ar.toString(),this.mHut.values().stream().max((Comparator<? super MountainHut>) c).map((mh)->{
				return ((MountainHut) mh).getBedsNumber();
			}));
		});*/
		Map<String, Optional<Integer>> resmap=this.mHut.values().stream()
				.collect(Collectors.groupingBy(
				mh->altitudeRange(mh),
				Collectors.mapping(MountainHut::getBedsNumber,
						Collectors.maxBy(Comparator.naturalOrder()))
				));
		this.ranges.stream().forEach(r->resmap.putIfAbsent(r.toString(),Optional.of(0)));
		return resmap;
	}

	/**
	 * Compute the municipality names per number of mountain huts in a municipality.
	 * The lists of municipality names must be in alphabetical order.
	 * 
	 * @return a map with the number of mountain huts in a municipality as key and a
	 *         list of municipality names as value
	 */
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {
		Map<Long, List<String>> resmap=new TreeMap<>();
		this.city.values().stream().forEach(c->{
			List <String>l;
			Long n=this.mHut.values().stream().filter((mh)->{
				if(c.getName().equals(mh.getMunicipality().getName()))
					return true;
				return false;
			}).count();
			if(resmap.containsKey(n)) {
				l=resmap.get(n);
				l.add(c.getName());
				l.sort((a,b)->{
					return a.toString().compareTo(b.toString());
				});
			}else {
				l=new ArrayList<>();
				l.add(c.getName());
			}
			resmap.put(n, l);
			
		});
		return resmap;
	}

}
