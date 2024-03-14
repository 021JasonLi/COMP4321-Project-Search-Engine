package Database.URLDatabase;

import java.io.IOException;

public class UrlToPageIdDatabase extends URLDatabase {
    public UrlToPageIdDatabase(String managerName, String objectName) {
        super(managerName, objectName);
    }

    @Override
    public void addEntry(String url) throws IOException {
        if (hashtable.get(url) != null) {
            return;
        }
        hashtable.put(url, currentUrlID);
        addIdCounter();
    }

    public int getEntry(String url) throws IOException {
        if (hashtable.get(url) == null) {
            return -1;
        }
        return (int) hashtable.get(url);
    }

//    public void deleteEntry(String url) throws IOException {
//        if (hashtable.get(url) != null) {
//            hashtable.remove(url);
//        }
//    }
}
