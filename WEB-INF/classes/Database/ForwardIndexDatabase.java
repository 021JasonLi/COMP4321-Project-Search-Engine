package Database;

import jdbm.helper.FastIterator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * A database to store the forward index of the web pages.
 */
public class ForwardIndexDatabase extends AbstractDatabase {

    /**
     * Create a forward index database. If the database is being re-indexed,
     * remove all existing entries in the database before creating it.
     * Otherwise, the existing database will be used for accessing.
     * @param databaseName The name of the database (filename of the db file).
     * @param columnName The name of the column in the database.
     */
    public ForwardIndexDatabase(String databaseName, String columnName) {
        super(databaseName, columnName);
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
    public void addEntry(int pageId, Vector<Integer> wordIds)
            throws IOException {
        for (int wordId : wordIds) {
            addEntry(pageId, wordId);
        }
    }

    /**
     * Get all entries in the forward index database.
     * @return A map of page ID to a map of word ID and frequency.
     * @throws IOException If an I/O error occurs when getting the entries.
     */
    @SuppressWarnings("unchecked")
    public HashMap<Integer, HashMap<Integer, Integer>> getAllEntries() throws IOException {
        HashMap<Integer, HashMap<Integer, Integer>> entries = new HashMap<>();
        FastIterator iter = hashtable.keys();
        Integer key = (Integer)iter.next();
        while (key != null) {
            entries.put(key, (HashMap<Integer, Integer>) hashtable.get(key));
            key = (Integer)iter.next();
        }
        return entries;
    }

    /**
     * Get an entry in the forward index database.
     * @param pageId The page ID.
     * @return A map of word ID and frequency.
     * @throws IOException If an I/O error occurs when getting the entry.
     */
    @SuppressWarnings("unchecked")
    public HashMap<Integer, Integer> getEntry(int pageId) throws IOException {
        return (HashMap<Integer, Integer>) hashtable.get(pageId);
    }

}
