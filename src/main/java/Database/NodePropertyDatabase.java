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
        if (id == -1) return;
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

    /**
     * Get the URL of the given page id from the database.
     * @param pageId The page id to get the URL from the database.
     * @return The URL of the page id if it exists in the database, otherwise null.
     * @throws IOException When there is an error in getting the entry from the database.
     */
    public String getEntry(int pageId) throws IOException {
        if (hashtable.get(pageId) == null) {
            return null;
        }
        return (String) hashtable.get(pageId);
    }

    /**
     * Print the database information with all the entries.
     * @throws IOException When there is an error in accessing the database.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void printDbInfo() throws IOException {
        FastIterator iter = hashtable.keys();
        Integer key = (Integer)iter.next();
        while (key != null) {
            HashMap<String, String> properties = (HashMap<String, String>) hashtable.get(key);
            System.out.println(key + ":");
            System.out.println("URL: " + properties.get("url"));
            System.out.println("Title: " + properties.get("title"));
            System.out.println("Last Modified: " + properties.get("lastModified"));
            System.out.println("Size: " + properties.get("size"));
            System.out.println();
            key = (Integer)iter.next();
        }
        System.out.println("\n");
    }

}
