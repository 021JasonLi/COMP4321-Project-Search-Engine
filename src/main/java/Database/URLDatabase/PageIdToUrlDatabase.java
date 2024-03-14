package Database.URLDatabase;

import jdbm.helper.FastIterator;

import java.io.IOException;

public class PageIdToUrlDatabase extends URLDatabase {
    public PageIdToUrlDatabase(String managerName, String objectName) {
        super(managerName, objectName);
    }

    @Override
    public void addEntry(String url) throws IOException {
        for (int i = 0; i < currentUrlID; i++) {
            if (hashtable.get(i) != null && hashtable.get(i).equals(url)) {
                return;
            }
        }
        hashtable.put(currentUrlID, url);
        addIdCounter();
    }

    public String getEntry(int pageId) throws IOException {
        if (hashtable.get(pageId) == null) {
            return null;
        }
        return (String) hashtable.get(pageId);
    }

//    public void deleteEntry(int pageId) throws IOException {
//        if (hashtable.get(pageId) != null) {
//            hashtable.remove(pageId);
//        }
//    }

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
