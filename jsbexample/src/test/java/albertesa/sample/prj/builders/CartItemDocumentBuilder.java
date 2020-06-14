package albertesa.sample.prj.builders;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

public class CartItemDocumentBuilder {

	private Map<String, Object> mapCi = new HashMap<>();
	private String productId;
	private String productName;
	private String productImg;
	private Integer numOfItems;
			
	public CartItemDocumentBuilder() {
		super();
	}

	public CartItemDocumentBuilder(String productId, String productName,
			String productImg, Integer numOfItems) {
		this.productId = productId;
		this.productName = productName;
		this.productImg = productImg;
		this.numOfItems = numOfItems;
	}

	public CartItemDocumentBuilder withProductName(String val) {
		productName = val;
		return this;
	}
	
	public CartItemDocumentBuilder withProductImg(String val) {
		productImg = val;
		return this;
	}
	
	public CartItemDocumentBuilder withProductId(String val) {
		productId = val;
		return this;
	}
	
	public CartItemDocumentBuilder withNumOfItems(Integer val) {
		numOfItems = val;
		return this;
	}
	
	public Document build() {
		mapCi.put("productName", productName);
		mapCi.put("productImg", productImg);
		mapCi.put("productId", productId);
		mapCi.put("numOfItems", numOfItems);
		return new Document(mapCi);		
	}
}
