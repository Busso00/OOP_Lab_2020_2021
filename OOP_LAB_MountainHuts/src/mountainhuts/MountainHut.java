package mountainhuts;

import java.util.Optional;

/**
 * Represents a mountain hut.
 * 
 * It is linked to a {@link Municipality}
 *
 */
public class MountainHut {
	private String name;
	private Optional<Integer> alt;
	private String cat;
	private Integer bed;
	private Municipality city;
	public MountainHut(String name,String cat,Integer bed,Municipality city,Integer alt) {
		this.name=name;
		this.cat=cat;
		this.bed=bed;
		this.city=city;
		this.alt=Optional.of(alt);
	}
	public MountainHut(String name,String cat,Integer bed,Municipality city) {
		this.name=name;
		this.cat=cat;
		this.bed=bed;
		this.city=city;
	}
	/**
	 * Unique name of the mountain hut
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Altitude of the mountain hut.
	 * May be absent, in this case an empty {@link java.util.Optional} is returned.
	 * 
	 * @return optional containing the altitude
	 */
	public Optional<Integer> getAltitude() {
		return this.alt;
	}

	/**
	 * Category of the hut
	 * 
	 * @return the category
	 */
	public String getCategory() {
		return this.cat;
	}

	/**
	 * Number of beds places available in the mountain hut
	 * @return number of beds
	 */
	public Integer getBedsNumber() {
		return this.bed;
	}

	/**
	 * Municipality where the hut is located
	 *  
	 * @return municipality
	 */
	public Municipality getMunicipality() {
		return this.city;
	}

}
