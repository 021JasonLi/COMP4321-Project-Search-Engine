package Database.URLDatabase;

import Database.AbstractDatabase;

import java.io.IOException;

public abstract class URLDatabase<T, V> extends AbstractDatabase<T, V> {
    protected URLDatabase(String managerName, String objectName) {
        super(managerName, objectName);
    }

    @Override
    abstract public void addEntry(T key, V value) throws IOException;

}
