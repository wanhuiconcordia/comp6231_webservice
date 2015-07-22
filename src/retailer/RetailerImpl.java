package retailer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import retailer.CustomerManager;
import tools.Customer;
import tools.Item;
import tools.ItemList;
import tools.ItemShippingStatus;
import tools.ItemShippingStatusList;
import tools.LoggerClient;
import tools.SignUpResult;
import warehouse.WarehouseInterface;

@WebService(endpointInterface = "retailer.RetailerInterface")
public class RetailerImpl implements RetailerInterface {
	
	private CustomerManager customerManager;
	private LoggerClient loggerClient;
	private ArrayList<WarehouseInterface> warehouseList;
	private String name;
	
	/**
	 * Constructor
	 * @param orb
	 * @param loggerClient
	 */
	public RetailerImpl(String name){
		this.name = name;
		warehouseList = new ArrayList<WarehouseInterface>();
		//this.loggerClient = new LoggerClient();
		customerManager = new CustomerManager("customers.xml");
		this.connectWarehouses();
	}

	/**
	 * Provide interface for user to input the warehouses' names for connecting
	 * @param in
	 */
	public void connectWarehouses(){
//		WarehouseInterface wh1 = new WareHouseImpl("wh1");
//
//		WarehouseInterface wh2 = new WareHouseImpl("wh2");
//		warehouseList.add(wh1);
//		warehouseList.add(wh2);
		
		Scanner in = new Scanner(System.in);
		while(true){
			System.out.print("Please input the port number of the warehouse service to establish connection (q to finish):");
			
			String port = in.nextLine();
			if(port.equals("q")){
				break;
			}else{
				String urlStr = "http://localhost:" + port + "/ws/warehouse?wsdl";
				try {
					URL url = new URL(urlStr);
					QName qname = new QName("http://warehouse/", "WarehouseImplService");
					WarehouseInterface warehouse;
					Service service = Service.create(url, qname);
					warehouse = service.getPort(WarehouseInterface.class);
					System.out.println("Obtained a handle on server object: " + warehouse.getName());
					warehouseList.add(warehouse);
				}catch (MalformedURLException e1) {
					e1.printStackTrace();
				}catch (Exception e) {
					//System.out.println("Failed to access the WSDL at:" + urlStr);
					e.printStackTrace();
					return;
				}
			}
		}	
		in.close();
	}

	@Override
	public ItemList getCatalog(int customerReferenceNumber) {
		System.out.println("getCatalog is called...");
		ItemList itemList = new ItemList();
		HashMap<String, Item> itemsMap = new HashMap<String, Item>();

		for(int i = 0; i < warehouseList.size(); i++){
			ItemList itemListFromWarehouse = warehouseList.get(i).getProductsByID("");
			for(Item item: itemListFromWarehouse.innerItemList){
				String key = item.productID;
				Item itemInMap = itemsMap.get(key); 
				if(itemInMap == null){
					itemsMap.put(key, item.clone());
				}else{
					itemInMap.quantity= itemInMap.quantity + item.quantity;
				}
			}
		}

		for(Item item: itemsMap.values()){
			itemList.innerItemList.add(item);
		}
		System.out.println(itemList.toString());
		return itemList;
	}

