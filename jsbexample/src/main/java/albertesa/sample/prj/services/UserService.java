package albertesa.sample.prj.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.eventbus.EventBus;

import albertesa.sample.prj.config.AppConfig;
import albertesa.sample.prj.repositories.IRepository;
import albertesa.sample.prj.services.eventbus.CreateCart;
import albertesa.sample.prj.util.UUIDUtil;

@Service
public class UserService {
	
	private String tableName;
	private IRepository repo;
	private ICacheService cacheSvc;
	private ObjectMapper mapper;
	private EventBus eventBus;

	@Autowired
	private UserService(IRepository mongoRepo,
			AppConfig appCfg,
			ICacheService cacheSvc,
			ObjectMapper mapper,
			EventBus eventBus) {
		super();
		this.tableName = appCfg.getUsersCollName();
		this.repo = mongoRepo;
		this.cacheSvc = cacheSvc;
		this.mapper = mapper;
		this.eventBus = eventBus;
		this.eventBus.register(this);
	}

	/**
	 * Retrieve cached user by <code>userName</code> from cache or DB.
	 * @param userName user name
	 * @return optional of encrypted password or empty
	 * @throws Exception if exception is thrown during cache or DB retrieval
	 */
	private Optional<User> getCachedUser(String userName) throws Exception {
		Optional<String> optPwd = cacheSvc.getValue(userName);
		if (optPwd.isPresent()) {
			return Optional.of(unmarshall(optPwd.get()));
		}
		return Optional.empty();
	}
	
	/**
	 * Get user from cache or DB
	 * @param email email of the user to retrieve
	 * @return Optional with user or empty
	 * @throws Exception if an exception occurs during retrieval from cache or DB
	 */
	public Optional<User> getUser(String email) throws Exception {
		Optional<User> optUsr = getCachedUser(email);
		if (optUsr.isPresent()) {
			return optUsr;
		}
		Optional<JsonNode> optJn = repo.getDocument(tableName, email);
		if (optJn.isPresent()) {
			User user = unmarshall(optJn.get());
			setCachedUser(user);
			return Optional.of(user);
		}
		return Optional.empty();
	}

	private void setCachedUser(User user) throws JsonProcessingException {
		String valToCache = mapper.writeValueAsString(user);
		cacheSvc.setValue(user.getId(), valToCache);
	}

	private User unmarshall(String val) throws JsonMappingException, JsonProcessingException {
	    JsonNode n = mapper.readTree(val);
	    return unmarshall(n);
	}

	private static User unmarshall(JsonNode n) {
		String idKey = "_id";
		if (n.get(idKey) == null) {
			idKey = "id";
		}
		String uname = n.get(idKey).asText();
		String pwdEncoded = n.get("password").asText();
		String cartId = n.get("cartId").asText();
		JsonNode prefs = n.get("preferences");
		Map<String, String> prefsMap = new HashMap<>();
		if (prefs.isObject()) {
			ObjectNode on = ObjectNode.class.cast(prefs);
			Iterator<Entry<String, JsonNode>> fields = on.fields();
			while(fields.hasNext()) {
				Entry<String, JsonNode> jnNextEntry = fields.next();
				prefsMap.put(jnNextEntry.getKey(), jnNextEntry.getValue().asText());
			}
		}
		return new User(uname, pwdEncoded, cartId, prefsMap);
	}

	public User addUser(String email, String encryptedPwd) throws JsonProcessingException {
		String cartId = UUIDUtil.generateUUID();
		User user = new User(email, encryptedPwd, cartId);
		user.setPreference("pref1", "val1");
		repo.saveDocument(tableName, user, User.class);
		eventBus.post(new CreateCart(email, cartId));
		setCachedUser(user);
		return user;
	}

	public static class User {
		private String id;
		private Map<String, String> preferences = new HashMap<>();
		private String password;
		private String cartId;
		
		private User(String id, String pwd, String cartId) {
			super();
			this.id = id;
			this.password = pwd;
			this.cartId = cartId;
		}
		
		private User(String id, String pwd, String cartId, Map<String, String> preferences) {
			this(id, pwd, cartId);
			this.preferences = preferences;
		}
		
		/**
		 * Sets a preference.
		 * @param prefName name of the preference
		 * @param prefValue value for the preference
		 * @return previous value if was previously set
		 */
		private Optional<String> setPreference(String prefName, String prefValue) {
			return Optional.ofNullable(preferences.put(prefName, prefValue));
		}

		public String getId() {
			return id;
		}

		public Map<String, String> getPreferences() {
			return preferences;
		}

		public String getPassword() {
			return password;
		}
		
		public String getCartId() {
			return this.cartId;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("User [id=");
			builder.append(id);
			builder.append(", preferences=");
			builder.append(preferences);
			builder.append("]");
			return builder.toString();
		}
		
		
	}
}
