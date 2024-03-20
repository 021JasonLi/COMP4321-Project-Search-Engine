package Indexer;

import Database.ForwardIndexDatabase;
import Database.InvertedIndexDatabase;
import Database.WordIdDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Indexer {

    private final HashMap<Integer, Vector<Integer>> titleTokenIds;
    private final HashMap<Integer, Vector<Integer>> bodyTokenIds;
    private final StopWordRemoval stopWordRemoval;
    private final Porter porter;

    private final WordIdDatabase wordIdDatabase;
    private final InvertedIndexDatabase titleInvertedIndexDatabase;
    private final InvertedIndexDatabase bodyInvertedIndexDatabase;
    private final ForwardIndexDatabase titleForwardIndexDatabase;
    private final ForwardIndexDatabase bodyForwardIndexDatabase;


    public Indexer(ArrayList<HashMap<Integer, Vector<String>>> tokens) throws IOException {
        this.stopWordRemoval = new StopWordRemoval();
        this.porter = new Porter();
        HashMap<Integer, Vector<String>> titleTokens = stopStem(tokens.get(0));
        HashMap<Integer, Vector<String>> bodyTokens = stopStem(tokens.get(1));

        this.wordIdDatabase = new WordIdDatabase(
                "wordIdDatabase", "word");
        this.titleInvertedIndexDatabase = new InvertedIndexDatabase(
                "titleInvertedIndexDatabase", "invertedIndex");
        this.bodyInvertedIndexDatabase = new InvertedIndexDatabase(
                "bodyInvertedIndexDatabase", "invertedIndex");
        this.titleForwardIndexDatabase = new ForwardIndexDatabase(
                "titleForwardIndexDatabase", "forwardIndex");
        this.bodyForwardIndexDatabase = new ForwardIndexDatabase(
                "bodyForwardIndexDatabase", "forwardIndex");

        // Add all words to the wordIdDatabase
        for (Integer id : titleTokens.keySet()) {
            wordIdDatabase.addEntry(titleTokens.get(id));
        }
        for (Integer id : bodyTokens.keySet()) {
            wordIdDatabase.addEntry(bodyTokens.get(id));
        }

        // Convert all words to word ids
        this.titleTokenIds = new HashMap<>();
        this.bodyTokenIds = new HashMap<>();
        for (Integer id : titleTokens.keySet()) {
            titleTokenIds.put(id, wordIdDatabase.getId(titleTokens.get(id)));
        }
        for (Integer id : bodyTokens.keySet()) {
            bodyTokenIds.put(id, wordIdDatabase.getId(bodyTokens.get(id)));
        }
    }

    public void index() throws IOException {
        invertedIndex();
        forwardIndex();
        finalizeAllDatabases();
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

    private void invertedIndex() throws IOException {
        for (Integer id : titleTokenIds.keySet()) {
            titleInvertedIndexDatabase.addEntry(titleTokenIds.get(id), id);
        }
        for (Integer id : bodyTokenIds.keySet()) {
            bodyInvertedIndexDatabase.addEntry(bodyTokenIds.get(id), id);
        }
    }

    private void forwardIndex() throws IOException {
        for (Integer id : titleTokenIds.keySet()) {
            titleForwardIndexDatabase.addEntry(id, titleTokenIds.get(id));
        }
        for (Integer id : bodyTokenIds.keySet()) {
            bodyForwardIndexDatabase.addEntry(id, bodyTokenIds.get(id));
        }
    }

    private void finalizeAllDatabases() {
        wordIdDatabase.finish();
        titleInvertedIndexDatabase.finish();
        bodyInvertedIndexDatabase.finish();
        titleForwardIndexDatabase.finish();
        bodyForwardIndexDatabase.finish();
    }

}
