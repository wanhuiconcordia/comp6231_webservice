package tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * @author comp6231.team5
 *
 */
public class CustomerManager {

	private ArrayList<Customer> customers;

	public CustomerManager(){
		customers = new ArrayList<Customer>();
		loadCustomers();
	}

	/**
	 *  save all the customers to xml file
	 */
	public void saveCustomers()
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream("customers.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(customers);
			out.close();
			fileOut.close();

		}catch(IOException i){
			i.printStackTrace();
		}
	}

	/**
	 * load all customers from xml file.
	 */
	public void loadCustomers()
	{
		try
		{
			FileInputStream fileIn = new FileInputStream("customers.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			customers = (ArrayList<Customer>) in.readObject();
			in.close();
			fileIn.close();
		}catch(IOException i){
//			i.printStackTrace();
			return;
		}catch(ClassNotFoundException c){
			c.printStackTrace();

			return;
		}
	}

	/**
	 * register a customer according the info provided
	 * @param name
	 * @param password
	 * @param street1
	 * @param street2
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 * @return an object of SignUpResult
	 */
	public synchronized SignUpResult register(String name, String password, String street1, String street2, String city, String state, String zip, String country){
		for(Customer customer: customers){
			if(customer.getName().equals(name) && customer.getPassword().equals(password)){
				return new SignUpResult(false, -1, "Failed to sign up! (User name exists, try another name)");
			}
		}
		int customerReferenceNumber = 1000 + customers.size();
		customers.add(new Customer(customerReferenceNumber, name, password, street1, street2, city, state, zip, country));
		System.out.println("Will save this user:");
		saveCustomers();
		return new SignUpResult(true, customerReferenceNumber , "Sign up successfully.");
	}

	public synchronized Customer find(int customerReferenceNumber, String password){
		for(Customer customer: customers){
			if(customer.getCustomerReferenceNumber() == customerReferenceNumber
					&& customer.getPassword().equals(password)){
				return customer;
			}
		}
		return null;
	}
	
	/**
	 * find a customer in customer list by the reference number
	 * @param customerReferenceNumber
	 * @return ture if find, false if not
	 */
	public synchronized boolean find(int customerReferenceNumber){
		for(Customer customer: customers){
			if(customer.getCustomerReferenceNumber() == customerReferenceNumber){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * get a customer by reference number
	 * @param customerReferenceNumber
	 * @return the customer
	 */
	public synchronized Customer getCustomerByReferenceNumber(int customerReferenceNumber){
		for(Customer customer: customers){
			if(customer.getCustomerReferenceNumber() == customerReferenceNumber){
				return customer;
			}
		}
		return null;
	}
}
