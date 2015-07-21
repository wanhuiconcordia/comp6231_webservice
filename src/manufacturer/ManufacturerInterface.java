package manufacturer;

import java.rmi.RemoteException;
import java.util.ArrayList;

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
	@WebMethod String processPurchaseOrder (Item item) throws RemoteException;
	@WebMethod Product getProductInfo (String aProdName) throws RemoteException;
	@WebMethod boolean receivePayment (String orderNum, float totalPrice) throws RemoteException;
	@WebMethod ArrayList<Product> getProductList() throws RemoteException;
}