package albertesa.sample.prj.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartControllerIntegrationTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CartControllerIntegrationTest.class);

	@LocalServerPort
	private int port;

	private URL base;
	
	private ObjectMapper objMapper= new ObjectMapper();

	@Autowired
	private TestRestTemplate template;

    @BeforeEach
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port);
    }

    @Test
    //@Disabled("temporary")
    public void crudCartTest() throws Exception {
    	// Create cart, add item
    	final String payLoad = "{\"productId\":\"prod2\",\"productName\":\"kettle\",\"productImg\":\"kettle.jpg\",\"numOfItems\":5}";
    	String url = String.format("%s/cart/%s/additem", base.toString(), "new");
    	logger.info("Test URL: {}", url);
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); 
        HttpEntity<String> request = new HttpEntity<>(payLoad, headers);
        ResponseEntity<String> response = this.template.postForEntity(url, request, String.class);
        String respBody = response.getBody();
        logger.info("Test Create Response: {}", respBody);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode jNode = objMapper.readTree(respBody);
        final String cartId = jNode.get("id").asText();
        assertThat(cartId).isNotEqualTo("new");
        // Get cart
        url = String.format("%s/cart/%s", base.toString(), cartId);
    	logger.info("Test Get URL: {}", url);
        response = template.getForEntity(url, String.class);
        respBody = response.getBody();
        logger.info("Test Get Response: {}", respBody);
        jNode = objMapper.readTree(respBody);
        assertThat(jNode.get("id").asText()).isEqualTo(cartId);
        // Delete cart
        url = String.format("%s/cart/%s/deletecart", base.toString(), cartId);
    	logger.info("Test Delete URL: {}", url);
    	headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); 
        request = new HttpEntity<>(headers);
        response = this.template.postForEntity(url, request, String.class);
        respBody = response.getBody();
        logger.info("Test Delete Response: {}", respBody);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        jNode = objMapper.readTree(respBody);
        assertThat(jNode.get("docId").asText()).isEqualTo(cartId);
        assertThat(jNode.get("numOfDeleted").asInt()).isEqualTo(1);
    }

    @Test
    @Disabled("temporary")
    public void getCartTest() throws Exception {
    	final String cartId = "2518511a-18cf-4a50-9479-8de69c68f714";
    	String url = String.format("%s/cart/%s", base.toString(), cartId);
    	logger.info("Test URL: {}", url);
        ResponseEntity<String> response = template.getForEntity(url, String.class);
        final String respBody = response.getBody();
        logger.info("Test Response: {}", respBody);
        JsonNode jNode = objMapper.readTree(respBody);
        assertThat(jNode.get("id").asText()).isEqualTo(cartId);
    }
    
    @Test
    //@Disabled("temporary")
    public void getCartNotFoundTest() throws Exception {
    	final String cartId = "nnnnnnn-nnnn-nnnn-nnnn-nnnnnnnnn";
    	final String errMsg = String.format("Cart not found %s", cartId);
    	String url = String.format("%s/cart/%s", base.toString(), cartId);
    	logger.info("Test URL: {}", url);
        ResponseEntity<String> response = template.getForEntity(url, String.class);
        final String respBody = response.getBody();
        logger.info("Test Response: {}", respBody);
        JsonNode jNode = objMapper.readTree(respBody);
        assertThat(jNode.get("status").asText()).isEqualTo("error");
        assertThat(jNode.get("message").asText()).isEqualTo(errMsg);
    }
}