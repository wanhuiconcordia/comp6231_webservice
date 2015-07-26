package manufacturer;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import tools.Product;
import tools.Item;
import tools.ProductList;
//Service Endpoint Interface
@WebService
@SOAPBinding(style = Style.RPC)

public interface ManufacturerInterface {
	/**
	 * @param item
	 * @return String order Number
	 */
	@WebMethod String processPurchaseOrder (Item item);
	
	/**
	 * @param aProdName
	 * @return Product
	 */
	@WebMethod Product getProductInfo (String aProdName);
	
	/**
	 * @param orderNum
	 * @param totalPrice
	 * @return true if success otherwise false
	 */
	@WebMethod boolean receivePayment (String orderNum, float totalPrice);
	/**
	 * @return ProductList 
	 */
	@WebMethod ProductList getProductList();
	/**
	 * @return manufacturer name
	 */
	@WebMethod String getName();	
}