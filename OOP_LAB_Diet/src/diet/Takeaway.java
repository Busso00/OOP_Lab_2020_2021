package diet;

import java.util.*;

/**
 * Represents the main class in the
 * take-away system.
 * 
 * It allows adding restaurant, users, and creating orders.
 *
 */
public class Takeaway {
	private Map<String,Restaurant> r=new TreeMap<>();
	private Map<String,User> u=new TreeMap<>();
	private List<Order> o=new ArrayList<>();
	/**
	 * Adds a new restaurant to the take-away system
	 * 
	 * @param r the restaurant to be added
	 */
	public void addRestaurant(Restaurant r) {
		this.r.put(r.getName(),r);
	}
	
	/**
	 * Returns the collections of restaurants
	 * 
	 * @return collection of added restaurants
	 */
	public Collection<String> restaurants() {
		return this.r.keySet();
	}
	
	/**
	 * Define a new user
	 * 
	 * @param firstName first name of the user
	 * @param lastName  last name of the user
	 * @param email     email
	 * @param phoneNumber telephone number
	 * @return
	 */
	public User registerUser(String firstName, String lastName, String email, String phoneNumber) {
		User user=new User(firstName,lastName,email,phoneNumber);
		this.u.put(lastName+" "+firstName, user);
		return user;
	}
	
	/**
	 * Gets the collection of registered users
	 * 
	 * @return the collection of users
	 */
	public Collection<User> users(){
		return this.u.values();
	}
	
	/**
	 * Create a new order by a user to a given restaurant.
	 * 
	 * The order is initially empty and is characterized
	 * by a desired delivery time. 
	 * 
	 * @param user				user object
	 * @param restaurantName	restaurant name
	 * @param h					delivery time hour
	 * @param m					delivery time minutes
	 * @return
	 */
	public Order createOrder(User user, String restaurantName, int h, int m) {
		Restaurant res=this.r.get(restaurantName);
		HourMin hMR[]=res.getHours();
		HourMin temp=new HourMin(h,m);
		boolean attesa=false;
		for(int j=0;j<hMR.length;j++) {
			if((j%2)==0) {
				if(((temp.compareTo(hMR[j])>=0)&&(temp.compareTo(hMR[j+1])<0))||(attesa)){
					if(attesa) {
						temp=hMR[j];
						attesa=false;
					}
					break;
				}
			}else{
				if((temp.compareTo(hMR[j])>=0)&&((temp.compareTo(hMR[j+1])<0))) {
					attesa=true;
				}else if(j==(hMR.length-1)) {
					attesa=true;
				}
			}
		}
		if(attesa){
			temp=hMR[0];
		}
		Order order=new Order(user,restaurantName,temp);
		this.o.add(order);
		res.addOrder(order);
		return order;
	}
	
	/**
	 * Retrieves the collection of restaurant that are open
	 * at the given time.
	 * 
	 * @param time time to check open
	 * 
	 * @return collection of restaurants
	 */
	public Collection<Restaurant> openedRestaurants(String time){
		HourMin h=new HourMin(time);
		List<Restaurant> oRL=new ArrayList<>();
		List<Restaurant> rL=new ArrayList<>();
		rL.addAll(this.r.values());
		Restaurant rV[]=new Restaurant[rL.size()];
		rV=rL.toArray(rV);
		for(Restaurant rTemp:rV) {
			if(rTemp.isOpen(h)) {
				oRL.add(rTemp);
			}
		}
		return oRL;
				
	}
}
