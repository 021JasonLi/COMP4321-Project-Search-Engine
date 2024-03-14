package Database.URLDatabase;

import java.io.IOException;

public class UrlToPageIdDatabase extends URLDatabase<String, Integer> {
    public UrlToPageIdDatabase(String managerName, String objectName) {
        super(managerName, objectName);
    }

    @Override
    public void addEntry(String url, Integer id) throws IOException {
        if (hashtable.get(url) != null) {
            return;
        }
        hashtable.put(url, id);
    }

    public int getEntry(String url) throws IOException {
        if (hashtable.get(url) == null) {
            return -1;
        }
        return (int) hashtable.get(url);
    }

}
