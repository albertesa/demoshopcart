package albertesa.sample.prj.controllers;

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
		this.id = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

}
