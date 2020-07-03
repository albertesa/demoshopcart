package albertesa.sample.prj.repositories;

import java.util.Collection;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Main interface to implement data persistence
 * @author alberte
 *
 */
public interface IRepository {

	/**
	 * Saves the document in the storage.
	 * @param <T> type of the document
	 * @param collName table name
	 * @param doc object of type <code>T</code>
	 * @param clazz class parameter of type <code>T</code>
	 */
	<T> void saveDocument(String collName, T doc, Class<T> clazz);

	/**
	 * Deletes the document from the storage.
	 * @param <T> type of the document
	 * @param collName table name
	 * @param id <code>id</code> of the document to delete
	 * @param clazz class parameter of type <code>T</code>
	 * @return number of deleted document
	 */
	<T> long deleteDocument(String collName, String id, Class<T> clazz);

	/**
	 * Gets all the documents from the storage
	 * @param collName table name
	 * @return query result in the form of a JSON node, empty collection if none found
	 * @throws Exception if exception is thrown
	 */
	Collection<JsonNode> getDocuments(String collName) throws Exception;

	/**
	 * Gets single document from the storage.
	 * @param collName table name
	 * @param id <code>id</code> if the document to retrieve
	 * @return result in the form of a JSON node, empty if none found 
	 * @throws Exception if exception is thrown 
	 */
	Optional<JsonNode> getDocument(String collName, String id) throws Exception;

	/**
	 * Replaces existing document in the storage.
	 * @param <T> type of the existing and new document
	 * @param collName table name
	 * @param id <code>id</code> if the existing document to replace
	 * @param doc object of type <code>T</code> to replace the existing one
	 * @param clazz class parameter of type <code>T</code>
	 * @return 
	 */
	<T> long replaceDocument(String collName, String id, T doc, Class<T> clazz);

}