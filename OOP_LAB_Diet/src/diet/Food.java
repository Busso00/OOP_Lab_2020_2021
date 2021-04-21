package diet;

import java.util.*;


/**
 * Facade class for the diet management.
 * It allows defining and retrieving raw materials and products.
 *
 */
public class Food{
	protected Map<String,NutritionalElement> mat =new TreeMap<>();
	protected Map<String,NutritionalElement> prod=new TreeMap<>();
	protected Map<String,NutritionalElement> recs=new TreeMap<>();
	protected Map<String,NutritionalElement> menu=new TreeMap<>();
	public class Element implements NutritionalElement{
		private String name;
		private double calories;
		private double proteins;
		private double carbs;
	    private double fat;
	    private boolean per100g;
	    
	    public Element(String name,double calories,double proteins,double carbs, double fat, boolean per100g){
	    	this.name=name;
	    	this.calories=calories;
	    	this.proteins=proteins;
	    	this.carbs=carbs;
	    	this.fat=fat;
	    	this.per100g=per100g;
	    }
	    @Override
	    public String getName() {
	    	return this.name;
	    }
	    @Override
	    public double getCalories() {
	    	return this.calories;
	    }
	    @Override
	    public double getProteins() {
	    	return this.proteins;
	    }
	    @Override
	    public double getCarbs() {
	    	return this.carbs;
	    }
	    @Override
	    public double getFat() {
	    	return this.fat;
	    }
	    @Override
	    public boolean per100g() {
	    	return this.per100g;
	    }
	    
	}
	
	/**
	 * Define a new raw material.
	 * 
	 * The nutritional values are specified for a conventional 100g amount
	 * @param name 		unique name of the raw material
	 * @param calories	calories per 100g
	 * @param proteins	proteins per 100g
	 * @param carbs		carbs per 100g
	 * @param fat 		fats per 100g
	 */
	public void defineRawMaterial(String name,double calories,double proteins,double carbs, double fat){
		Element M=new Element(name,calories,proteins,carbs,fat,true);
		mat.put(name, M);
	}
	
	/**
	 * Retrieves the collection of all defined raw materials
	 * 
	 * @return collection of raw materials though the {@link Element} interface
	 */
	public Collection<NutritionalElement> rawMaterials(){
		return mat.values();
	}
	
	/**
	 * Retrieves a specific raw material, given its name
	 * 
	 * @param name  name of the raw material
	 * 
	 * @return  a raw material though the {@link Element} interface
	 */
	public NutritionalElement getRawMaterial(String name){
		return mat.get(name);
	}

	/**
	 * Define a new packaged product.
	 * The nutritional values are specified for a unit of the product
	 * 
	 * @param name 		unique name of the product
	 * @param calories	calories for a product unit
	 * @param proteins	proteins for a product unit
	 * @param carbs		carbs for a product unit
	 * @param fat 		fats for a product unit
	 */
	public void defineProduct(String name,double calories,double proteins,double carbs,double fat){
		Element P=new Element(name,calories,proteins,carbs,fat,false);
		prod.put(name, P);
	}
	
	/**
	 * Retrieves the collection of all defined products
	 * 
	 * @return collection of products though the {@link Element} interface
	 */
	public Collection<NutritionalElement> products(){
		return prod.values();
	}
	
	/**
	 * Retrieves a specific product, given its name
	 * @param name  name of the product
	 * @return  a product though the {@link Element} interface
	 */
	public NutritionalElement getProduct(String name){
		return prod.get(name);
	}
	
	/**
	 * Creates a new recipe stored in this Food container.
	 *  
	 * @param name name of the recipe
	 * 
	 * @return the newly created Recipe object
	 */
	public Recipe createRecipe(String name) {
		Recipe R= new Recipe(name,mat);
		recs.put(name,R);
		return R;
	}
	
	/**
	 * Retrieves the collection of all defined recipes
	 * 
	 * @return collection of recipes though the {@link Element} interface
	 */
	public Collection<NutritionalElement> recipes(){
		return recs.values();
	}
	
	/**
	 * Retrieves a specific recipe, given its name
	 * 
	 * @param name  name of the recipe
	 * 
	 * @return  a recipe though the {@link Element} interface
	 */
	public NutritionalElement getRecipe(String name){		
		return recs.get(name);
	}
	
	/**
	 * Creates a new menu
	 * 
	 * @param name name of the menu
	 * 
	 * @return the newly created menu
	 */
	public Menu createMenu(String name) {
		Menu M= new Menu(name,prod,recs);
		menu.put(name,M);
		return M;
	}
	
}
