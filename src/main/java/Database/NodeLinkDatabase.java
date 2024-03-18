package Database;

import jdbm.helper.FastIterator;

import java.io.IOException;
import java.util.HashSet;

/**
 * A database to store the parent-to-child or child-to-parent relationship between nodes.
 */
public class NodeLinkDatabase extends AbstractDatabase {
    /**
     * Create a database with key as parent or child node and value as a set of child or parent nodes.
     * @param managerName The name of the database manager (filename of the db file).
     * @param objectName The name of the database object.
     */
    public NodeLinkDatabase(String managerName, String objectName) {
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
     * Print the database information with all the entries.
     * @throws IOException When there is an error in accessing the database.
     */
    @Override
    public void printDbInfo() throws IOException {
        FastIterator iter = hashtable.keys();
        Integer key = (Integer)iter.next();
        while (key != null) {
            System.out.println(key + " = " + hashtable.get(key));
            key = (Integer)iter.next();
        }
    }

}

