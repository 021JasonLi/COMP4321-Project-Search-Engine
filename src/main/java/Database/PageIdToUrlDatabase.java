package Database;

import Database.AbstractDatabase;
import jdbm.helper.FastIterator;

import java.io.IOException;

/**
 * A database to store the mapping from page id to URL.
 */
public class PageIdToUrlDatabase extends AbstractDatabase {
    private int id = 0;

    /**
     * Create a database with key as page id and value as url.
     * The page id is unique and update automatically based on the existing entries in the database.
     * @param managerName The name of the database manager (filename of the db file).
     * @param objectName The name of the database object.
     * @throws IOException When there is an error in creating the database.
     */
    public PageIdToUrlDatabase(String managerName, String objectName) throws IOException {
        super(managerName, objectName);
        updateInitId();
    }

    /**
     * Add a new URL to the database with a unique id.
     * The key is the id and the value is the URL.
     * If the URL already exists in the database, it will not be added.
     * @param url The URL to be added to the database.
     * @throws IOException When there is an error in adding the entry to the database.
     */
    public void addEntry(String url) throws IOException {
        for (int i = 0; i < id; i++) {
            if (hashtable.get(i).equals(url)) {
                return;
            }
        }
        hashtable.put(id, url);
        id++;
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
    public void printDbInfo() throws IOException {
        FastIterator iter = hashtable.keys();
        Integer key = (Integer)iter.next();
        while (key != null) {
            System.out.println(key + " = " + hashtable.get(key));
            key = (Integer)iter.next();
        }
    }

    /**
     * Update the initial id to the (maximum id + 1) in the database.
     * This is used to ensure that the id is unique.
     * @throws IOException When there is an error in getting the keys from the database.
     */
    private void updateInitId() throws IOException {
        FastIterator iter = hashtable.keys();
        Integer key = (Integer)iter.next();
        while (key != null) {
            if (key >= id) {
                id = key + 1;
            }
            key = (Integer)iter.next();
        }
    }

}
