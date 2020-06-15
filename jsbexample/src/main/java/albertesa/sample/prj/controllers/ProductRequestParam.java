package albertesa.sample.prj.controllers;

import albertesa.sample.prj.util.SanitizerUtil;

public class ProductRequestParam {
	
	private String id;
	private String productName;
	private String productImg;
	private String productDesc;
	
	public ProductRequestParam() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String productId) {
		this.id = SanitizerUtil.sanitize(productId);
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

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = SanitizerUtil.sanitize(productDesc);
	}

}
