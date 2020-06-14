package albertesa.sample.prj.repositories;

import static com.mongodb.client.model.Filters.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import albertesa.sample.prj.builders.CartDocumentBuilder;
import albertesa.sample.prj.builders.CartItemDocumentBuilder;
import albertesa.sample.prj.config.MongoDbClientWrapper;
import albertesa.sample.prj.services.CartService;
import albertesa.sample.prj.services.CartService.Cart;
import albertesa.sample.prj.services.CartService.CartItem;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class MongoRepositoryTest {
	@Mock
    private MongoClient mockClient;
    @Mock
    private MongoCollection<Document> mockCollection;
    @Mock
    private MongoDatabase mockDB;
    
    @InjectMocks
    private MongoDbClientWrapper mongoClient;
    
    private MongoRepository mongoRep;
    
    private ObjectMapper objMapper = new ObjectMapper();
    
    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        mongoRep = new MongoRepository(mongoClient, objMapper);
    }
    
    @Test
    public void getCartTest() throws Exception {
    	//JsonNode jNode = objMapper.readTree("{\"id\":\"2518511a-18cf-4a50-9479-8de69c68f714\","
    	//		+ "\"items\":[{\"productId\":\"prod2\",\"productName\":\"kettle\",\"productImg\":\"kettle.jpg\"}]}");
    	
    	final String cartId = "2518511a-18cf-4a50-9479-8de69c68f714";
    	final String productId = "prod1";
    	final String productName = "tomatoes";
    	final String productImg = "tomatoes.jpg";
    	final int numOfItems = 5;
    	
    	Document docCartItem = new CartItemDocumentBuilder(productId, productName, productImg, numOfItems).build();
    	Document docCart = new CartDocumentBuilder(cartId,
    			Arrays.asList(new Document[] {docCartItem})).build();
    	FindIterable<Document> iterable = mock(FindIterable.class);
    	when(mockDB.getCollection(anyString())).thenReturn(mockCollection);
    	when(mockCollection.find(eq("_id", cartId))).thenReturn(iterable);
    	when(iterable.first()).thenReturn(docCart);

    	Optional<JsonNode> optJsonNode = mongoRep.getDocument("coll1", cartId);
    	assertThat(optJsonNode.isPresent()).isTrue();
    	
    	Cart cart = CartService.unmarshallCartJson(optJsonNode.get());
    	assertThat(cart.getId()).isEqualTo(cartId);
    	Collection<CartItem> cartItems = cart.getItems();
    	assertThat(cartItems).isNotNull();
    	assertThat(cartItems.size()).isEqualTo(1);
    	CartItem cartItem = cartItems.iterator().next();
    	assertThat(cartItem.getNumOfItems()).isEqualTo(numOfItems);
    	assertThat(cartItem.getProductId()).isEqualTo(productId);
    	assertThat(cartItem.getProductName()).isEqualTo(productName);
    	assertThat(cartItem.getProductImg()).isEqualTo(productImg);
    }
}
