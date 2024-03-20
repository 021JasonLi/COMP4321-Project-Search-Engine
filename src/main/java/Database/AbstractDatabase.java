package Database;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

import java.io.IOException;
import java.util.ArrayList;

/**
 * An abstract database class.
 * It provides the basic functionalities for a database.
 */
public abstract class AbstractDatabase {
    protected RecordManager manager;
    protected HTree hashtable;

    /**
     * Create a JDBM database.
     * @param managerName The name of the database manager (name of the db file).
     * @param objectName The name of the database object.
     */
    protected AbstractDatabase(String managerName, String objectName) {
        try {
            manager = RecordManagerFactory.createRecordManager("./db/" + managerName);
            long recId = manager.getNamedObject(objectName);
            if (recId != 0) {
                hashtable = HTree.load(manager, recId);
            } else {
                hashtable = HTree.createInstance(manager);
                manager.setNamedObject(objectName, hashtable.getRecid());
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Commit and close the database.
     */
    public void finish() {
        try {
            manager.commit();
            manager.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Delete all entries in the database. Since some databases may require to be re-indexed,
     * so this method is provided to clear the database.
     * @throws IOException If the database file cannot be accessed.
     */
    protected void deleteAll() throws IOException {
        FastIterator iter = hashtable.keys();
        ArrayList<Object> keysToRemove = new ArrayList<>();
        Object key = iter.next();
        while (key != null) {
            keysToRemove.add(key);
            key = iter.next();
        }
        for (Object keyToRemove : keysToRemove) {
            hashtable.remove(keyToRemove);
        }
    }

}
