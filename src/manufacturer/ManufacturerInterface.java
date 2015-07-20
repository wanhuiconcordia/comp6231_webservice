package manufacturer;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
//Service Endpoint Interface
@WebService
@SOAPBinding(style = Style.RPC)

public interface ManufacturerInterface {
	@WebMethod String processPurchaseOrder (tools.Item item);
	  tools.Product getProductInfo (String aProdName);
	  boolean receivePayment (String orderNum, float totalPrice);
	  tools.ProductList getProductList();
}