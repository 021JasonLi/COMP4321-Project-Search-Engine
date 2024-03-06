package Indexer;

import java.io.IOException;

public abstract class URLIndexer extends AbstractIndexer {
    protected int currentUrlID = 0;

    protected URLIndexer(String managerName, String objectName) {
        super(managerName, objectName);
    }

    protected void addIdCounter() {
        currentUrlID++;
    }

    abstract public void addEntry(String url) throws IOException;

}
