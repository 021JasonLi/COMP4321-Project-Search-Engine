package Database;

import jdbm.helper.FastIterator;

import java.io.IOException;
import java.util.HashMap;

/**
 * A database to store the mapping from page id to URL.
 */
public class NodePropertyDatabase extends AbstractDatabase {

    /**
     * Create a database with key as page id and value as url.
     * The page id is unique and update automatically based on the existing entries in the database.
     * @param managerName The name of the database manager (filename of the db file).
     * @param objectName The name of the database object.
     */
    public NodePropertyDatabase(String managerName, String objectName) {
        super(managerName, objectName);
    }

    /**
     * Add an entry to the database with the given page id and its properties,
     * including the URL and page title, last modified date and size of page.
     * If the entry already exists in the database, it will not be added again.
     * @param id The unique id of the URL.
     * @param properties The properties of the URL.
     * @throws IOException When there is an error in adding the entry to the database.
     */
    @SuppressWarnings("unchecked")
    public void addEntry(int id, HashMap<String, String> properties) throws IOException {
        FastIterator iter = hashtable.keys();
        Integer key = (Integer)iter.next();
        while (key != null) {
            String url = ((HashMap<String, String>) hashtable.get(key)).get("url");
            if ((key == id) && (url.equals(properties.get("url")))) {
                return;
            }
            key = (Integer)iter.next();
        }
        hashtable.put(id, properties);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> getEntry(int id) throws IOException {
        return (HashMap<String, String>) hashtable.get(id);
    }

    /**
     * Get all properties entries in the database.
     * @return A map of all page ids to their properties.
     * @throws IOException When there is an error in getting the entries from the database.
     */
    @SuppressWarnings("unchecked")
    public HashMap<Integer, HashMap<String, String>> getAllEntries() throws IOException {
        HashMap<Integer, HashMap<String, String>> entries = new HashMap<>();
        FastIterator iter = hashtable.keys();
        Integer key = (Integer)iter.next();
        while (key != null) {
            entries.put(key, (HashMap<String, String>) hashtable.get(key));
            key = (Integer)iter.next();
        }
        return entries;
    }

    /**
     * Get the URL of the page with the given page id.
     * @param id The unique id of the URL.
     * @return The URL of the page with the given page id.
     * @throws IOException When there is an error in getting the URL from the database.
     */
    @SuppressWarnings("unchecked")
    public String getUrl(int id) throws IOException {
        return ((HashMap<String, String>) hashtable.get(id)).get("url");
    }

    /**
     * Get the last modified date of the page with the given page id.
     * @param id The unique id of the URL.
     * @return The last modified date of the page with the given page id in long format.
     * If the last modified date is unknown, it will return 0.
     * @throws IOException When there is an error in getting the last modified date from the database.
     */
    @SuppressWarnings("unchecked")
    public long getLastModified(int id) throws IOException {
        if (hashtable.get(id) == null) {
            return 0;
        }
        if (((HashMap<String, String>) hashtable.get(id)).get("lastModified").equals("unknown")) {
            return 0;
        }
        return Long.parseLong(((HashMap<String, String>) hashtable.get(id)).get("lastModified"));
    }

}
