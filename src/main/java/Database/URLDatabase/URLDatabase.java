package Database.URLDatabase;

import Database.AbstractDatabase;

import java.io.IOException;

public abstract class URLDatabase extends AbstractDatabase {
    protected int id = 0;

    protected URLDatabase(String managerName, String objectName) {
        super(managerName, objectName);
    }

    abstract public void addEntry(String url) throws IOException;

}
