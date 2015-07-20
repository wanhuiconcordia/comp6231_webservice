package retailer;

import javax.jws.WebService;

import tools.Customer;
import tools.Item;
import tools.ItemShippingStatus;
import tools.SignUpResult;

@WebService(endpointInterface = "retailer.RetailerInterface")
public class RetailerImpl implements RetailerInterface {

	@Override
	public Item[] getCatalog(int customerReferenceNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemShippingStatus[] submitOrder(int customerReferenceNumber,
			Item[] itemList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SignUpResult signUp(String name, String password, String street1,
			String street2, String city, String state, String zip,
			String country) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer signIn(int customerReferenceNumber, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item[] getProducts(String productID) {
		// TODO Auto-generated method stub
		return null;
	}

}
