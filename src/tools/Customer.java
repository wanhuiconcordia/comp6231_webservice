package tools;
import java.io.Serializable;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
/**
 * @author comp6231.team5
 *
 */
public class Customer implements Serializable{
	private static final long serialVersionUID = -6031592301495856489L;
	private int customerReferenceNumber;
	private String name;
	private String password;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String zip;
	private String country;

	/**
	 * Constructor
	 * @param customerReferenceNumber
	 * @param name
	 * @param password
	 * @param street1
	 * @param street2
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public Customer(int customerReferenceNumber,
			String name,
			String password,
			String street1,
			String street2,
			String city,
			String state,
			String zip,
			String country){
		this.customerReferenceNumber = customerReferenceNumber;
		this.name = name;
		this.password = password;
		this.street1 = street1;
		this.street2 = street2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "CustomerReferenceNumber:" + customerReferenceNumber 
				+ ", Name:" + name
				+ ", Password:" + password
				+ ", Street1:" + street1
				+ ", Street2:" + street2
				+ ", City:" + city
				+ ", State:" + state
				+ ", Zip:" + zip
				+ ", Country:" + country;
	}
	
	/**
	 * @return name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return password
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * @return customerReference
	 */
	public int getCustomerReferenceNumber(){
		return customerReferenceNumber;
	}
	
	/**
	 * store the customer to an xml element
	 * @return element format of the customer
	 */
	public Element toXmlElement() {
		DefaultElement customerElem = new DefaultElement("customer");
		
		Element subElem = customerElem.addElement("customerReferenceNumber");
		subElem.setText(String.valueOf(customerReferenceNumber));
		
		subElem = customerElem.addElement("name");
		subElem.setText(name);
		
		subElem = customerElem.addElement("password");
		subElem.setText(password);
		
		subElem = customerElem.addElement("street1");
		subElem.setText(street1);
		
		subElem = customerElem.addElement("street2");
		subElem.setText(street2);
		
		subElem = customerElem.addElement("city");
		subElem.setText(city);
		
		subElem = customerElem.addElement("state");
		subElem.setText(state);
		
		subElem = customerElem.addElement("zip");
		subElem.setText(zip);
		
		subElem = customerElem.addElement("country");
		subElem.setText(country);
		return customerElem;
	}
}
