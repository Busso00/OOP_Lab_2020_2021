package diet;
import java.util.*;

/**
 * Represents a recipe of the diet.
 * 
 * A recipe consists of a a set of ingredients that are given amounts of raw materials.
 * The overall nutritional values of a recipe can be computed
 * on the basis of the ingredients' values and are expressed per 100g
 * 
 *
 */
public class Recipe extends Food implements NutritionalElement{
	private String name;
	private double calories;
	private double proteins;
	private double carbs;
    private double fat;
    private double qta;
    private List <String>ingr=new LinkedList<>();
    private Map<String,NutritionalElement> matforRecipe=new TreeMap<>();
    private double []qties=new double[0];
    public Recipe(String name,Map<String,NutritionalElement> matforRecipe) {
    	this.name=name;
    	this.calories=0.0;
    	this.proteins=0.0;
    	this.carbs=0.0;
    	this.fat=0.0;
    	this.qta=0.0;
    	this.matforRecipe=matforRecipe;
    }
	/**
	 * Adds a given quantity of an ingredient to the recipe.
	 * The ingredient is a raw material.
	 * 
	 * @param material the name of the raw material to be used as ingredient
	 * @param quantity the amount in grams of the raw material to be used
	 * @return the same Recipe object, it allows method chaining.
	 */
	public Recipe addIngredient(String material, double quantity) {
		NutritionalElement M=matforRecipe.get(material);
		this.calories+=M.getCalories()*quantity/100;
		this.proteins+=M.getProteins()*quantity/100;
		this.carbs+=M.getCarbs()*quantity/100;
		this.fat+=M.getFat()*quantity/100;
		this.ingr.add(material);
		this.qta+=quantity;
		this.qties=Arrays.copyOf(this.qties, this.qties.length+1);
		this.qties[this.qties.length-1]=quantity;
		return this;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public double getCalories() {
		return this.calories*100/this.qta;
	}

	@Override
	public double getProteins() {
		return this.proteins*100/this.qta;
	}

	@Override
	public double getCarbs() {
		return this.carbs*100/this.qta;
	}

	@Override
	public double getFat() {
		return this.fat*100/this.qta;
	}

	/**
	 * Indicates whether the nutritional values returned by the other methods
	 * refer to a conventional 100g quantity of nutritional element,
	 * or to a unit of element.
	 * 
	 * For the {@link Recipe} class it must always return {@code true}:
	 * a recipe expresses nutritional values per 100g
	 * 
	 * @return boolean indicator
	 */
	@Override
	public boolean per100g() {
		return true;
	}
	
	
	/**
	 * Returns the ingredients composing the recipe.
	 * 
	 * A string that contains all the ingredients, one per per line, 
	 * using the following format:
	 * {@code "Material : ###.#"} where <i>Material</i> is the name of the 
	 * raw material and <i>###.#</i> is the relative quantity. 
	 * 
	 * Lines are all terminated with character {@code '\n'} and the ingredients 
	 * must appear in the same order they have been added to the recipe.
	 */
	@Override
	public String toString() {
		String s="";
		int i=0;
		for(String t:ingr) {
			s+=t+" : "+qties[i++]+"\n";
		}
		return s;
	}
}
