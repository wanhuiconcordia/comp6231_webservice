package manufacturer;

import java.net.URL;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import tools.Item;

public class ManufacturerClient {

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		System.out.println("Please input the port number of the manufacturer service:");
		String port = in.nextLine();
		String urlStr = "http://localhost:" + port + "/ws/manufacturer?wsdl";
		URL url = new URL(urlStr);
		QName qname = new QName("http://manufacturer/", "ManufacturerImplService");

		ManufacturerInterface manufacturer;
		try{
			Service service = Service.create(url, qname);
			manufacturer = service.getPort(ManufacturerInterface .class);
		}catch (Exception e) {
			//e.printStackTrace();

			System.out.println("Failed to access the WSDL at:" + urlStr);
			return;
		}
		
		System.out.println(manufacturer.processPurchaseOrder(new Item("Sony", "DVD", 25, 10)));
//		System.out.println(manufacturer.getProductInfo("ddd").toString());
		
		System.out.println(manufacturer.getProductList().toString());
	}
}