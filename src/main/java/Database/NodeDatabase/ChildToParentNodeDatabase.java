package Database.NodeDatabase;

import Database.AbstractDatabase;

import java.io.IOException;
import java.util.HashSet;

public class ChildToParentNodeDatabase extends AbstractDatabase {
    public ChildToParentNodeDatabase(String managerName, String objectName) {
        super(managerName, objectName);
    }

    @SuppressWarnings("unchecked")
    public void addEntry(int child, int parent) throws IOException {
        if (hashtable.get(child) == null) {
            HashSet<Integer> parentSet = new HashSet<>(parent);
            hashtable.put(child, parentSet);
        }
        else {
            HashSet<Integer> parentSet = (HashSet<Integer>) hashtable.get(child);
            parentSet.add(parent);
            hashtable.put(child, parentSet);
        }
    }

}
