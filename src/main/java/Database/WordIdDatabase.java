package Database;

import jdbm.helper.FastIterator;

import java.io.IOException;
import java.util.Vector;

/**
 * Bidirectional database to store the mapping from word to id and id to word.
 */
public class WordIdDatabase extends AbstractDatabase {
    private int id = 0;

    /**
     * Create a new WordIdDatabase instance.
     * It automatically updates the initial id based on the existing entries.
     * @param databaseName The name of the database (filename of the db file).
     * @param columnName The name of the column in the database.
     * @throws IOException If the database file cannot be created.
     */
    public WordIdDatabase(String databaseName, String columnName) throws IOException {
        super(databaseName, columnName);
        updateInitId();
    }

    /**
     * Add a new bidirectional entry to the database,
     * mapping the word to the id and the id to the word.
     * @param word The word to be added.
     * @throws IOException If the database file cannot be accessed.
     */
    public void addEntry(String word) throws IOException {
        if (hashtable.get(word) != null) {
            return;
        }
        hashtable.put(id, word);
        hashtable.put(word, id);
        id++;
    }

    /**
     * Add multiple words entries to the database.
     * @param words All the words to be added.
     * @throws IOException If the database file cannot be accessed.
     */
    public void addEntry(Vector<String> words) throws IOException {
        for (String word : words) {
            addEntry(word);
        }
    }

    /**
     * Get the id of a word.
     * @param word The word to get the id. If the word does not exist, return -1.
     * @return The id of the word.
     * @throws IOException If the database file cannot be accessed.
     */
    public int getId(String word) throws IOException {
        if (hashtable.get(word) == null) {
            return -1;
        }
        return (int) hashtable.get(word);
    }

    /**
     * Get the ids of multiple words.
     * @param words A list of words to get the ids.
     * @return A list of ids of the words. If the word does not exist, return -1.
     * @throws IOException If the database file cannot be accessed.
     */
    public Vector<Integer> getId(Vector<String> words) throws IOException {
        Vector<Integer> ids = new Vector<>();
        for (String word : words) {
            ids.add(getId(word));
        }
        return ids;
    }

    /**
     * Update the initial id based on the existing entries.
     * @throws IOException If the database file cannot be accessed.
     */
    private void updateInitId() throws IOException {
        FastIterator iter = hashtable.keys();
        Object key = iter.next();
        while (key != null) {
            if (key instanceof Integer) {
                if ((Integer) key >= id) {
                    id = (Integer) key + 1;
                }
            }
            key = iter.next();
        }
    }

}
