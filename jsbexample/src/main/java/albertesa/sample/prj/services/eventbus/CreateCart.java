package albertesa.sample.prj.services.eventbus;

public class CreateCart {
	private String cartId;
	private String userId;

	public CreateCart(String userId, String cartId) {
		super();
		this.cartId = cartId;
		this.userId = userId;
	}

	public String getCartId() {
		return cartId;
	}

	public String getUserId() {
		return userId;
	}
	
	
}
