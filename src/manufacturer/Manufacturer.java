package manufacturer;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import javax.xml.ws.Endpoint;

import tools.LoggerClient;

public class Manufacturer {
	
	private static LoggerClient loggerClient;
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		
		loggerClient = new LoggerClient();
		
		//Endpoint publisher
		
		Scanner in = new Scanner(System.in);
		System.out.println("Please input the port number to publish your manufacturer:");
		String port = in.nextLine();
		String url = "http://localhost:" + port + "/ws/manufacturer";
		System.out.println("Please input the manufacturer name:");
		String manufacturerName = in.nextLine();
		Endpoint.publish(url, new ManufacturerImpl(manufacturerName , loggerClient));
		System.out.println(manufacturerName + " is published. The url is:" + url);
	}
}