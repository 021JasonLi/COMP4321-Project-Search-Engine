package Database.URLDatabase;

import Database.AbstractDatabase;
import jdbm.helper.FastIterator;

import java.io.IOException;

/**
 * A database to store the mapping from URL to page id.
 */
public class UrlToPageIdDatabase extends AbstractDatabase {
    private int id = 0;

    /**
     * Create a database with key as URL and value as page id.
     * The page id is unique and update automatically based on the existing entries in the database.
     * @param managerName The name of the database manager (filename of the db file).
     * @param objectName The name of the database object.
     * @throws IOException When there is an error in creating the database.
     */
    public UrlToPageIdDatabase(String managerName, String objectName) throws IOException {
        super(managerName, objectName);
        updateInitId();
    }

    /**
     * Add a new URL to the database with a unique id.
     * The key is the URL and the value is the id.
     * If the URL already exists in the database, it will not be added.
     * @param url The URL to be added to the database.
     * @throws IOException When there is an error in adding the entry to the database.
     */
    public void addEntry(String url) throws IOException {
        if (hashtable.get(url) != null) {
            return;
        }
        hashtable.put(url, id);
        id++;
    }

    /**
     * Get the id of the given URL from the database.
     * @param url The URL to get the id from the database.
     * @return The id of the URL if it exists in the database, otherwise -1.
     * @throws IOException When there is an error in getting the entry from the database.
     */
    public int getEntry(String url) throws IOException {
        if (hashtable.get(url) == null) {
            return -1;
        }
        return (int) hashtable.get(url);
    }

    /**
     * Update the initial id to the (maximum id + 1) in the database.
     * This is used to ensure that the id is unique.
     * @throws IOException When there is an error in getting the keys from the database.
     */
    private void updateInitId() throws IOException {
        FastIterator iter = hashtable.keys();
        String key = (String)iter.next();
        while (key != null) {
            if ((int)hashtable.get(key) >= id) {
                id = (int)hashtable.get(key) + 1;
            }
            key = (String)iter.next();
        }
    }

}
