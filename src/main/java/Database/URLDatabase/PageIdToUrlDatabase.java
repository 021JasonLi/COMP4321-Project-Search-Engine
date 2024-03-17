package Database.URLDatabase;

import jdbm.helper.FastIterator;

import java.io.IOException;

public class PageIdToUrlDatabase extends URLDatabase {
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
    @Override
    public void addEntry(String url) throws IOException {
        for (int i = 0; i < id; i++) {
            if (hashtable.get(i).equals(url)) {
                return;
            }
        }
        hashtable.put(id, url);
        id++;
    }

    public String getEntry(int pageId) throws IOException {
        if (hashtable.get(pageId) == null) {
            return null;
        }
        return (String) hashtable.get(pageId);
    }

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
