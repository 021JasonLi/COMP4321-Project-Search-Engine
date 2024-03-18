package Database;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

import java.io.IOException;

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
     * Print the database when all the keys and their corresponding values.
     * @throws IOException If an I/O error occurs when accessing elements in the database.
     */
    public void printDbInfo() throws IOException {
        FastIterator iter = hashtable.keys();
        String key = (String)iter.next();
        while (key != null) {
            System.out.println(key + " = " + hashtable.get(key));
            key = (String)iter.next();
        }
        System.out.println("\n");
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

}
