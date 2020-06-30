package albertesa.sample.prj.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class MongoDbClientWrapper {
	
	private static final Logger logger = LoggerFactory.getLogger(MongoDbClientWrapper.class);

	private MongoClientURI uri;
	private MongoClient mongoClient;
	private MongoDatabase database;
	private AppConfig appCfg;
	
	@Autowired
	public MongoDbClientWrapper(AppConfig appCfg) {
		this.appCfg = appCfg;
	}
	
	@PostConstruct
    private void postConstruct() {
		try {
			String connStr = appCfg.getResolvedConnectionString();
			uri = new MongoClientURI(connStr);
			mongoClient = new MongoClient(uri);
			CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
					MongoClientSettings.getDefaultCodecRegistry(),
					CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
			database = mongoClient.getDatabase(appCfg.getDb()).withCodecRegistry(pojoCodecRegistry);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
    }
	
	@PreDestroy
	private void preDestroy() {
		mongoClient.close();
    }
	
	public <T> MongoCollection<T> getCollection(String collName, Class<T> clazz) {
		return database.getCollection(collName, clazz);
	}
	
	public MongoCollection<Document> getCollection(String collName) {
		return database.getCollection(collName);
	}
}
