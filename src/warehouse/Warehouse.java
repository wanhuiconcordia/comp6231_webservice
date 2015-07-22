package warehouse;

import java.rmi.RemoteException;
import java.util.Scanner;

import javax.xml.ws.Endpoint;



public class Warehouse {
	public static void main(String []args) throws RemoteException{
		Scanner in = new Scanner(System.in);
		System.out.println("Please input the port number to publish your Warehouse:");
		String port = in.nextLine();
		String url = "http://localhost:" + port + "/ws/warehouse";
		System.out.println("Please input the warehouse name:");
		String warehouseName = in.nextLine();
		Endpoint.publish(url, new WarehouseImpl(warehouseName));
		System.out.println(warehouseName + " is published. The url is:" + url);
	}
}
