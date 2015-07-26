package client;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import tools.*;
import retailer.*;

/**
 * @author comp6231.team5
 * Client side application
 * Functionalities: 
 * #sign in
 * #sign up
 * #sign out
 * #get item information
 * #make orders
 */
public class Client
{
	private RetailerInterface retailer;	
	private Scanner in;
	private Customer currentCustomer;
	private LoggerClient loggerClient;
	private String name;
	private ArrayList<Item> retailerItemCatalog;


	
	/**
	 * Constructor
	 */
	public Client(){
		name = "client";
		in = new Scanner(System.in);
		loggerClient = new LoggerClient();
		retailerItemCatalog = new ArrayList<Item>();
	}
	
	/**
	 * Provide a interface for user to input the retailer's port for connecting
	 * @return
	 */
	public boolean connectRetailer(){

		System.out.println("Please input the port number of the retailer service:");
		String port = in.nextLine();
		String urlStr = "http://localhost:" + port + "/ws/retailer?wsdl";
		
		try {
			URL url = new URL(urlStr);
			QName qname = new QName("http://retailer/", "RetailerImplService");
			Service service = Service.create(url, qname);
			retailer = service.getPort(RetailerInterface.class);
			return true;
		} catch (MalformedURLException e1) {
			System.out.println("Failed to access the WSDL at:" + urlStr);
			//e1.printStackTrace();
			return false;
		}
	}

	/**
	 * Sign up
	 * @return sign up result
	 */
	public boolean customerSignUp(){
		if(currentCustomer != null){
			customerSignOut();
		}
		System.out.println("Input customer name:");
		String name = in.next();
		System.out.println("Input customer password:");
		String password = in.next();
		System.out.println("Input customer street1:");
		String street1 = in.next();
		System.out.println("Input customer street2:");
		String street2 = in.next();
		System.out.println("Input customer city:");
		String city = in.next();
		System.out.println("Input customer state:");
		String state = in.next();
		System.out.println("Input customer zip code:");
		String zip = in.next();
		System.out.println("Input customer country:");
		String country = in.next();
		try {
			loggerClient.write(this.name + ": Tries to sign up with:" + name + ", " + password
					+ ", " + street1
					+ ", " + street2
					+ ", " + city
					+ ", " + state
					+ ", " + zip
					+ ", " + country);
			SignUpResult signUpResult  = retailer.signUp(name, password, street1, street2, city, state, zip, country);
			if(signUpResult == null){
				loggerClient.write(this.name + ": The retailer returned null. Failed to signup.");
				System.out.println("The retailer returned null. Failed to signup.");
				return false;
			}else{
				if(signUpResult.result){
					currentCustomer = new Customer(signUpResult.customerReferenceNumber, name, password, street1, street2, city, state, zip, country);
					System.out.println("Your creferenceNumber is:" + signUpResult.customerReferenceNumber);
					loggerClient.write(this.name + ": Sign successfully. The retailer returned:" + signUpResult.message);
				}else{
					System.out.println("Failed to sign up. The retailer returned:" + signUpResult.message);
					loggerClient.write(this.name + ": Failed to sign up. The retailer returned:" + signUpResult.message);
				}
				return signUpResult.result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Sign in
	 * @return sign in result
	 */
	public boolean customerSignIn(){
		if(currentCustomer != null){
			customerSignOut();
		}
		try{
			System.out.println("Input customer ReferenceNumber:");
			int customerReferenceNumber = Integer.parseInt(in.next());
			System.out.println("Input customer password:");
			String password = in.next();

			loggerClient.write(name + ": Tries to sign in with reference number:" + customerReferenceNumber + " and password:" + password);
			currentCustomer = retailer.signIn(customerReferenceNumber, password);
			if(currentCustomer == null){
				loggerClient.write(name + ": The retailer returned null. Failed to sign in. Please try again.");
				System.out.println("The retailer returned null. Failed to sign in. Please try again.");
				return false;
			}else{
				System.out.println("Signed in properly. Your person informations are:" + currentCustomer.toString());
				loggerClient.write(name + ": Signed in properly. The customer info:" + currentCustomer.toString());
				return true;
			}
		} catch (NumberFormatException e){
			System.out.println("ReferenceNumber should contains digits only. Please try again.");
			return false;
		}
	}

	/**
	 * Sing out
	 */
	public void customerSignOut(){
		if(currentCustomer == null)
			return;
		loggerClient.write(name + ": Current customer:" + currentCustomer.name + " signed out.");
		currentCustomer = null;
	}

	/**
	 * get retailer's item catalog
	 * @return available item list
	 */
	public void getCatalog(){
		if(currentCustomer == null){
			System.out.println("This operation is only allowed for registed user. Please sign in or sign up.");
		}else{
			ItemList itemList = retailer.getCatalog(currentCustomer.customerReferenceNumber);
			retailerItemCatalog.clear();
			if(itemList == null){
				System.out.println("Retailer return null");
			}else {
				System.out.println("Retailer item catalog:");
				System.out.println(itemList.toString());
				retailerItemCatalog = itemList.innerItemList;
			}
		}
	}

	/**
	 * make order
	 * @return the items shipping status 
	 */
	public void makeOrder(){
		if(currentCustomer == null){
			System.out.println("This operation is only allowed for registed user. Please sign in or sign up.");
		}else{
			
			if(retailerItemCatalog.isEmpty()){
				getCatalog();
			}
			
			String inputString = new String();
			ArrayList<Item> itemOrderList = new ArrayList<Item>();
			String log = new String();
			for(Item item: retailerItemCatalog){
				System.out.print("Input quantity for :" + item.toString() + " (q for finishing order list):");
				inputString = in.next();
				if(inputString.equals("q")){
					break;
				}else{
					try{
						int quantiy = Integer.parseInt(inputString);
						Item tmpItem = item.clone();
						tmpItem.quantity = quantiy;
						itemOrderList.add(tmpItem);
						log = log + tmpItem.toString() + "</br>";
					}catch(NumberFormatException e){
						System.out.println("Please input a number or 'q'.");
					}
				}
			}
			if(itemOrderList.size() > 0){
				loggerClient.write(name + ": Tries to order:");
				loggerClient.write(log);
				
				ItemList itemList = new ItemList();
				itemList.setItems(itemOrderList);
				ItemShippingStatusList itemShippingStatusList = retailer.submitOrder(currentCustomer.customerReferenceNumber, itemList);
				if(itemShippingStatusList == null){
					System.out.println("submitOrder return null.");
				}else{
					System.out.println("Return itemShippingStatus:");
					System.out.println(itemShippingStatusList.toString());
				}
			}
		}
	}

	public static void main(String args[])
	{
		Client client = new Client();
		
		if(!client.connectRetailer()){
			return;
		}
		String operation;
		do{
			System.out.println("Type [1] to sign up.");
			System.out.println("Type [2] to sign in.");
			System.out.println("Type [3] to sign out.");
			System.out.println("Type [4] to get product catalog.");
			System.out.println("Type [5] to make an order.");
			System.out.println("Type [6] to quit.");
			operation = client.in.next();
			if(operation.compareTo("1") == 0){
				client.customerSignUp();
			}else if(operation.compareTo("2") == 0){
				client.customerSignIn();
			}else if(operation.compareTo("3") == 0){
				client.customerSignOut();
			}else if(operation.compareTo("4") == 0){
				client.getCatalog();
			}else if(operation.compareTo("5") == 0){
				client.makeOrder();
			}else if(operation.compareTo("6") == 0){
				break;
			}else{
				System.out.println("Wrong input. Try again.");
			}
		}while(true);

		client.in.close();
	}
}
