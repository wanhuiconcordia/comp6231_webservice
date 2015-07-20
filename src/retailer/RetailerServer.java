package retailer;

import java.util.Scanner;

import javax.xml.ws.Endpoint;



import retailer.RetailerImpl;
import tools.LoggerClient;

public class RetailerServer {
	


	public static void main(String []args){
		RetailerImpl retailerImpl;
		String retailerName;
		Scanner in = new Scanner(System.in);;
		String port;
		String url;
		System.out.println("Please input the port number to publish your retailer:");
		port = in.nextLine();
		System.out.print("Input the name of current retailer:");
		retailerName = in.nextLine();
		url = "http://localhost:" + port + "/ws/retailer";
		retailerImpl = new RetailerImpl(retailerName);
		Endpoint.publish(url, retailerImpl);
		System.out.println(retailerName + " is published. The url is:" + url);
		
	}
}
