package warehouse;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import tools.ItemList;

//Service Endpoint Interface
@WebService
@SOAPBinding(style = Style.RPC)

public interface WarehouseInterface {
	@WebMethod ItemList getProductsByID (String productID);
	// get the specific product by id, if null/empty, all returned
	@WebMethod ItemList getProductsByType (String productType);
	// get a list of products by product type, if null/empty,all returned
	@WebMethod ItemList getProductsByRegisteredManufacturers (String manufacturerName);
	//get a list of products by manufacturer name,if null/empty, all returned
	@WebMethod ItemList getProducts (String productID, String manufacturerName);
	boolean registerRetailer (String retailerName);
	boolean unregisterRegailer (String retailerName);
	@WebMethod ItemList shippingGoods (ItemList itemList,String reatilername);
	String getName ();
}