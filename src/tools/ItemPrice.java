package tools;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ItemPrice {


    private  String AWS_ACCESS_KEY_ID = "AKIAJNJKW4RGMLFQZVPA";
    private  String AWS_SECRET_KEY = "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";
    private  String ENDPOINT = "ecs.amazonaws.ca";
    private  String ITEM_ID ;
    private  String reqUrl ;
    ItemRequestHelper helper;
    
    public ItemPrice(String itemID)
    {
    	this.ITEM_ID = itemID;
    	helper = new ItemRequestHelper();
        try {
            helper = ItemRequestHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("AssociateTag","aaaasbbascc");
        params.put("Operation", "ItemLookup");
        params.put("ItemId", ITEM_ID);
        params.put("ResponseGroup", "Offers");

        this.reqUrl = helper.sign(params);
    }


    public float fetchPrice() throws Exception {
            String price = null;        
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(this.reqUrl);
            Node titleNode = doc.getElementsByTagName("Price").item(0);
            price = titleNode.getTextContent();  
            String[] priceVal = price.split(" ");
            return Float.valueOf(priceVal[1].trim()).floatValue();
    }

}

