package Indexer;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;

import java.io.IOException;

abstract class AbstractIndexer {
    protected RecordManager manager;
    protected HTree hashtable;

    AbstractIndexer(String managerName, String objectName) {
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

    public void finish() {
        try {
            manager.commit();
            manager.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

}
