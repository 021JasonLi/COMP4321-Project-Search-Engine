package Indexer;

import Database.ForwardIndexDatabase;
import Database.InvertedIndexDatabase;
import Database.WordIdDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * An indexer to index the web pages (both title and body).
 */
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


    /**
     * Create an indexer instance.
     * The title and body tokens are processed by stop word removal and stemming.
     * Bi-gram and tri-gram are also generated.
     * Each word and n-gram are stored as wordId internally.
     * @param tokens An array list of title token and body token.
     * @throws IOException If an I/O error occurs when creating the databases.
     */
    public Indexer(ArrayList<HashMap<Integer, Vector<String>>> tokens)
            throws IOException {
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

    /**
     * Index the web pages (both title and body) by inverted index and forward index.
     * @throws IOException If an I/O error occurs when indexing the web pages.
     */
    public void index() throws IOException {
        invertedIndex();
        forwardIndex();
        finalizeAllDatabases();
    }

    /**
     * Remove stop words and stem the tokens.
     * @param tokens The tokens to be processed.
     * @return The tokens after stop word removal and stemming.
     */
    private HashMap<Integer, Vector<String>> stopStem(
            HashMap<Integer, Vector<String>> tokens) {
        HashMap<Integer, Vector<String>> result = new HashMap<>();
        HashMap<Integer, Vector<String>> biGram = getBiGram(tokens);
        HashMap<Integer, Vector<String>> triGram = getTriGram(tokens);
        for (Integer id : tokens.keySet()) {
            Vector<String> removedSymbols = porter.removeSymbols(tokens.get(id));
            Vector<String> removedStopWords = stopWordRemoval.removeStopWord(removedSymbols);
            Vector<String> stemmed = porter.stripAffixes(removedStopWords);
            stemmed.addAll(biGram.get(id));
            stemmed.addAll(triGram.get(id));
            result.put(id, stemmed);
        }
        return result;
    }

    /**
     * Get bi-gram from the tokens. If the bi-gram contains stop words, it will be removed.
     * Each word in the bi-gram will then be stemmed.
     * @param tokens The tokens to generate bi-gram.
     * @return The bi-gram after stop word removal and stemming.
     */
    private HashMap<Integer, Vector<String>> getBiGram(
            HashMap<Integer, Vector<String>> tokens) {
        HashMap<Integer, Vector<String>> result = new HashMap<>();
        for (Integer id : tokens.keySet()) {
            if (tokens.get(id).size() < 2) {
                result.put(id, new Vector<>());
                continue;
            }
            Vector<String> biGram = new Vector<>();
            for (int i = 0; i < tokens.get(id).size() - 1; i++) {
                Vector<String> biGramPair = new Vector<>();
                biGramPair.add(tokens.get(id).get(i));
                biGramPair.add(tokens.get(id).get(i + 1));
                biGramPair = porter.removeSymbols(biGramPair);
                if (stopWordRemoval.notContainStopWord(biGramPair)) {
                    Vector<String> stemmed = porter.stripAffixes(biGramPair);
                    if (stemmed.size() == 2) {
                        biGram.add(stemmed.get(0) + " " + stemmed.get(1));
                    }
                }
            }
            result.put(id, biGram);
        }
        return result;
    }

    /**
     * Get tri-gram from the tokens. If the tri-gram contains stop words, it will be removed.
     * Each word in the tri-gram will then be stemmed.
     * @param tokens The tokens to generate tri-gram.
     * @return The tri-gram after stop word removal and stemming.
     */
    private HashMap<Integer, Vector<String>> getTriGram(
            HashMap<Integer, Vector<String>> tokens) {
        HashMap<Integer, Vector<String>> result = new HashMap<>();
        for (Integer id : tokens.keySet()) {
            if (tokens.get(id).size() < 3) {
                result.put(id, new Vector<>());
                continue;
            }
            Vector<String> triGram = new Vector<>();
            for (int i = 0; i < tokens.get(id).size() - 2; i++) {
                Vector<String> triGramPair = new Vector<>();
                triGramPair.add(tokens.get(id).get(i));
                triGramPair.add(tokens.get(id).get(i + 1));
                triGramPair.add(tokens.get(id).get(i + 2));
                triGramPair = porter.removeSymbols(triGramPair);
                if (stopWordRemoval.notContainStopWord(triGramPair)) {
                    Vector<String> stemmed = porter.stripAffixes(triGramPair);
                    if (stemmed.size() == 3) {
                        triGram.add(stemmed.get(0) + " " + stemmed.get(1) + " " + stemmed.get(2));
                    }
                }
            }
            result.put(id, triGram);
        }
        return result;
    }

    /**
     * Create inverted index for both title and body.
     * @throws IOException If an I/O error occurs when creating the inverted index.
     */
    private void invertedIndex() throws IOException {
        for (Integer id : titleTokenIds.keySet()) {
            titleInvertedIndexDatabase.addEntry(titleTokenIds.get(id), id);
        }
        for (Integer id : bodyTokenIds.keySet()) {
            bodyInvertedIndexDatabase.addEntry(bodyTokenIds.get(id), id);
        }
    }

    /**
     * Create forward index for both title and body.
     * @throws IOException If an I/O error occurs when creating the forward index.
     */
    private void forwardIndex() throws IOException {
        for (Integer id : titleTokenIds.keySet()) {
            titleForwardIndexDatabase.addEntry(id, titleTokenIds.get(id));
        }
        for (Integer id : bodyTokenIds.keySet()) {
            bodyForwardIndexDatabase.addEntry(id, bodyTokenIds.get(id));
        }
    }

    /**
     * Finalize all databases.
     */
    private void finalizeAllDatabases() {
        wordIdDatabase.finish();
        titleInvertedIndexDatabase.finish();
        bodyInvertedIndexDatabase.finish();
        titleForwardIndexDatabase.finish();
        bodyForwardIndexDatabase.finish();
    }

}
