package tools;
import java.io.Serializable;

/**
 * @author comp6231.team5
 *
 */
public class ItemShippingStatus extends Item implements Serializable{
	private static final long serialVersionUID = -4959224898727923197L;
	private boolean shippingStatus;
	
	/**
	 * constructor
	 * @param item
	 * @param shippingStatus
	 */
	public ItemShippingStatus(Item item, boolean shippingStatus) {
		super(item);
		this.shippingStatus = shippingStatus;
	}
	
	/**
	 * constructor
	 * @param manufacturerName
	 * @param productType
	 * @param unitPrice
	 * @param quantity
	 * @param shippingStatus
	 */
	public ItemShippingStatus(String manufacturerName, String productType, float unitPrice, int quantity, boolean shippingStatus){
		super(manufacturerName, productType, unitPrice, quantity);
		this.shippingStatus = shippingStatus;
	}
	
	/* (non-Javadoc)
	 * @see tools.Item#toString()
	 */
	public String toString(){
		return super.toString() + ",\t" + (shippingStatus ? "shipped" : "not shipped"); 
	}
}
