package retailer;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import tools.Customer;
import tools.Item;
import tools.ItemList;
import tools.ItemShippingStatus;
import tools.ItemShippingStatusList;
import tools.SignUpResult;

//Service Endpoint Interface
@WebService
@SOAPBinding(style = Style.RPC)

public interface RetailerInterface {
	@WebMethod ItemList getCatalog (int customerReferenceNumber);
	@WebMethod ItemShippingStatusList submitOrder (int customerReferenceNumber, ItemList orderItemList);
	@WebMethod SignUpResult signUp (String name, String password, String street1, String street2, String city, String state, String zip, String country);
	@WebMethod Customer signIn (int customerReferenceNumber, String password);
	@WebMethod ItemList getProducts (String productID);
}