	@Override
	public ItemShippingStatusList submitOrder(int customerReferenceNumber,
			ItemList itemOrderList) {
		ItemShippingStatusList itemShippingStatusList= new ItemShippingStatusList();
		Customer currentCustomer = customerManager.getCustomerByReferenceNumber(customerReferenceNumber);
		if(currentCustomer == null){
			loggerClient.write(name + ": customer reference number can not be found in customer database.");
			return itemShippingStatusList;
		}
		
		if(itemOrderList == null){
			loggerClient.write(name + ": null order list.");
			return itemShippingStatusList;
		}else if(itemOrderList.innerItemList.isEmpty()){
			loggerClient.write(name + ": empty order list.");
			return itemShippingStatusList;
		}else{
			HashMap<String, ItemShippingStatus> receivedItemShippingStatusMap = new HashMap<String, ItemShippingStatus>();
			HashMap<String, Item> orderMap = new HashMap<String, Item>();
			for(Item item: itemOrderList.innerItemList){
				Item itemImpl = new Item(item);
				System.out.println(itemImpl.toString());
				if(item.quantity > 0){
					Item itemInOrderMap = orderMap.get(item.productID);
					if(itemInOrderMap == null){
						orderMap.put(item.productID, new Item(item));
					}else{
						itemInOrderMap.quantity += item.quantity;
					}
				}
			}
			
			for(WarehouseInterface thisWarehouse: warehouseList){
				int itemRequestFromWarehouseCount = orderMap.size();
				if(itemRequestFromWarehouseCount > 0)
				{
					ItemList itemRequestFromWarehouseList = new ItemList(itemRequestFromWarehouseCount);
					int i = 0;
					for(Item orderItem: orderMap.values()){
						itemRequestFromWarehouseList.innerItemList.set(i, orderItem);
						i++;
					}
					thisWarehouse.registerRetailer(name);
					ItemList itemsGotFromCurrentWarehouse = thisWarehouse.shippingGoods(itemRequestFromWarehouseList, name);
					if(itemsGotFromCurrentWarehouse == null){
						System.out.println("warehouse return null");
					}else if(itemsGotFromCurrentWarehouse.innerItemList.isEmpty()){
						System.out.println("warehouse return empty arrry");
					}else{
						String log = new String();
						for(Item item: itemsGotFromCurrentWarehouse.innerItemList){
							Item itemInReceivedItemShippingStatusMap = receivedItemShippingStatusMap.get(item.productID);
							if(itemInReceivedItemShippingStatusMap == null){
								receivedItemShippingStatusMap.put(item.productID, new ItemShippingStatus(item, true));
							}else{
								itemInReceivedItemShippingStatusMap.quantity += item.quantity;
							}

							Item itemInOrderMap = orderMap.get(item.productID);
							if(itemInOrderMap == null){
								System.out.println("Warehouse side error. never request this item from warehouse, but the warehouse return this item.");
							}else{
								itemInOrderMap.quantity -= item.quantity;
								if(itemInOrderMap.quantity == 0){
									orderMap.remove(item.productID);
								}
							}
						}
					}
				}else{
					break;
				}
			}
			
			ArrayList<ItemShippingStatus> tmpItemShippingStatusList = new ArrayList<ItemShippingStatus>();
			
			for(ItemShippingStatus itemInReceivedItemShippingStatusMap: receivedItemShippingStatusMap.values()){
				tmpItemShippingStatusList.add(itemInReceivedItemShippingStatusMap);
			}
			
			for(Item itemInOrderMap: orderMap.values()){
				tmpItemShippingStatusList.add(new ItemShippingStatus(itemInOrderMap, false));
			}
			
			itemShippingStatusList.setItems(tmpItemShippingStatusList);
			return itemShippingStatusList;
		}
	}

	@Override
	public SignUpResult signUp(String name, String password, String street1,
			String street2, String city, String state, String zip,
			String country) {
		return customerManager.register(name, password, street1, street2, city, state, zip, country);
	}

	@Override
	public Customer signIn(int customerReferenceNumber, String password) {
		return customerManager.find(customerReferenceNumber, password);
	}

	@Override
	public ItemList getProducts(String productID) {
		ItemList allItems = new ItemList();
		HashMap<String, Item> itemsMap = new HashMap<String, Item>();

		for(int i = 0; i < warehouseList.size(); i++){
			ItemList itemListFromWarehouse = warehouseList.get(i).getProductsByID(productID);
			for(Item item: itemListFromWarehouse.innerItemList){
				String key = item.productID;
				Item itemInMap = itemsMap.get(key); 
				if(itemInMap == null){
					itemsMap.put(key, item);// item.clone() changes to item ?
				}else{
					itemInMap.quantity = itemInMap.quantity + item.quantity;
				}
			}
		}

		for(Item item: itemsMap.values()){
			allItems.innerItemList.add(item);
		}
		return allItems;
	}
	
	/**
	 * get a random order for number between 1 to count
	 * @param count
	 * @return int[] which stores the randomized number from 1 to count
	 */
	private int[] getRandOrder(int count){
		int[][] orderContainer = new int[count][2];
		Random randomGenerator = new Random();
		for(int i = 0; i < count; i++){
			orderContainer[i][0] = i;
			orderContainer[i][1] = randomGenerator.nextInt(count * 2);
		}


		for(int i = 0; i < count - 1; i++){
			for(int j = i + 1; j < count; j++){
				if(orderContainer[i][1] > orderContainer[j][1]){
					int tmp = orderContainer[i][1];
					orderContainer[i][1] = orderContainer[j][1];
					orderContainer[j][1] = tmp;

					tmp = orderContainer[i][0];
					orderContainer[i][0] = orderContainer[j][0];
					orderContainer[j][0] = tmp;
				}
			}
		}
		int []order = new int[count];
		for(int i = 0; i < count; i++){
			order[i] = orderContainer[i][0];
		}
		return order;
	}

}
