package Indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Indexer {

    private final HashMap<Integer, Vector<String>> titleTokens;
    private final HashMap<Integer, Vector<String>> bodyTokens;
    private final StopWordRemoval stopWordRemoval;
    private final Porter porter;

    public Indexer(ArrayList<HashMap<Integer, Vector<String>>> tokens) throws IOException {
        this.stopWordRemoval = new StopWordRemoval();
        this.porter = new Porter();
        this.titleTokens = stopStem(tokens.get(0));
        this.bodyTokens = stopStem(tokens.get(1));
    }

    private HashMap<Integer, Vector<String>> stopStem(HashMap<Integer, Vector<String>> tokens) {
        HashMap<Integer, Vector<String>> result = new HashMap<>();
        for (Integer id : tokens.keySet()) {
            Vector<String> removedStopWords = stopWordRemoval.removeStopWord(tokens.get(id));
            Vector<String> stemmed = porter.stripAffixes(removedStopWords);
            result.put(id, stemmed);
        }
        return result;
    }

}
