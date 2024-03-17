package Database.NodeDatabase;

import Database.AbstractDatabase;

import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

public class ParentToChildNodeDatabase extends AbstractDatabase {
    public ParentToChildNodeDatabase(String managerName, String objectName) {
        super(managerName, objectName);
    }

    @SuppressWarnings("unchecked")
    public void addEntry(int parent, Vector<Integer> children) throws IOException {
        if (hashtable.get(parent) == null) {
            HashSet<Integer> childrenSet = new HashSet<>(children);
            hashtable.put(parent, childrenSet);
        }
        else {
            HashSet<Integer> childrenSet = (HashSet<Integer>) hashtable.get(parent);
            childrenSet.addAll(children);
            hashtable.put(parent, childrenSet);
        }
    }

}
