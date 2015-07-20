package manufacturer;

import java.util.Scanner;

import javax.xml.ws.Endpoint;

public class Manufacturer {
	public static void main(String[] args) {
		//Endpoint publisher
		Scanner in = new Scanner(System.in);
		System.out.println("Please input the port number to publish your manufacturer:");
		String port = in.nextLine();
		String url = "http://localhost:" + port + "/ws/manufacturer";
		System.out.println("Please input the manufacturer name:");
		String manufacturerName = in.nextLine();
		Endpoint.publish(url, new ManufacturerImpl(manufacturerName));
		System.out.println(manufacturerName + " is published. The url is:" + url);
	}
}