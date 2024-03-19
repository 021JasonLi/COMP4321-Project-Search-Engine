package Indexer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class Indexer {

    private final HashMap<Integer, Vector<String>> tokens;
    private final StopWordRemoval stopWordRemoval;

    public Indexer(HashMap<Integer, Vector<String>> tokens) throws IOException {
        this.tokens = tokens;
        this.stopWordRemoval = new StopWordRemoval();
        removeStopWords();
    }



    private void removeStopWords() {
        for (Integer id : tokens.keySet()) {
            Vector<String> words = tokens.get(id);
            words = stopWordRemoval.removeStopWord(words);
            tokens.put(id, words);
            System.out.println("Removed stop words from page " + id);
            System.out.println("Remaining words: " + words + "\n");
        }
    }


}
