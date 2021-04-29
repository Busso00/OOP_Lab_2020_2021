package diet;
import java.util.*;

import diet.Order.OrderStatus;

/**
 * Represents a restaurant in the take-away system
 *
 */
public class Restaurant{
	private Food cibo;
	private String name;
	private List<HourMin> hourMinutes=new ArrayList<>();
	private List<Order> ord=new ArrayList<>();
	/**
	 * Constructor for a new restaurant.
	 * 
	 * Materials and recipes are taken from
	 * the food object provided as argument.
	 * 
	 * @param name	unique name for the restaurant
	 * @param food	reference food object
	 */
	public Restaurant(String name, Food food) {
		this.cibo=food;
		this.name=name;
	}
	
	/**
	 * gets the name of the restaurant
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Define opening hours.
	 * 
	 * The opening hours are considered in pairs.
	 * Each pair has the initial time and the final time
	 * of opening intervals.
	 * 
	 * for a restaurant opened from 8:15 until 14:00 and from 19:00 until 00:00, 
	 * is thoud be called as {@code setHours("08:15", "14:00", "19:00", "00:00")}.
	 * 
	 * @param hm a list of opening hours
	 */
	public void setHours(String ... hm) {
		HourMin h;
		if((hm.length%2)==0) {
			for(String s:hm) {
				h=new HourMin(s);
				this.hourMinutes.add(h);
			}
			
			Collections.sort(this.hourMinutes);
		}
	}
	
	public HourMin[] getHours() {
		HourMin h[]=new HourMin[this.hourMinutes.size()];
		h=this.hourMinutes.toArray(h);
		return h;
	}
	
	public Menu getMenu(String name) {
		return cibo.getMenu(name);
	}
	
	/**
	 * Creates a new menu
	 * 
	 * @param name name of the menu
	 * 
	 * @return the newly created menu
	 */
	public Menu createMenu(String name) {
		Menu m=cibo.createMenu(name);
		return m;
	}

	/**
	 * Find all orders for this restaurant with 
	 * the given status.
	 * 
	 * The output is a string formatted as:
	 * <pre>
	 * Napoli, Judi Dench : (19:00):
	 * 	M6->1
	 * Napoli, Ralph Fiennes : (19:00):
	 * 	M1->2
	 * 	M6->1
	 * </pre>
	 * 
	 * The orders are sorted by name of restaurant, name of the user, and delivery time.
	 * 
	 * @param status the status of the searched orders
	 * 
	 * @return the description of orders satisfying the criterion
	 */
	public String ordersWithStatus(OrderStatus status) {
		String res="";
		Order[] tempO=new Order[ord.size()];
		Collections.sort(ord);
		tempO=ord.toArray(tempO);
		for(Order o:tempO) {
			if(o.getStatus()==status) {
				res+=o.toString();
			}
		}
		return res;
	}
	
	public void addOrder(Order o) {
		this.ord.add(o);
	}
	
	public boolean isOpen(HourMin temp) {
		HourMin hMR[]=this.getHours();
		for(int j=0;j<(hMR.length-1);j++) {
			if((j%2)==0) {
				if((temp.compareTo(hMR[j])>=0)&&(temp.compareTo(hMR[j+1])<0)){
					return true;
				}
			}
		}
		return false;
	}
}
