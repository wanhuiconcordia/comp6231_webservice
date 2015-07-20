package warehouse;

import javax.jws.WebService;

import tools.ItemList;
//Service Implementation
@WebService(endpointInterface = "warehouse.WarehouseInterface")
public class WareHouseImpl implements WarehouseInterface {

	@Override
	public ItemList getProductsByID(String productID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemList getProductsByType(String productType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemList getProductsByRegisteredManufacturers(String manufacturerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemList getProducts(String productID, String manufacturerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean registerRetailer(String retailerName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unregisterRegailer(String retailerName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemList shippingGoods(ItemList itemList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
