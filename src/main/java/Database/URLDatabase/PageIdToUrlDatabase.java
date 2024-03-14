package Database.URLDatabase;

import jdbm.helper.FastIterator;

import java.io.IOException;

public class PageIdToUrlDatabase extends URLDatabase<Integer, String> {
    public PageIdToUrlDatabase(String managerName, String objectName) {
        super(managerName, objectName);
    }

    @Override
    public void addEntry(Integer id, String url) throws IOException {
        for (int i = 0; i < id; i++) {
            if (hashtable.get(i) != null && hashtable.get(i).equals(url)) {
                return;
            }
        }
        hashtable.put(id, url);
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

}
