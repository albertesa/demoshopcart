package albertesa.sample.prj.controllers;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import albertesa.sample.prj.services.CartService;
import albertesa.sample.prj.services.CartService.Cart;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cart")
@Tag(name = "CartController", description = "Cart CRUD API")
//@CrossOrigin(origins = "http://localhost:4200")
public class CartController {
	
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);
	
	private CartService cartService;

	@Autowired
	public CartController(CartService cartService) {
		super();
		this.cartService = cartService;
		logger.info("CartContoller created");
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Collection<Cart>> getCarts() throws Exception {
		Collection<Cart> carts = cartService.getCarts();
		return new ResponseEntity<Collection<Cart>>(carts, HttpStatus.OK);
	}

	@Operation(summary = "Get Cart by Cart ID", description = "Get Cart by Cart ID <code>cartId</code>", tags = { "CartController" })
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "successful operation",
	                content = @Content(schema = @Schema(implementation = Cart.class))),
	        @ApiResponse(responseCode = "404", description = "JSON object",
            content = @Content(schema = @Schema(implementation = String.class),
            		examples = @ExampleObject(
            			      name = "example",
            			      value = "{\"status\":\"error\",\"message\":\"Cart not found xxxx-xxxx-xxxx-xxxx\"}")))})
	@RequestMapping(value = "/{cartId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Cart> getCart(@PathVariable String cartId) throws Exception {
		Optional<Cart> cartOpt = cartService.getCart(cartId);
		Cart cart = cartOpt.orElseThrow(
				() -> new NoSuchElementException(String.format("Cart not found %s", cartId)));
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@Operation(summary = "Add CartItem to Cart", description = "Update Cart by %cartId%", tags = { "CartController" })
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "successful operation",
	                content = @Content(schema = @Schema(implementation = Cart.class)))})
	@RequestMapping(value = "/{cartId}/additem", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Cart> addItem(
    		@Parameter(description="Cart ID to create/update. Specify Cart ID \"new\" to generate a new Cart.", required=true)
    		@PathVariable String cartId,
    		@Parameter(description="CartItem to add/update. CartItem will be updated by Product ID. Cannot be null or empty.", 
            required=true, schema=@Schema(implementation = Void.class))
    		@Valid @RequestBody CartItemRequestParam cartItem,
    		HttpServletRequest request,
    		HttpServletResponse response) throws Exception {
		final String requestUserHeader = request.getHeader("Authorization-User");
		Cart cart = cartService.addCartItem(cartId, cartItem, requestUserHeader);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}

	@RequestMapping(value = "/{cartId}/removeitem", method = RequestMethod.POST,
			produces = "application/json")
    public ResponseEntity<Cart> removeItem(
    		@PathVariable String cartId,
    		@RequestBody CartItemRequestParam rmItem,
    		HttpServletRequest request,
    		HttpServletResponse response) throws Exception {
		final String requestUserHeader = request.getHeader("Authorization-User");
		Cart cart = cartService.removeCartItem(requestUserHeader, cartId, rmItem);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}

	@RequestMapping(value = "/{cartId}/resetcart", method = RequestMethod.POST,
			produces = "application/json")
    public ResponseEntity<UpdateDocResponse> resetCart(
    		@PathVariable String cartId,
    		HttpServletRequest request,
    		HttpServletResponse response) throws JsonProcessingException {
		final String requestUserHeader = request.getHeader("Authorization-User");
		long numOfDeleted = cartService.resetCart(requestUserHeader, cartId);
		return new ResponseEntity<UpdateDocResponse>(new UpdateDocResponse(cartId, numOfDeleted), HttpStatus.OK);
	}
    
}