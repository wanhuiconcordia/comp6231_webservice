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
	@WebMethod String processPurchaseOrder (Item item);
	@WebMethod Product getProductInfo (String aProdName);
	@WebMethod boolean receivePayment (String orderNum, float totalPrice);
	@WebMethod ProductList getProductList();
	@WebMethod String getName();
}