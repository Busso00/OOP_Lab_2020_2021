package diet;

/**
 * Represent a take-away system user
 *  
 */
public class User implements Comparable<User>{
	String fn;
	String ln;
	String email;
	String phone;
	public User(String fn,String ln) {
		this.fn=fn;
		this.ln=ln;
	}
	
	public User(String fn,String ln,String email,String phone) {
		this.fn=fn;
		this.ln=ln;
		this.email=email;
		this.phone=phone;
	}
	/**
	 * get user's last name
	 * @return last name
	 */
	public String getLastName() {
		return this.ln;
	}
	
	/**
	 * get user's first name
	 * @return first name
	 */
	public String getFirstName() {
		return this.fn;
	}
	
	/**
	 * get user's email
	 * @return email
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * get user's phone number
	 * @return  phone number
	 */
	public String getPhone() {
		return this.phone;
	}
	
	/**
	 * change user's email
	 * @param email new email
	 */
	public void SetEmail(String email) {
		this.email=email;
	}
	@Override
	public String toString() {
		return this.fn+" "+this.ln;
	}
	@Override
	public int compareTo(User b) {
		return this.toString().compareTo(b.toString());
	}
	/**
	 * change user's phone number
	 * @param phone new phone number
	 */
	public void setPhone(String phone) {
		this.phone=phone;
	}
	
	
}
