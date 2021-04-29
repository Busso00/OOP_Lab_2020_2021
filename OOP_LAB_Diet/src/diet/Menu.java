package diet;

import java.util.*;

/**
 * Represents a complete menu.
 * 
 * It can be made up of both packaged products and servings of given recipes.
 *
 */
public class Menu extends Food implements NutritionalElement {
	private String name;
	private double calories;
	private double proteins;
	private double carbs;
    private double fat;
    //private double qta;
    private List <String>piatti=new LinkedList<>();
    protected Map<String,NutritionalElement> recsMenu=new TreeMap<>();
	protected Map<String,NutritionalElement> prodMenu=new TreeMap<>();
    //private double []qties=new double[0];
    public Menu(String name, Map<String,NutritionalElement> prodMenu, Map<String,NutritionalElement> recsMenu) {
    	this.name=name;
    	this.calories=0.0;
    	this.proteins=0.0;
    	this.carbs=0.0;
    	this.fat=0.0;
    	this.prodMenu=prodMenu;
    	this.recsMenu=recsMenu;
    	//this.qta=0.0;
    }
	/**
	 * Adds a given serving size of a recipe.
	 * 
	 * The recipe is a name of a recipe defined in the
	 * {@Link Food} in which this menu has been defined.
	 * 
	 * @param recipe the name of the recipe to be used as ingredient
	 * @param quantity the amount in grams of the recipe to be used
	 * @return the same Menu to allow method chaining
	 */
	
	public Menu addRecipe(String recipe, double quantity) {
		NutritionalElement R=recsMenu.get(recipe);
		//this.qta+=quantity;
		this.calories+=R.getCalories()*quantity/100;
		this.proteins+=R.getProteins()*quantity/100;
		this.carbs+=R.getCarbs()*quantity/100;
		this.fat+=R.getFat()*quantity/100;
		this.piatti.add(R.getName());
		//this.qties=Arrays.copyOf(this.qties, this.qties.length+1);
		//this.qties[this.qties.length-1]=quantity;
		return this;
	}

	/**
	 * Adds a unit of a packaged product.
	 * The product is a name of a product defined in the
	 * {@Link Food} in which this menu has been defined.
	 * 
	 * @param product the name of the product to be used as ingredient
	 * @return the same Menu to allow method chaining
	 */
	public Menu addProduct(String product) {
		NutritionalElement R=prodMenu.get(product);
		//this.qta+=R.
		this.calories+=R.getCalories();
		this.proteins+=R.getProteins();
		this.carbs+=R.getCarbs();
		this.fat+=R.getFat();
		this.piatti.add(R.getName());
		return this;
	}

	/**
	 * Name of the menu
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Total KCal in the menu
	 */
	@Override
	public double getCalories() {
		return this.calories;
	}

	/**
	 * Total proteins in the menu
	 */
	@Override
	public double getProteins() {
		return this.proteins;
	}

	/**
	 * Total carbs in the menu
	 */
	@Override
	public double getCarbs() {
		return this.carbs;
	}

	/**
	 * Total fats in the menu
	 */
	@Override
	public double getFat() {
		return this.fat;
	}
	/**
	 * Indicates whether the nutritional values returned by the other methods
	 * refer to a conventional 100g quantity of nutritional element,
	 * or to a unit of element.
	 * 
	 * For the {@link Menu} class it must always return {@code false}:
	 * nutritional values are provided for the whole menu.
	 * 
	 * @return boolean 	indicator
	 */
	@Override
	public boolean per100g() {
		// nutritional values are provided for the whole menu.
		return false;
	}
	
}
