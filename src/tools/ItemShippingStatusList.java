package tools;

import java.util.ArrayList;

/**
 * @author comp6231.team5
 *
 */
public class ItemShippingStatusList {
	public ArrayList<Item> innerItemShippingStatusList;
	
	/**
	 * Constructor 
	 */
	public ItemShippingStatusList(){
		innerItemShippingStatusList = new ArrayList<Item>();
	}
	
	/**
	 * @param i
	 */
	public ItemShippingStatusList(int i) {
		innerItemShippingStatusList = new ArrayList(i);
	}

	/**
	 * Constructor 
	 * @param Item item
	 */
	public void addItem(Item item){
		if(innerItemShippingStatusList == null){
			innerItemShippingStatusList = new ArrayList<Item>();
		}
		innerItemShippingStatusList.add(item);
	}
	
	/**
	 * @param ArrayList<Item> otherItemShippingStatusList
	 */
	public void setItems(ArrayList<Item> otherItemShippingStatusList){
		innerItemShippingStatusList = otherItemShippingStatusList;
	}
	
	/**
	 * clear all the elements in innerItemShippingStatusList.
	 */
	public void clearItems(){
		innerItemShippingStatusList.clear();
	}
}
