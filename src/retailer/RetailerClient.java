package retailer;

import java.net.URL;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import tools.Item;

public class RetailerClient {

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		System.out.println("Please input the port number of the Retailer service:");
		String port = in.nextLine();
		String urlStr = "http://localhost:" + port + "/ws/retailer?wsdl";
		URL url = new URL(urlStr);
		QName qname = new QName("http://retailer/", "RetailerImplService");

		RetailerInterface retailer;
		try{
			Service service = Service.create(url, qname);
			retailer = service.getPort(RetailerInterface .class);
		}catch (Exception e) {
			e.printStackTrace();

			System.out.println("Failed to access the WSDL at:" + urlStr);
			return;
		}
		
		System.out.println(retailer.getCatalog(0).innerItemList);
		System.out.println(retailer.getProducts("").innerItemList);
		System.out.println(retailer.signIn(0,"213"));
		System.out.println(retailer.signUp("name1", "password1", "street11", "street21", "city1", "state1", "zip1", "country1"));
		//System.out.println(retailer.submitOrder(0, null));
		
	}
}