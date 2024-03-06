package Indexer;

import java.io.IOException;

public class PageIdToUrlIndexer extends URLIndexer {
    public PageIdToUrlIndexer(String managerName, String objectName) {
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
        System.out.println("Added: " + currentUrlID + " with url: " + url);
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

}
