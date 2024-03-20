package Database;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class InvertedIndexDatabase extends AbstractDatabase {

    public InvertedIndexDatabase(String databaseName, String columnName) throws IOException {
        super(databaseName, columnName);
        deleteAll(); // we are re-indexing, so we need to clear the database
    }

    @SuppressWarnings("unchecked")
    public void addEntry(int wordId, int pageId) throws IOException {
        if (hashtable.get(wordId) == null) {
            HashMap<Integer, Integer> record = new HashMap<>();
            record.put(pageId, 1);
            hashtable.put(wordId, record);
        }
        else {
            HashMap<Integer, Integer> record = (HashMap<Integer, Integer>) hashtable.get(wordId);
            record.merge(pageId, 1, Integer::sum);
            hashtable.put(wordId, record);
        }
    }

    public void addEntry(Vector<Integer> wordIds, int pageId) throws IOException {
        for (int wordId : wordIds) {
            addEntry(wordId, pageId);
        }
    }

}
