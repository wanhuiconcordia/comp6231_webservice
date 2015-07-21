package warehouse;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import manufacturer.ManufacturerInterface;
import tools.Item;
import tools.ItemList;
import tools.LoggerClient;
import tools.Product;
//Service Implementation
@WebService(endpointInterface = "warehouse.WarehouseInterface")
public class WareHouseImpl implements WarehouseInterface {


	private InventoryManager inventoryManager;
	private String name;
	int minimumquantity=20;
	private ArrayList<ManufacturerInterface> manufactures;
	private ArrayList<String> retailers;
	private LoggerClient loggerClient;
	Scanner in ;

	/**
	 * Constructor
	 * @param orb2
	 * @param name
	 * @throws RemoteException 
	 */
	public WareHouseImpl(String name) throws RemoteException{
		this. name = name;
		manufactures= new ArrayList<ManufacturerInterface>();
		retailers=new ArrayList<String>();
		if(connect()){
			inventoryManager=new InventoryManager(name);
			for(ManufacturerInterface manufact: manufactures){
				ArrayList<Product> productList = manufact.getProductList();
				if(productList != null){
					for(Product product: productList){
						String key = product.manufacturerName + product.productType;
						Item inventoryItem = inventoryManager.inventoryItemMap.get(key);
						if(inventoryItem == null){
							inventoryManager.inventoryItemMap.put(key, new Item(product.manufacturerName,product.productType,product.unitPrice, 0));
						}
					}
				}
			}
			replenish();
		}else{
			inventoryManager=new InventoryManager(name);
		}


	}
	public boolean connect(){
		boolean connected= false;
		Scanner in = new Scanner(System.in);
		while(true){
			System.out.print("Please input the port number of the manufacturer service to establish connection (q to finish):");

			String port = in.nextLine();
			if(port.equals("q")){
				break;
			}else{
				String urlStr = "http://localhost:" + port + "/ws/manufacturer?wsdl";
				URL url;
				try {
					url = new URL(urlStr);
					QName qname = new QName("http://manufacturer/", "ManufacturerImplService");
					ManufacturerInterface manufacturer;
					Service service = Service.create(url, qname);
					manufacturer = service.getPort(ManufacturerInterface .class);
					System.out.println("Obtained a handle on server object: " + manufacturer.getName());
					manufactures.add(manufacturer);

				}catch (MalformedURLException e1) {
					e1.printStackTrace();
				}catch (Exception e) {
					System.out.println("Failed to access the WSDL at:" + urlStr);
					//return connected;
				}
			}
		}	
		in.close();
		return connected;
	}
	public void replenish(){
		for(Item item: inventoryManager.inventoryItemMap.values()){
			if(item.quantity < minimumquantity){
				for(ManufacturerInterface manufacturer:manufactures){
					if(manufacturer == null){
						loggerClient.write(name + ": Failed to get manufacturer from manufactures!");
					}else{
						try {
							if(manufacturer.getName().equals(item.manufacturerName))
							{


								Item orderItem = new Item(item.manufacturerName,item.productType,item.unitPrice,item.quantity);

								int oneTimeOrderCount = 40;
								orderItem.quantity=oneTimeOrderCount;
								String orderNum = manufacturer.processPurchaseOrder(orderItem);
								if(orderNum == null){
									loggerClient.write(name + ": manufacturer.processPurchaseOrder return null!");
								}else{
									if(manufacturer.receivePayment(orderNum, orderItem.unitPrice * orderItem.quantity)){
										item.quantity=item.quantity + oneTimeOrderCount;
										inventoryManager.saveItems();
									}else{
										loggerClient.write(name + ": manufacturer.receivePayment return null!");
									}
								}

							}
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

		}
	}
	@Override
	public ItemList getProductsByID(String productID) {
		ItemList returnitems=new ItemList();

		if((productID!=null)){

			Item inventoryItem = inventoryManager.inventoryItemMap.get(productID);
			if(inventoryItem != null){

				returnitems.addItem(inventoryItem);

			}

		}
		else{

			for(Item i:inventoryManager.inventoryItemMap.values()){

				returnitems.addItem(i);
			}

		}
		return returnitems;

	}

	@Override
	public ItemList getProductsByType(String productType) {
		// TODO Auto-generated method stub
		ItemList returnitems=new ItemList();
		for(Item inventoryitems:inventoryManager.inventoryItemMap.values()){

			if(inventoryitems.productType.equals(productType)){
				returnitems.addItem(inventoryitems);
			}
		}
		return returnitems;
	}

	@Override
	public ItemList getProductsByRegisteredManufacturers(String manufacturerName){
		// TODO Auto-generated method stub
		ItemList returnitems=new ItemList();
		for(ManufacturerInterface manufacture:manufactures){
			try {
				if(manufacture.getName().equals(manufacturerName)){

					for(Item inventoryitems:inventoryManager.inventoryItemMap.values()){

						if(inventoryitems.manufacturerName.equals(manufacturerName)){
							returnitems.addItem(inventoryitems);
						}
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return returnitems;
	}

	@Override
	public ItemList getProducts(String productID, String manufacturerName) {
		// TODO Auto-generated method stub
		ItemList returnitems=new ItemList();

		if((productID!=null)){

			Item inventoryItem = inventoryManager.inventoryItemMap.get(productID);
			if(inventoryItem != null && inventoryItem.manufacturerName.equals(manufacturerName)){

				returnitems.addItem(inventoryItem);

			}

		}
		else{

			for(Item i:inventoryManager.inventoryItemMap.values()){

				returnitems.addItem(i);
			}

		}
		return returnitems;
	}

	@Override
	public boolean registerRetailer(String retailerName) {
		if(retailerName.isEmpty()){

			return false;

		}
		else{
			if(!retailers.contains(retailerName)){
				retailers.add(retailerName);
			}
			return true;

		}
	}

	@Override
	public boolean unregisterRegailer(String retailerName) {
		// TODO Auto-generated method stub
		if(retailerName.isEmpty()){

			return false;

		}else{
			if(retailers.contains(retailerName)){

				retailers.remove(retailerName);
				return true;

			}else{

				return false;
			}


		}

	}

	@Override
	public ItemList shippingGoods(ItemList itemlist,String reatilername) {

		ItemList availableItems=new ItemList();
		for(String rname:retailers){
			if(rname.equals(reatilername))
			{



				for(Item item: itemlist.innerItemList){
					String key = item.manufacturerName+ item.productType;
					Item inventoryItem = inventoryManager.inventoryItemMap.get(key);
					if(inventoryItem != null){
						if(inventoryItem.quantity < item.quantity){
							availableItems.addItem(new Item(inventoryItem.manufacturerName,inventoryItem.productType,inventoryItem.unitPrice,inventoryItem.quantity));
							inventoryItem.quantity=0;
						}else{
							availableItems.addItem(new Item(item.manufacturerName,item.productType,item.unitPrice,item.quantity));
							inventoryItem.quantity=(inventoryItem.quantity - item.quantity);
						}
						inventoryManager.saveItems();
					}
				}
			}
			else{
				loggerClient.write("retailer not registered(only registered reatilers gets goods). Please register first.");
				System.out.println("retailer not registered(only registered reatilers gets goods). Please register first.");
			}
		}
		replenish();

		String log = new String();
		for(Item item: availableItems.innerItemList){
			log = log + "Manufacturer name:" + item.manufacturerName
					+ ", Product type:" + item.productType 
					+ ", Unit price:" + item.unitPrice+ ",	quantity:" + item.quantity + "</br>";
		}
		if(log.length() > 0){
			loggerClient.write(name + ": available items:");
			loggerClient.write(log);
		}

		return availableItems;

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
