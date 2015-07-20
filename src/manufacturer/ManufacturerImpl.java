package manufacturer;

import java.util.HashMap;
import java.util.Random;

import javax.jws.WebService;

import tools.Item;
import tools.LoggerClient;
import tools.Product;
import tools.ProductList;
//Service Implementation
@WebService(endpointInterface = "manufacturer.ManufacturerInterface")

public class ManufacturerImpl implements ManufacturerInterface {
	private String name;
//	private HashMap<String, Item> purchaseOrderMap;
//	private int orderNum;
//	private LoggerClient loggerClient;
//	private PurchaseOrderManager purchaseOrderManager;
//	Random random ; 
	public ManufacturerImpl(String name) {
		this.name = name;
	}
	
	@Override
	public String processPurchaseOrder(Item item) {
		System.out.println("processPurchaseOrder is called...");
		System.out.println(item.toString());
		return "aha";
	}

	@Override
	public Product getProductInfo(String aProdName) {
		System.out.println("getProductInfo is called...");
		return new Product("Samsung", "TV", 111);
	}

	@Override
	public boolean receivePayment(String orderNum, float totalPrice) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ProductList getProductList() {
		ProductList productList = new ProductList();
		productList.addProduct(new Product("Samsung", "Cell phone", 120));
		productList.addProduct(new Product("Apple", "DVD", 220));
		return productList;
	}

}
