package warehouse;

import java.util.ArrayList;
import java.util.Scanner;

import javax.jws.WebService;

import tools.Item;
import tools.ItemList;
import tools.LoggerClient;
import tools.Product;
//Service Implementation
@WebService(endpointInterface = "warehouse.WarehouseInterface")
public class WareHouseImpl implements WarehouseInterface {
	

	//private InventoryManager inventoryManager;
		private String name;
		int minimumquantity=100;
		//private HashMap<String,Manufacturer> manufactures;
		private ArrayList<String> retailerNameList;
		private LoggerClient loggerClient;
		Scanner in ;
		
		/**
		 * Constructor
		 * @param orb2
		 * @param name
		 */
		public WareHouseImpl(String name){
			this. name = name;
			

		}
	@Override
	public ItemList getProductsByID(String productID) {
		ItemList returnitems=new ItemList();
		Product pt1 = new Product("sony","tv",400);
		pt1.productID ="1";
	
		Product pt2 = new Product("samsung","video",1000);
		pt2.productID= "2";
		
		Item im1 = new Item(pt1, 20);
		Item im2 = new Item(pt2, 30);
		returnitems.innerItemList.add(im1);
		returnitems.innerItemList.add(im2);
		return returnitems;
	}

	@Override
	public ItemList getProductsByType(String productType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemList getProductsByRegisteredManufacturers(String manufacturerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemList getProducts(String productID, String manufacturerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean registerRetailer(String retailerName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unregisterRegailer(String retailerName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemList shippingGoods(ItemList itemList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
