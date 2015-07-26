package warehouse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import tools.Item;
import tools.ItemList;
import tools.LoggerClient;

/**
 * @author comp6231.team5
 * Provide full functionality testing to warehouse
 */
public class WarehouseClient {

	private WarehouseInterface warehouse;
	private Scanner in;
	private LoggerClient loggerClient;
	private String name;


	/**
	 * Constructor
	 */
	public WarehouseClient() {
		name = "warehouseClient";
		in = new Scanner(System.in);
		loggerClient = new LoggerClient();
	}

	/**
	 * Provide interface for user to input warehouses' names for connecting
	 * @return
	 */
	public boolean connectWarehouse(){
		System.out.println("Please input the port number of the warehouse service:");
		String port = in.nextLine();
		String urlStr = "http://localhost:" + port + "/ws/warehouse?wsdl";
		
		try {
			URL url = new URL(urlStr);
			QName qname = new QName("http://warehouse/", "WarehouseImplService");
			Service service = Service.create(url, qname);
			warehouse = service.getPort(WarehouseInterface.class);
			return true;
		} catch (MalformedURLException e1) {
			System.out.println("Failed to access the WSDL at:" + urlStr);
			//e1.printStackTrace();
			return false;
		}
	}

	/**
	 * test getProductsById(String)
	 */
	private void testGetProductsById(){
		System.out.println("Please input product ID:");
		try{
			String productID = in.nextLine();
			ItemList itemList = warehouse.getProductsByID(productID);
			System.out.println(itemList.toString());
		}catch(Exception e){
			System.out.println("Input a integer for product id.");
		}
	}		

	/**
	 * test getProductsByType(String)
	 */
	private void testGetProductsByType(){
		System.out.println("Please input product type:");
		String productType = in.nextLine();
		System.out.println("productType:" + productType);
		ItemList itemList = warehouse.getProductsByType(productType);
		
		System.out.println(itemList.toString());
	}

	
	/**
	 * test getProductsByManufactureId(String)
	 */
	private void testGetProductsByManufactureId(){
		System.out.println("Please input manufacture ID:");
		String manufactureID = in.nextLine();
		ItemList itemList = warehouse.getProductsByRegisteredManufacturers(manufactureID);
		
		System.out.println(itemList.toString());
	}

	/**
	 * test getProductsPerManufacturer(String, String)
	 */
	private void testGetProductsPerManufacturer(){
		try{
			System.out.println("Please input manufacturer ID:");
			String manufacturerID = in.nextLine();
			System.out.println("Please input product ID:");
			String productID = in.nextLine();
			
			ItemList itemList = warehouse.getProducts(productID, manufacturerID);
			System.out.println(itemList.toString());
		}catch(Exception e){
			System.out.println("Input a integer for product id.");
		}
	}

	/**
	 * test registeringRetailer(String)
	 */
	private void testRegisteringRetailer(){
		System.out.println("Will register:" + name);
		if(warehouse.registerRetailer(name)){
			System.out.println("Registering is done Successfully.");
		}else{
			System.out.println("Failed to do registration.");
		}
	}

	
	/**
	 * test unregisteringRetailer(String) 
	 */
	private void testUnregisteringRetailer(){
		System.out.println("Will unregister:" + name);
		if(warehouse.unregisterRegailer(name)){
			System.out.println("UnRegistering is done Successfully.");
		}else{
			System.out.println("Failed to do unRegistration.");
		}
	}

	/**
	 * test Item[] itemArray = warehouse.shippingGoods(Item[])
	 */
	private void testShippingGoods(){
		ItemList itemList1 = warehouse.getProductsByID("");
		ItemList itemArray = warehouse.shippingGoods(itemList1, name);
		System.out.println(itemArray.toString());
	}

	
	public static void main(String[] args) {
		WarehouseClient warehouseClient = new WarehouseClient();
	
		if(!warehouseClient.connectWarehouse()){
			return;
		}

		String cmd = new String(); 
		while(true){
			System.out.println("Please select the option:");
			System.out.println("\t1 for getting product by id.");
			System.out.println("\t2 for getting product by product type.");
			System.out.println("\t3 for getting product by manufacture id.");
			System.out.println("\t4 for getting product by product type per manufacturer id.");
			System.out.println("\t5 for registering retailer.");
			System.out.println("\t6 for unregistering retailer.");
			System.out.println("\t7 for shipping goods.");
			System.out.println("\t8 for shutting down warehouse.");

			System.out.println("\tq for QUIT.");

			cmd = warehouseClient.in.nextLine();
			if(cmd.equals("1")){
				warehouseClient.testGetProductsById();
			}else if(cmd.equals("2")){
				warehouseClient.testGetProductsByType();
			}else if(cmd.equals("3")){
				warehouseClient.testGetProductsByManufactureId();
			}else if(cmd.equals("4")){
				warehouseClient.testGetProductsPerManufacturer();
			}else if(cmd.equals("5")){
				warehouseClient.testRegisteringRetailer();
			}else if(cmd.equals("6")){
				warehouseClient.testUnregisteringRetailer();
			}else if(cmd.equals("7")){
				warehouseClient.testShippingGoods();
			}else if(cmd.equals("q")){
				break;
			}else{
				System.out.println("Wrong input. Please try again.");
			}
		}
	}

}


