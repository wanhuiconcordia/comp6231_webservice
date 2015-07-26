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
import tools.ProductList;
//Service Implementation
@WebService(endpointInterface = "warehouse.WarehouseInterface")
public class WarehouseImpl implements WarehouseInterface {


	private InventoryManager inventoryManager;
	private String name;
	int minimumquantity=20;
	private ArrayList<ManufacturerInterface> manufactures;
	private ArrayList<String> retailers;
	private LoggerClient loggerClient;
	Scanner in ;

	/**
	 * Constructor
	 * @param name
	 */
	public WarehouseImpl(String name){
		this. name = name;
		manufactures= new ArrayList<ManufacturerInterface>();
		retailers=new ArrayList<String>();
		loggerClient=new LoggerClient();
		if(connect()){
			System.out.println("connected to the manufacturer");
			inventoryManager=new InventoryManager(name);
			for(ManufacturerInterface manufact: manufactures){
				ProductList productList = manufact.getProductList();
				System.out.println( productList.toString());
				if(productList.innerProductList.size()!=0){
					System.out.println( productList.toString());
					for(Product product: productList.innerProductList){
						System.out.println("the product is:"+product.productType);
						String key = product.manufacturerName + product.productType;
						Item inventoryItem = inventoryManager.inventoryItemMap.get(key);
						//System.out.println("inventory item "+inventoryItem.productType);
						if(inventoryItem == null){
							System.out.println("item added");
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
	
	/**
	 * Provide interface for user to input the manufacturer' ports for connecting
	 */
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
					manufacturer = service.getPort(ManufacturerInterface.class);
					System.out.println("Obtained a handle on server object: " + manufacturer.getName());
					manufactures.add(manufacturer);
					connected = true;

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
	
	
	/**
	 * replenish inventory 
	 */
	public void replenish(){
		System.out.println("enterd to replanish");
		for(Item item: inventoryManager.inventoryItemMap.values()){
			
			if(item.quantity < minimumquantity){
				System.out.println(" called the replanish");
				for(ManufacturerInterface manufacturer:manufactures){
					
					
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
									System.out.println("payment recevied");
									inventoryManager.saveItems();
									
								}else{
									loggerClient.write(name + ": manufacturer.receivePayment return null!");
								}
							}
						}else{
							System.out.println("manufacturer.getName()    !=     item.manufacturerName)");
						}
					
				}
			}

		}
	}
	/* (non-Javadoc)
	 * @see warehouse.WarehouseInterface#getProductsByID(java.lang.String)
	 */
	@Override
	public ItemList getProductsByID(String productID) {
		System.out.println("getProductsByID is called...");
		ItemList returnitems=new ItemList();

		if(!(productID.equals(""))){
			
			for(Item inventoryItem:inventoryManager.inventoryItemMap.values()){
				if(inventoryItem != null&& inventoryItem.productID.equals(productID)){

				returnitems.addItem(inventoryItem);

			}
			}
		}
		else{
			System.out.println("entering loop"+inventoryManager.inventoryItemMap.toString() );
			for(Item i:inventoryManager.inventoryItemMap.values()){
				System.out.println("entered loop and item:"+i.productType);
				returnitems.addItem(i);
			}

		}
		
		System.out.println("return:");
		System.out.println(returnitems.toString());
		return returnitems;

	}

	/* (non-Javadoc)
	 * @see warehouse.WarehouseInterface#getProductsByType(java.lang.String)
	 */
	@Override
	public ItemList getProductsByType(String productType) {
		// TODO Auto-generated method stub
		ItemList returnitems=new ItemList();
		for(Item inventoryitems:inventoryManager.inventoryItemMap.values()){

			if(inventoryitems!=null&&inventoryitems.productType.equals(productType)){
				returnitems.addItem(inventoryitems);
			}
		}
		System.out.println("return:");
		System.out.println(returnitems.toString());
		return returnitems;
	}

	/* (non-Javadoc)
	 * @see warehouse.WarehouseInterface#getProductsByRegisteredManufacturers(java.lang.String)
	 */
	@Override
	public ItemList getProductsByRegisteredManufacturers(String manufacturerName){
		// TODO Auto-generated method stub
		ItemList returnitems=new ItemList();
		for(ManufacturerInterface manufacture:manufactures){
			System.out.println(manufacture.getName()+"  "+manufacturerName);
			if(manufacture.getName().equals(manufacturerName)){

				for(Item inventoryitems:inventoryManager.inventoryItemMap.values()){

					if(inventoryitems!=null&&inventoryitems.manufacturerName.equals(manufacturerName)){
						returnitems.addItem(inventoryitems);
					}
				}

			}
		}
		System.out.println("return:");
		System.out.println(returnitems.toString());
		return returnitems;
	}

	/* (non-Javadoc)
	 * @see warehouse.WarehouseInterface#getProducts(java.lang.String, java.lang.String)
	 */
	@Override
	public ItemList getProducts(String productID, String manufacturerName) {
		// TODO Auto-generated method stub
		ItemList returnitems=new ItemList();

		if(!(productID.equals(""))){

		//	Item inventoryItem = inventoryManager.inventoryItemMap.get(productID);
			for(Item inventoryItem:inventoryManager.inventoryItemMap.values()){
			if(inventoryItem!=null&&inventoryItem.productID.equals(productID) && inventoryItem.manufacturerName.equals(manufacturerName)){

				returnitems.addItem(inventoryItem);

			}
			}

		}
		else{

			for(Item i:inventoryManager.inventoryItemMap.values()){

				returnitems.addItem(i);
			}
		

		}
		System.out.println("return:");
		System.out.println(returnitems.toString());
		return returnitems;
	}

	/* (non-Javadoc)
	 * @see warehouse.WarehouseInterface#registerRetailer(java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see warehouse.WarehouseInterface#unregisterRegailer(java.lang.String)
	 */
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
		System.out.println("ship good called");
		ItemList availableItems=new ItemList();
		for(String rname:retailers){
			if(rname.equals(reatilername))
			{
				for(Item item: itemlist.innerItemList){
					String key = item.manufacturerName+ item.productType;
					Item inventoryItem = inventoryManager.inventoryItemMap.get(key);
					if(inventoryItem != null){
						System.out.println("inventory item to send"+inventoryItem.productType+"   "+item.productType);;
						if(inventoryItem.quantity < item.quantity){
							System.out.println("added item");
							availableItems.addItem(new Item(inventoryItem.manufacturerName,inventoryItem.productType,inventoryItem.unitPrice,inventoryItem.quantity));
							inventoryItem.quantity=0;
						}else{
							availableItems.addItem(new Item(item.manufacturerName,item.productType,item.unitPrice,item.quantity));
							inventoryItem.quantity=(inventoryItem.quantity - item.quantity);
						}
					synchronized (inventoryItem) {
						
							inventoryManager.saveItems();
						}
					}
				}
			}
			else{
				loggerClient.write("retailer not registered(only registered reatilers gets goods). Please register first.");
				System.out.println("retailer not registered(only registered reatilers gets goods). Please register first.");
			}
		}
		replenish();
		System.out.println(availableItems.toString());
		String log = new String();
		for(Item item: availableItems.innerItemList){
			log = log + "Manufacturer name:" + item.manufacturerName
					+ ", Product type:" + item.productType 
					+ ", Unit price:" + item.unitPrice+ ",	quantity:" + item.quantity + "</br>";
		}
		if(log.length() > 0&& !(availableItems.innerItemList.isEmpty())){
			loggerClient.write(name + ": available items:");
			loggerClient.write(log);
		}
		System.out.println("goods shiped"+availableItems.toString());
		return availableItems;

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
