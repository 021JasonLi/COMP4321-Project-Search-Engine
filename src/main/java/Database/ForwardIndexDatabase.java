package Database;

import jdbm.helper.FastIterator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class ForwardIndexDatabase extends AbstractDatabase {

    public ForwardIndexDatabase(String databaseName, String columnName) throws IOException {
        super(databaseName, columnName);
        deleteAll(); // we are re-indexing, so we need to clear the database
    }

    @SuppressWarnings("unchecked")
    public void addEntry(int pageId, int wordId) throws IOException {
        if (hashtable.get(pageId) == null) {
            HashMap<Integer, Integer> record = new HashMap<>();
            record.put(wordId, 1);
            hashtable.put(pageId, record);
        }
        else {
            HashMap<Integer, Integer> record = (HashMap<Integer, Integer>) hashtable.get(pageId);
            record.merge(wordId, 1, Integer::sum);
            hashtable.put(pageId, record);
        }
    }

    public void addEntry(int pageId, Vector<Integer> wordIds) throws IOException {
        for (int wordId : wordIds) {
            addEntry(pageId, wordId);
        }
    }

}
