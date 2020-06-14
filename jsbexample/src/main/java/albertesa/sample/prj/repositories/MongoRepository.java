package albertesa.sample.prj.repositories;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import albertesa.sample.prj.config.MongoDbClientWrapper;

@Repository
public class MongoRepository implements IRepository {
	
	private ObjectMapper objMapper;
	private MongoDbClientWrapper client;
	
	@Autowired
	public MongoRepository(MongoDbClientWrapper client, ObjectMapper objMapper) {
		super();
		this.client = client;
		this.objMapper = objMapper;
	}

	@Override
	public <T> void saveDocument(String collName, T obj, Class<T> clazz) {
		try {
			MongoCollection<T> collection = client.getCollection(collName, clazz);
			collection.insertOne(obj);
		} catch (Exception e) {
			throw e;
		}

	}
	
	@Override
	public <T> long deleteDocument(String collName, String id, Class<T> clazz) {
		try {
			MongoCollection<T> collection = client.getCollection(collName, clazz);
			DeleteResult delRes = collection.deleteOne(eq("_id", id));
			return delRes.getDeletedCount();
		} catch (Exception e) {
			throw e;
		}

	}
	
	@Override
	public Collection<JsonNode> getDocuments(String collName) throws Exception {
		try {
			Collection<JsonNode> jNodes = new ArrayList<>();
			MongoCollection<Document> collection = client.getCollection(collName);
			Iterator<Document> iterDocs = collection.find().iterator();
			while(iterDocs.hasNext()) {
				Document doc = iterDocs.next();
				jNodes.add(objMapper.readTree(doc.toJson()));
			}
			return jNodes;
		} catch (Exception e) {
			throw e;
		}

	}
	
	@Override
	public Optional<JsonNode> getDocument(String collName, String id) throws Exception {
		try {
			MongoCollection<Document> collection = client.getCollection(collName);
			Document doc = collection.find(eq("_id", id)).first();
			if (doc != null) {
				return Optional.of(objMapper.readTree(doc.toJson()));
			}
			return Optional.empty();
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public <T> void replaceDocument(String collName, String id, T obj, Class<T> clazz) {
		try {
			MongoCollection<T> collection = client.getCollection(collName, clazz);
			collection.replaceOne(eq("_id", id), obj);
		} catch (Exception e) {
			throw e;
		}		
	}

}
