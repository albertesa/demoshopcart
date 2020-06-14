package albertesa.sample.prj.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import albertesa.sample.prj.config.AppConfig;
import albertesa.sample.prj.controllers.ProductRequestParam;
import albertesa.sample.prj.repositories.IRepository;
import albertesa.sample.prj.util.AssertUtil;
import albertesa.sample.prj.util.UUIDUtil;

@Service
public class ProductService {

	private IRepository repo;
	private String tableName;

	@Autowired
	public ProductService(IRepository mongoRep, AppConfig appCfg) {
		super();
		this.repo = mongoRep;
		this.tableName = appCfg.getProdCollName();
	}

	public Collection<Product> getProducts() throws Exception {
		Collection<JsonNode> jNodes = repo.getDocuments(tableName);
		Collection<Product> carts = new ArrayList<>();
		for (JsonNode n : jNodes) {
			carts.add(unmarshallProductJson(n));
		}
		return carts;
	}

	public Optional<Product> getProduct(String prodId) throws Exception {
		Optional<JsonNode> jNode = repo.getDocument(tableName, prodId);
		if (jNode.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(unmarshallProductJson(jNode.get()));
	}

	public long deleteProduct(String cartId) {
		long numOfDeleted = repo.deleteDocument(tableName, cartId, Product.class);
		return numOfDeleted;
	}

	public Product setProduct(ProductRequestParam newProd) throws Exception {
		String prodId = newProd.getId();
		AssertUtil.assertValidRequestId(prodId);
		if ("new".equalsIgnoreCase(prodId)) {
			prodId = UUIDUtil.generateUUID();
		}
		Product prodReq = new Product(
				prodId,
				newProd.getProductName(),
				newProd.getProductImg(),
				newProd.getProductDesc());
		Optional<Product> existingProdOpt = getProduct(prodId);
		if (existingProdOpt.isEmpty()) {
			return generateProduct(prodReq);
		}
		return updateExistingProduct(prodReq);
	}

	private Product updateExistingProduct(Product newProd) {
		repo.replaceDocument(tableName, newProd.id, newProd, Product.class);
		return newProd;
	}

	private Product generateProduct(Product newProd) {
		repo.saveDocument(tableName, newProd, Product.class);
		return newProd;
	}

	public static Product unmarshallProductJson(JsonNode jn) {
		Product prod = new Product(
				jn.get("_id").asText(),
				jn.get("productName").asText(),
				jn.get("productImg").asText(),
				jn.get("productDesc").asText());
		return prod;
	}

	public static Product unmarshallProduct(Document doc) {
		Product prod = new Product(
				doc.getString("_id"),
				doc.getString("productName"),
				doc.getString("productImg"),
				doc.getString("productDesc"));
		return prod;
	}

	public static class Product {
		private String id;
		private String productName;
		private String productImg;
		private String productDesc;

		private Product(String productId, String productName, String productImg, String productDesc) {
			super();
			this.id = productId;
			this.productName = productName;
			this.productImg = productImg;
			this.productDesc = productDesc;
		}

		/**
		 * Copy constructor to enable immutability of Products by copying
		 * 
		 * @param prod
		 */
		private Product(Product prod) {
			super();
			this.id = prod.id;
			this.productName = prod.productName;
			this.productImg = prod.productImg;
			this.productDesc = prod.productDesc;
		}

		public String getId() {
			return id;
		}

		public String getProductName() {
			return productName;
		}

		public String getProductImg() {
			return productImg;
		}

		public String getProductDesc() {
			return productDesc;
		}
	}

}
