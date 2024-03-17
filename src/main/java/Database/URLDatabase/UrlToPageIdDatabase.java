package Database.URLDatabase;

import jdbm.helper.FastIterator;

import java.io.IOException;

public class UrlToPageIdDatabase extends URLDatabase {
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
    @Override
    public void addEntry(String url) throws IOException {
        if (hashtable.get(url) != null) {
            return;
        }
        hashtable.put(url, id);
        id++;
    }

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
