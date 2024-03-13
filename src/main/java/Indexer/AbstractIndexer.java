package Indexer;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

import java.io.IOException;

public abstract class AbstractIndexer {
    protected RecordManager manager;
    protected HTree hashtable;

    protected AbstractIndexer(String managerName, String objectName) {
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

    public void printDbInfo() throws IOException {
        FastIterator iter = hashtable.keys();
        String key = (String)iter.next();
        while (key != null) {
            System.out.println(key + " = " + hashtable.get(key));
            key = (String)iter.next();
        }
    }

    public void finish() {
        try {
            manager.commit();
            manager.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

}
