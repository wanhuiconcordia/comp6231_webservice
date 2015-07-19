package tools;
import java.io.Serializable;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

/**
 * 
 * @author comp6231.team5
 *
 */
public class Item extends Product implements Serializable{
	protected static final long serialVersionUID = -1927708729616470764L;
	protected int quantity;
	
	/**
	 * constructor
	 * @param otherItem
	 */
	public Item(Item otherItem){
		super(otherItem.manufacturerName, otherItem.productType, otherItem.unitPrice);
		this.quantity = otherItem.quantity;
	}
	/**
	 * @param manufacturerName
	 * @param productType
	 * @param unitPrice
	 * @param quantity
	 */
	public Item(String manufacturerName, String productType, float unitPrice, int quantity){
		super(manufacturerName, productType, unitPrice);
		this.quantity = quantity;
	}
	
	/**
	 * constructor
	 * @param product
	 * @param quantity
	 */
	public Item(Product product, int quantity){
		super(product);
		this.quantity = quantity;
	}
	
	/* (non-Javadoc)
	 * @see tools.Product#toString()
	 */
	public String toString(){
		return super.toString() + ",\t" + quantity; 
	}
	
	/**
	 * @return quantity of the item
	 */
	public int getQuantity(){
		return quantity;
	}
	
	/**
	 * @param q
	 */
	public void setQuantity(int q){
		quantity = q;
	}
	
	/* (non-Javadoc)
	 * @see tools.Product#clone()
	 */
	public Item clone(){
		return new Item(this);
	}
	
	/**
	 * @return the clone product from the current item 
	 */
	public Product cloneProduct(){
		return super.clone();
	}
	
	/**
	 * determine whether the current item is the same as the other item
	 * @param otherItem
	 * @return ture if the same, false if not
	 */
	public boolean isSameProductAs(Item otherItem){
		return (this.manufacturerName.equals(otherItem.manufacturerName))
				&& (this.productType.equals(otherItem.productType))
				&& (this.unitPrice == otherItem.unitPrice);
	}
	/**
	 * save the current item to an  Element object
	 * @return the Element object
	 */
	public Element toXmlElement() {
		DefaultElement customerElem = new DefaultElement("item");
		
		Element subElem = customerElem.addElement("manufacturerName");
		subElem.setText(manufacturerName);
		
		subElem = customerElem.addElement("productType");
		subElem.setText(productType);
		
		subElem = customerElem.addElement("unitPrice");
		subElem.setText(String.valueOf(unitPrice));
		
		subElem = customerElem.addElement("quantity");
		subElem.setText(String.valueOf(quantity));
		return customerElem;
	}
}
