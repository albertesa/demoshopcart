package albertesa.sample.prj.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;

import albertesa.sample.prj.config.AppConfig;
import albertesa.sample.prj.controllers.CartItemRequestParam;
import albertesa.sample.prj.repositories.IRepository;
import albertesa.sample.prj.util.AssertUtil;
import albertesa.sample.prj.util.UUIDUtil;

@Service
public class CartService {

	private IRepository repo;
	private String tableName;

	@Autowired
	public CartService(IRepository mongoRep, AppConfig appCfg) {
		super();
		this.repo = mongoRep;
		this.tableName = appCfg.getCartCollName();
	}

	public Collection<Cart> getCarts() throws Exception {
		Collection<JsonNode> jNodes = repo.getDocuments(tableName);
		Collection<Cart> carts = new ArrayList<>();
		for (JsonNode n : jNodes) {
			carts.add(unmarshallCartJson(n));
		}
		return carts;
	}

	public Optional<Cart> getCart(String cartId) throws Exception {
		Optional<JsonNode> jNode = repo.getDocument(tableName, cartId);
		if (jNode.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(unmarshallCartJson(jNode.get()));
	}

	public long deleteCart(String cartId) {
		long numOfDeleted = repo.deleteDocument(tableName, cartId, Cart.class);
		return numOfDeleted;
	}

	/**
	 * Cart item with 0 number of items will be removed from the cart
	 * @param cartId
	 * @param newItem
	 * @return new or updated existing cart
	 * @throws Exception 
	 */
	public Cart setCartItem(String cartId, CartItem newItem) throws Exception {
		AssertUtil.assertValidRequestId(cartId);
		Cart cart;
		if ("new".equalsIgnoreCase(cartId)) {
			cartId = UUIDUtil.generateUUID();
		}
		Optional<Cart> existingCartOpt = getCart(cartId);
		if (existingCartOpt.isEmpty()) {
			if (newItem.getNumOfItems() == 0) {
				return new Cart(cartId);
			}
			return generateCart(cartId, newItem);
		}
		cart = existingCartOpt.get();
		return updateExistingCart(cart, newItem);
	}

	private Cart updateExistingCart(Cart cart, CartItem newItem) {
		Cart newCart = generateUpdatedCart(cart, newItem);
		repo.replaceDocument(tableName, newCart.getId(), newCart, Cart.class);
		return newCart;
	}

	private Cart generateCart(String cartId, CartItem newItem) {
		Cart cart;
		cart = new Cart(cartId);
		cart.setCartItem(newItem);
		repo.saveDocument(tableName, cart, Cart.class);
		return cart;
	}

	/**
	 * Generates a new cart with the cart items from the existing <code>cart</code>
	 * and the new cart item set with {@link Cart#setCartItem(CartItem)}.
	 * @param cart existing cart
	 * @param newItem cart item to set
	 * @return a new cart
	 * @see {@link Cart#setCartItem(CartItem)}
	 */
	private Cart generateUpdatedCart(Cart cart, CartItem newItem) {
		Cart newCart = new Cart(cart.getId());
		Collection<CartItem> items = cart.getItems();
		items.forEach(i -> newCart.setCartItem(i));
		newCart.setCartItem(newItem);
		return newCart;
	}

	public Cart addCartItem(String cid, CartItemRequestParam cartItem) throws Exception {
		Cart existingCart = this.setCartItem(
				cid,
				new CartItem(
						cartItem.getProductId(),
						cartItem.getProductName(),
						cartItem.getProductImg(),
						cartItem.getNumOfItems()));
		return existingCart;
	}

	/**
	 * Submit cart item with 0 number of items
	 * @param cid
	 * @param cartItem
	 * @return
	 * @throws Exception 
	 */
	public Cart removeCartItem(String cid, CartItemRequestParam cartItem) throws Exception {
		Cart existingCart = this.setCartItem(
				cid,
				new CartItem(
						cartItem.getProductId(),
						null,
						null,
						0));
		return existingCart;
	}

	public static Cart unmarshallCartJson(JsonNode n) {
		Cart cart = new Cart(n.get("_id").asText());
		JsonNode items = n.get("items");
		if (items.isArray()) {
			items.iterator().forEachRemaining(jn -> {
				cart.setCartItem(new CartItem(
						jn.get("productId").asText(),
						jn.get("productName").asText(),
						jn.get("productImg").asText(),
						jn.get("numOfItems").asInt()));
			});
		}
		return cart;
	}

	public static Cart unmarshallCart(Document doc) {
		Cart cart = new Cart(doc.getString("_id"));
		Object o = doc.get("items");
		if (o instanceof ArrayList) {
			ArrayList<?> items = ArrayList.class.cast(o);
			items.forEach(i -> {
				Document docItem = Document.class.cast(i);
				cart.setCartItem(new CartItem(
						docItem.getString("productId"),
						docItem.getString("productName"),
						docItem.getString("productImg"),
						docItem.getInteger("numOfItems")));
			});
		}
		return cart;
	}

	public static class Cart {
		private String id;
		private Map<String, CartItem> mapItems = new HashMap<>();

		private Cart(String id) {
			super();
			this.id = id;
		}

		public String getId() {
			return id;
		}

		@BsonIgnore
		public int getNumberOfItems() {
			return this.mapItems.values().stream().mapToInt(CartItem::getNumOfItems).sum();
		}

		/**
		 * Sets the cart item <code>newItem</code> into the cart;
		 * if the number of items in the cart item is <code>&lt; 1</code>
		 * the item will be removed from the cart, if it was there.
		 * @param newItem the cart item to set
		 * @return the cart
		 */
		private Cart setCartItem(CartItem newItem) {
			if (newItem.getNumOfItems() < 1) {
				this.mapItems.remove(newItem.getProductId());
				return this;
			}
			this.mapItems.put(newItem.getProductId(), newItem);
			return this;
		}

		/**
		 * Enables immutability of the cart
		 * @return clone of cart items
		 */
		public Collection<CartItem> getItems() {
			Collection<CartItem> cloneItems = mapItems.values().parallelStream().map(i -> new CartItem(i))
					.collect(Collectors.toList());
			return cloneItems;
		}
	}

	public static class CartItem {
		private String productId;
		private String productName;
		private String productImg;
		private int numOfItems;

		private CartItem(String productId, String productName, String productImg, int numOfItems) {
			super();
			this.productId = productId;
			this.productName = productName;
			this.productImg = productImg;
			this.numOfItems = numOfItems;
		}

		/**
		 * Copy constructor to enable immutability of CartItems by copying
		 * 
		 * @param item
		 */
		private CartItem(CartItem item) {
			super();
			this.productId = item.productId;
			this.productName = item.productName;
			this.productImg = item.productImg;
			this.numOfItems = item.numOfItems;
		}

		public String getProductId() {
			return productId;
		}

		public String getProductName() {
			return productName;
		}

		public String getProductImg() {
			return productImg;
		}

		public int getNumOfItems() {
			return numOfItems;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("CartItem [productId=");
			builder.append(productId);
			builder.append(", productName=");
			builder.append(productName);
			builder.append(", productImg=");
			builder.append(productImg);
			builder.append(", numOfItems=");
			builder.append(numOfItems);
			builder.append("]");
			return builder.toString();
		}
	}

}
