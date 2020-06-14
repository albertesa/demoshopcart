package albertesa.sample.prj.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

public class CartDocumentBuilder {

	private Map<String, Object> mapC = new HashMap<>();
	private String id;
	private List<Document> docs = new ArrayList<>();
			
	public CartDocumentBuilder() {
		super();
	}

	public CartDocumentBuilder(String id, List<Document> docs) {
		this.id = id;
		this.docs.addAll(docs);
	}
	
	public CartDocumentBuilder withItem(Document doc) {
		docs.add(doc);
		return this;
	}
	
	public Document build() {
		mapC.put("_id", id);
		mapC.put("items", docs);
		return new Document(mapC);		
	}
}
