package tools;


/**
 * @author comp6231.team5
 *
 */
public class Product {
	public String productID;
	public String manufacturerName;
	public String productType;
	public float unitPrice;

	/**
	 * constructor
	 * @param String manufacturerName
	 * @param String productType
	 * @param float unitPrice
	 */
	public Product(String manufacturerName, String productType, float unitPrice){
		this.productID = String.valueOf((manufacturerName + productType).hashCode());
		this.manufacturerName = manufacturerName;
		this.productType = productType;
		this.unitPrice = unitPrice;
	}

	/**
	 * constructor
	 * @param String productID
	 * @param String manufacturerName
	 * @param String productType
	 * @param float unitPrice
	 */
	public Product(String productID, String manufacturerName, String productType, float unitPrice){

		String tmpProductID = String.valueOf((manufacturerName + productType).hashCode());
		if(!tmpProductID.equals(productID)){
			System.out.println("Product Id does not match (manufacturerName + productType).hashCode()");
		}

		this.productID = tmpProductID;
		this.manufacturerName = manufacturerName;
		this.productType = productType;
		this.unitPrice = unitPrice;
	}

	/**
	 * constructor
	 * @param Product product
	 */
	public Product(Product product){
		this.productID = product.productID;
		this.manufacturerName = product.manufacturerName;
		this.productType = product.productType;
		this.unitPrice = product.unitPrice;
	}

	/**
	 * determine if the current product is the same as the other one
	 * @param Product otherProduct
	 * @return true if the same, false if not
	 */
	public boolean isSame(Product otherProduct){
		return (productID.equals(otherProduct.productID));
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
		return productID
				+ ",\t" + manufacturerName
				+ ",\t" + productType 
				+ ",\t" + unitPrice;
	}
}
