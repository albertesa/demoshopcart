package albertesa.sample.prj.controllers;

import albertesa.sample.prj.util.SanitizerUtil;

public class CartItemRequestParam {
	
	private String productId;
	private String productName;
	private String productImg;
	private int numOfItems;
	
	public CartItemRequestParam() {
		super();
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = SanitizerUtil.sanitize(productId);
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = SanitizerUtil.sanitize(productName);
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = SanitizerUtil.sanitize(productImg);
	}

	public int getNumOfItems() {
		return numOfItems;
	}

	public void setNumOfItems(int numOfItems) {
		this.numOfItems = numOfItems;
	}

}
