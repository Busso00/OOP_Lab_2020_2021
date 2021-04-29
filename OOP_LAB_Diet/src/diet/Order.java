package diet;

import java.util.*;

/**
 * Represents an order in the take-away system
 */
public class Order implements Comparable<Order>{
	private User u;
	private String resName;
	private HourMin hMO;
	private OrderStatus Status=OrderStatus.ORDERED;
	private PaymentMethod Payment=PaymentMethod.CASH;
	private List<Menqt> menus=new ArrayList<>();
	public class Menqt implements Comparable<Menqt>{
		private String name;
		private int qta;
		public Menqt(String name, int qta) {
			this.name=name;
			this.qta=qta;
		}
		@Override
		public int compareTo(Menqt q) {
			return this.name.compareTo(q.name);
		}
		@Override
		public String toString() {
			return this.name.toUpperCase()+"->"+this.qta;
		}
	}
	public Order(User u,String resName,int h,int m) {
		this.u=u;
		this.resName=resName;
		this.hMO=new HourMin(h,m);
	}
	public Order(User u,String resName,HourMin hMO) {
		this.u=u;
		this.resName=resName;
		this.hMO=hMO;
	}
	/**
	 * Defines the possible order status
	 */
	public enum OrderStatus {
		ORDERED, READY, DELIVERED;
	}
	/**
	 * Defines the possible valid payment methods
	 */
	public enum PaymentMethod {
		PAID, CASH, CARD;
	}
		
	/**
	 * Total order price
	 * @return order price
	 */
	public double Price() {
		return -1.0;
	}
	
	public User getUser() {
		return this.u;
	}
	/**
	 * define payment method
	 * 
	 * @param method payment method
	 */
	public void setPaymentMethod(PaymentMethod method) {
		this.Payment=method;
	}
	
	/**
	 * get payment method
	 * 
	 * @return payment method
	 */
	public PaymentMethod getPaymentMethod() {
		return this.Payment;
	}
	
	/**
	 * change order status
	 * @param newStatus order status
	 */
	public void setStatus(OrderStatus newStatus) {
		this.Status=newStatus;
	}
	
	/**
	 * get current order status
	 * @return order status
	 */
	public OrderStatus getStatus(){
		return this.Status;
	}
	
	/**
	 * Add a new menu with the relative order to the order.
	 * The menu must be defined in the {@link Food} object
	 * associated the restaurant that created the order.
	 * 
	 * @param menu     name of the menu
	 * @param quantity quantity of the menu
	 * @return this order to enable method chaining
	 */
	public Order addMenus(String menu, int quantity) {
		Menqt mQ=new Menqt(menu,quantity);
		Menqt []arr=new Menqt[this.menus.size()];
		arr=this.menus.toArray(arr);
		for(Menqt temp:arr) {
			if(temp.name==menu) {
				temp.qta=quantity;
				Collections.sort(this.menus);
				return this;
			}
		}
		this.menus.add(mQ);
		Collections.sort(this.menus);
		return this;
	}
	
	/**
	 * Converts to a string as:
	 * <pre>
	 * RESTAURANT_NAME, USER_FIRST_NAME USER_LAST_NAME : DELIVERY(HH:MM):
	 * 	MENU_NAME_1->MENU_QUANTITY_1
	 * 	...
	 * 	MENU_NAME_k->MENU_QUANTITY_k
	 * </pre>
	 */
	@Override
	public String toString() {
		String ordRes= this.resName+", "+this.u.toString()+" : "+this.hMO.toString()+":\n";
		for(Menqt mQ:menus) {
			ordRes+="\t"+mQ.toString()+"\n";
		}
		return ordRes;
	}
	@Override
	public int compareTo(Order b) {
		int c;
		if((c=this.resName.compareTo(b.resName))!=0)
			return c;
		else
			if((c=this.u.toString().compareTo(b.u.toString()))!=0)
				return c;
		return this.hMO.compareTo(b.hMO);
	}
	
}
