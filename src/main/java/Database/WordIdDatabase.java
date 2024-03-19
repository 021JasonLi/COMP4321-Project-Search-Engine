package Database;

import jdbm.helper.FastIterator;

import java.io.IOException;
import java.util.Vector;

/**
 * Bidirectional database to store the mapping from word to id and id to word.

 */
public class WordIdDatabase extends AbstractDatabase {
    private int id = 0;

    public WordIdDatabase(String databaseName, String columnName) throws IOException {
        super(databaseName, columnName);
        updateInitId();
    }

    public void addEntry(String word) throws IOException {
        if (hashtable.get(word) != null) {
            return;
        }
        hashtable.put(id, word);
        hashtable.put(word, id);
        id++;
    }

    public void addEntry(Vector<String> words) throws IOException {
        for (String word : words) {
            addEntry(word);
        }
    }

    public Vector<Integer> convertTokensToIds(Vector<String> tokens) throws IOException {
        Vector<Integer> ids = new Vector<>();
        for (String token : tokens) {
            if (hashtable.get(token) != null) {
                ids.add((int)hashtable.get(token));
            }
        }
        return ids;
    }

    private void updateInitId() throws IOException {
        FastIterator iter = hashtable.keys();
        Object key = iter.next();
        while (key != null) {
            if (key instanceof String) {
                if ((int) hashtable.get(key) >= id) {
                    id = (int) hashtable.get(key) + 1;
                }
            }
            key = iter.next();
        }
    }

    public void printAllEntries() throws IOException {
        FastIterator iter = hashtable.keys();
        Object key = iter.next();
        while (key != null) {
            if (key instanceof Integer) {
                System.out.println(key + " : " + hashtable.get(key));
            }
            key = iter.next();
        }
    }
}
