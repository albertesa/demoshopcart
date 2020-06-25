package albertesa.sample.prj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
	private String usersCollName = "";
	private String connectionStr = "";
	private String jwtSecret;

	@Autowired
	public AppConfig() {
		super();
	}

	public String getJwtSecret() {
		return jwtSecret;
	}

	@Value("#{environment.JWT_SECRET}")
	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
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

	public String getUsersCollName() {
		return usersCollName;
	}

	@Value("${mongo.tables.users}")
	public void setUsersCollName(String usersCollName) {
		this.usersCollName = usersCollName;
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

	@Bean
	public FilterRegistrationBean<CorsFilter> processCorsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:4200");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		CorsFilter filter = new CorsFilter(source);
		final FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(filter);
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
