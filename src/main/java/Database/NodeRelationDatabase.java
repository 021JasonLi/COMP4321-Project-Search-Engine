package Database;

import jdbm.helper.FastIterator;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A database to store the parent-to-child or child-to-parent relationship between nodes.
 */
public class NodeRelationDatabase extends AbstractDatabase {
    /**
     * Create a database with key as parent or child node and value as a set of child or parent nodes.
     * @param managerName The name of the database manager (filename of the db file).
     * @param objectName The name of the database object.
     */
    public NodeRelationDatabase(String managerName, String objectName) {
        super(managerName, objectName);
    }

    /**
     * Add a link from the parent node to the child node or vice versa in the database.
     * @param key The parent or child node.
     * @param value The child or parent node.
     * @throws IOException When there is an error in accessing the database.
     */
    @SuppressWarnings("unchecked")
    public void addEntry(int key, int value) throws IOException {
        if (hashtable.get(key) == null) {
            HashSet<Integer> set = new HashSet<>();
            set.add(value);
            hashtable.put(key, set);
        }
        else {
            HashSet<Integer> set = (HashSet<Integer>) hashtable.get(key);
            set.add(value);
            hashtable.put(key, set);
        }
    }

    /**
     * Get all the child nodes of all the parent nodes or vice versa from the database.
     * @return A map from all parent or child nodes to a set of their child or parent nodes.
     * @throws IOException When there is an error in accessing the database.
     */
    @SuppressWarnings("unchecked")
    public HashMap<Integer, HashSet<Integer>> getAllEntries() throws IOException {
        FastIterator iter = hashtable.keys();
        Integer key = (Integer)iter.next();
        HashMap<Integer, HashSet<Integer>> result = new HashMap<>();
        while (key != null) {
            result.put(key, (HashSet<Integer>)hashtable.get(key));
            key = (Integer)iter.next();
        }
        return result;
    }

    /**
     * Get all the child nodes of a parent node or vice versa from the database.
     * @param key The parent or child node.
     * @return A set of child or parent nodes.
     * @throws IOException When there is an error in accessing the database.
     */
    @SuppressWarnings("unchecked")
    public HashSet<Integer> getEntry(int key) throws IOException {
        return (HashSet<Integer>) hashtable.get(key);
    }

}

