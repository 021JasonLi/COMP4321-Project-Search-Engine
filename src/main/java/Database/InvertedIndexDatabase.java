package Database;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * A database to store the inverted index of the web pages.
 */
public class InvertedIndexDatabase extends AbstractDatabase {

    /**
     * Create an inverted index database.
     * Remove all existing entries in the database before creating it.
     * @param databaseName The name of the database (filename of the db file).
     * @param columnName The name of the column in the database.
     * @throws IOException If an I/O error occurs when creating the database.
     */
    public InvertedIndexDatabase(String databaseName, String columnName) throws IOException {
        super(databaseName, columnName);
        deleteAll(); // we are re-indexing, so we need to clear the database
    }

    /**
     * Add an entry to the inverted index database.
     * The key is the word ID and the value is a map of page ID and frequency.
     * @param wordId The word ID.
     * @param pageId The page ID.
     * @throws IOException If an I/O error occurs when adding the entry.
     */
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

    /**
     * Add a list of entries to the inverted index database.
     * @param wordIds The list of word IDs.
     * @param pageId The page ID.
     * @throws IOException If an I/O error occurs when adding the entry.
     */
    public void addEntry(Vector<Integer> wordIds, int pageId) throws IOException {
        for (int wordId : wordIds) {
            addEntry(wordId, pageId);
        }
    }

}
