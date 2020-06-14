package albertesa.sample.prj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class AppConfig {

	private String uname = "";
	private String password = "";
	private String host = "";
	private String db = "";
	private String cartCollName = "";
	private String prodCollName = "";
	private String connectionStr = "";
	
	@Autowired
	public AppConfig() {
		super();
	}

	public String getUname() {
		return uname;
	}

	@Value("#{environment.MONGO_USER}")
	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPassword() {
		return password;
	}

	@Value("#{environment.MONGO_PWD}")
	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	@Value("#{environment.MONGO_HOST}")
	public void setHost(String host) {
		this.host = host;
	}

	public String getDb() {
		return db;
	}

	@Value("#{environment.MONGO_DB}")
	public void setDb(String db) {
		this.db = db;
	}

	public String getCartCollName() {
		return cartCollName;
	}

	@Value("${mongo.tables.carts}")
	public void setCartCollName(String cartCollName) {
		this.cartCollName = cartCollName;
	}

	public String getProdCollName() {
		return prodCollName;
	}

	@Value("${mongo.tables.products}")
	public void setProdCollName(String prodCollName) {
		this.prodCollName = prodCollName;
	}

	public String getConnectionStr() {
		return connectionStr;
	}

	@Value("${mongo.conn}")
	public void setConnectionStr(String connectionStr) {
		this.connectionStr = connectionStr;
	}

	public String getResolvedConnectionString() {
		return String.format(connectionStr, uname, password, host, db);
	} 
}
