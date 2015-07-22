package manufacturer;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.jws.WebService;

import org.dom4j.Element;
import org.omg.CORBA.ORB;

import tools.Item;
import tools.LoggerClient;
import tools.Product;
import tools.ProductList;
import tools.XmlFileController;
//Service Implementation
@WebService(endpointInterface = "manufacturer.ManufacturerInterface")

public class ManufacturerImpl implements ManufacturerInterface {
	private static final long serialVersionUID = 1L;
	private String name;
	private HashMap<String, Item> purchaseOrderMap;
	private int orderNum;
	private LoggerClient loggerClient;
	private PurchaseOrderManager purchaseOrderManager;
	Random random ;
	/**
	 * Constructor
	 * @param name
	 * @param loggerClient
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public ManufacturerImpl(String name, LoggerClient loggerClient){
		this.name = name;
		this.loggerClient = loggerClient;
		purchaseOrderMap = new HashMap<String, Item>();
		orderNum = 1000;
		purchaseOrderManager = new PurchaseOrderManager(name);
		random = new Random();
		setProduct(random);
		System.out.println("ManufacturerImplementation constructed:" + name);
	}
	
	/**
	 * Simulate real produce.
	 * @param productName
	 * @param quantity
	 * @return
	 */
	private boolean produce(String productName, int quantity){
		return true;
	}

	/* (non-Javadoc)
	 * @see manufacturer.ManufacturerInterface#processPurchaseOrder(tools.Item)
	 */
	@Override
	public String processPurchaseOrder(Item purchaseItem) {
		System.out.println("processPurchaseOrder is called...");
		if(!purchaseItem.getManufacturerName().equals(name)){
			System.out.println(name + ": Manufacturer name is not equal to current manufacturer name:" + purchaseItem.getManufacturerName());
			loggerClient.write(name + ": Manufacturer name is not equal to current manufacturer name:" + purchaseItem.getManufacturerName());
			return null;
		}		
		Item availableItem = purchaseOrderManager.itemsMap.get(purchaseItem.getProductType());
		if(availableItem == null){
			loggerClient.write(name + ": " + purchaseItem.getProductType() + " is not supported!");
			return null;
		}else{
			if(purchaseItem.getUnitPrice() < availableItem.getUnitPrice()){
				loggerClient.write(name + ": The order price (" + purchaseItem.getUnitPrice() + ") is lower than defined price(" + availableItem.getUnitPrice() + ")");
				return null;
			}else{
				if(purchaseItem.quantity >= availableItem.quantity){
					int oneTimeQuantity = 100;
					if(produce(purchaseItem.getProductType(), oneTimeQuantity)){
						availableItem.quantity =availableItem.quantity + oneTimeQuantity;
						
						purchaseOrderManager.saveItems();
						
						loggerClient.write(name + ": Produced " + oneTimeQuantity + " " + purchaseItem.getProductType());
					}else{
						loggerClient.write(name + ": Failed to produce:" + oneTimeQuantity);
						return null;
					}
				}
				
				if(purchaseItem.quantity >= availableItem.quantity){
					return null;
				}else{
					String orderNumString = new Integer(orderNum++).toString();
					purchaseOrderMap.put(orderNumString, purchaseItem);
					loggerClient.write(name + ": Send order number (" + orderNumString + ") to warehouse.");
					return orderNumString;
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see manufacturer.ManufacturerInterface#getProductInfo(java.lang.String)
	 */
	@Override
	public Product getProductInfo(String productType){
		Item avaiableItem = purchaseOrderManager.itemsMap.get(productType);
		if(avaiableItem == null){
			loggerClient.write(name + ": " + productType + " does not exist in this manufacturer!");
			return null;
		}else{
			return new Product(avaiableItem.getManufacturerName(),avaiableItem.getProductType(), avaiableItem.getUnitPrice());
		}
	}
	/* (non-Javadoc)
	 * @see manufacturer.ManufacturerInterface#receivePayment(java.lang.String, float)
	 */
	@Override
	public boolean receivePayment(String orderNum, float totalPrice){
		Item waitingForPayItem = purchaseOrderMap.get(orderNum);
		if(waitingForPayItem == null){
			loggerClient.write(name + ": " + orderNum + " does not exist in purchaseOrderMap of current manufacturer!");
			return false;
		}else{
			if(waitingForPayItem.quantity * waitingForPayItem.getUnitPrice() == totalPrice){
				Item inhandItem = purchaseOrderManager.itemsMap.get(waitingForPayItem.getProductType());
				inhandItem.quantity = inhandItem.quantity - waitingForPayItem.quantity;
				purchaseOrderManager.saveItems();
				loggerClient.write(name + ": received pament. OrderNum:" + orderNum + ", totalPrice:" + totalPrice);
				purchaseOrderMap.remove(orderNum);
				return true;
			}else{
				loggerClient.write(name + ": the total price does not match for this order number: " + orderNum);
				return false;
			}
		}
	}
	
	/**
	 * @return current manufacturer's name
	 */
	public String getName(){
		return name;
	}

	/* (non-Javadoc)
	 * @see manufacturer.ManufacturerInterface#getProductList()
	 */
	@Override
	public ArrayList<Product> getProductList(){
		ArrayList<Product> productList = new ArrayList<Product>();
		for(Item item: purchaseOrderManager.itemsMap.values()){
			productList.add(item.cloneProduct());
		}
		return productList;
	}
	
	/**
	 * Read product information from configure(xml) file and put them into a items map 
	 * @param randm 
	 */
	private void setProduct(Random randm){
		XmlFileController productInfofile = new XmlFileController(name + ".xml");
		Element root = productInfofile.Read();
		if(root == null){
			XmlFileController xmlfile = new XmlFileController("settings/product_info.xml");
			Element root2 = xmlfile.Read();
			
			if(root2 == null){
				System.out.println("Failed to read settings/product_info.xml");
			}else{
				List<Element> nodes = root2.elements("product");
				for(Element subElem: nodes){
					String manufacturerName = name;
					String productType = subElem.element("productType").getText();
					float unitPrice = randm.nextInt(300 - 200 + 1) + 200;
					int quantity = randm.nextInt(150 - 10 + 1) + 10;
					Item item = new Item(manufacturerName, productType, unitPrice, quantity);
					System.out.println(item.toString() + " is added from : product_info.xml");
					if(purchaseOrderManager.itemsMap.get(productType) == null){
						purchaseOrderManager.itemsMap.put(productType, item);
					}
				}
				purchaseOrderManager.saveItems();
			}
		}else{
			List<Element> nodes = root.elements("product");
			boolean newProductAdded = false;
			for(Element subElem: nodes){
				String manufacturerName = name;
				String productType = subElem.element("productType").getText();
				float unitPrice = randm.nextInt(300 - 200 + 1) + 200;
				if(purchaseOrderManager.itemsMap.get(productType) == null){
					purchaseOrderManager.itemsMap.put(productType, new Item(manufacturerName, productType, unitPrice, randm.nextInt(150 - 10 + 1) + 10));
					newProductAdded = true;
				}
			}
			if(newProductAdded){
				purchaseOrderManager.saveItems();
			}
		}
	}
}
