package Database;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * A database to store the forward index of the web pages.
 */
public class ForwardIndexDatabase extends AbstractDatabase {

    /**
     * Create a forward index database.
     * Remove all existing entries in the database before creating it.
     * @param databaseName The name of the database (filename of the db file).
     * @param columnName The name of the column in the database.
     * @throws IOException If an I/O error occurs when creating the database.
     */
    public ForwardIndexDatabase(String databaseName, String columnName) throws IOException {
        super(databaseName, columnName);
        deleteAll(); // we are re-indexing, so we need to clear the database
    }

    /**
     * Add an entry to the forward index database.
     * The key is the page ID and the value is a map of word ID and frequency.
     * @param pageId The page ID.
     * @param wordId The word ID.
     * @throws IOException If an I/O error occurs when adding the entry.
     */
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

    /**
     * Add a list of entries to the forward index database.
     * @param pageId The page ID.
     * @param wordIds The list of word IDs.
     * @throws IOException If an I/O error occurs when adding the entry.
     */
    public void addEntry(int pageId, Vector<Integer> wordIds) throws IOException {
        for (int wordId : wordIds) {
            addEntry(pageId, wordId);
        }
    }

}
