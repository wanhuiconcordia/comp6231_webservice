package tools;

import java.io.Serializable;

/**
 * @author comp6231.team5
 *
 */
public class Product implements Serializable {
	private static final long serialVersionUID = 6733918368705678280L;
	protected String manufacturerName;
	protected String productType;
	protected float unitPrice;
	
	/**
	 * constructor
	 * @param manufacturerName
	 * @param productType
	 * @param unitPrice
	 */
	public Product(String manufacturerName, String productType, float unitPrice){
		this.manufacturerName = manufacturerName;
		this.productType = productType;
		this.unitPrice = unitPrice;
	}
	
	/**
	 * constructor
	 * @param product
	 */
	public Product(Product product){
		this.manufacturerName = product.manufacturerName;
		this.productType = product.productType;
		this.unitPrice = product.unitPrice;
	}
	
	/**
	 * determine if the current product is the same as the other one
	 * @param otherProduct
	 * @return true if the same, false if not
	 */
	public boolean isSame(Product otherProduct){
		return (manufacturerName == otherProduct.manufacturerName)
				&& (productType == otherProduct.productType)
				&& (unitPrice == otherProduct.unitPrice);
	}
	
	/**
	 * @return manufacturer name
	 */
	public String getManufacturerName(){
		return manufacturerName;
	}
	
	/**
	 * @return product type
	 */
	public String getProductType(){
		return productType;
	}
	
	/**
	 * @return
	 */
	public float getUnitPrice(){
		return unitPrice;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Product clone(){
		return new Product(this);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return manufacturerName
				+ ",\t" + productType 
				+ ",\t" + unitPrice;
	}
}
