package SearchEngine;

import Database.ForwardIndexDatabase;
import Database.InvertedIndexDatabase;
import Database.NodePropertyDatabase;
import Database.WordIdDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * The class to calculate the weight of the terms in the query.
 */
public class WeightCalculator {
    private final NodePropertyDatabase nodePropertyDatabase;
    private final InvertedIndexDatabase titleInvertedIndexDatabase;
    private final InvertedIndexDatabase bodyInvertedIndexDatabase;
    private final ForwardIndexDatabase titleForwardIndexDatabase;
    private final ForwardIndexDatabase bodyForwardIndexDatabase;
    private final WordIdDatabase wordIdDatabase;

    private final int totalNumberOfDocuments;

    /**
     * The constructor to initialize the databases.
     * @throws IOException if there is an error in the database.
     */
    public WeightCalculator() throws IOException {
        nodePropertyDatabase = new NodePropertyDatabase(
                "nodePropertyDatabase", "property");
        titleInvertedIndexDatabase = new InvertedIndexDatabase(
                "titleInvertedIndexDatabase", "invertedIndex");
        bodyInvertedIndexDatabase = new InvertedIndexDatabase(
                "bodyInvertedIndexDatabase", "invertedIndex");
        titleForwardIndexDatabase = new ForwardIndexDatabase(
                "titleForwardIndexDatabase", "forwardIndex");
        bodyForwardIndexDatabase = new ForwardIndexDatabase(
                "bodyForwardIndexDatabase", "forwardIndex");
        wordIdDatabase = new WordIdDatabase(
                "wordIdDatabase", "word");
        totalNumberOfDocuments = nodePropertyDatabase.getAllEntries().size();
    }

    /**
     * Get the term weight of the query.
     * The term weight is calculated as the term frequency (TF) multiplied by the
     * inverse document frequency (IDF) divided by the maximum term frequency (maxTF).
     * @param query the query to search
     * @param title whether the query is for the title or the body
     * @return the term weight of the query
     * @throws IOException if there is an error in the database
     */
    public HashMap<Integer, HashMap<Integer, Double>> getTermWeight (
            Vector<String> query, boolean title) throws IOException {
        HashMap<Integer, HashMap<Integer, Double>> termWeight = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Integer>> tf = getTF(query, title);
        HashMap<Integer, Integer> maxTF = getMaxTF(title);
        HashMap<Integer, Double> idf = getIDF(query, title);
        for (int pageId: tf.keySet()) {
            HashMap<Integer, Double> termWeightValue = new HashMap<>();
            HashMap<Integer, Integer> wordTF = tf.get(pageId);
            for (int wordId: wordTF.keySet()) {
                double weight = wordTF.get(wordId) * idf.get(wordId) / maxTF.get(pageId);
                termWeightValue.put(wordId, weight);
            }
            termWeight.put(pageId, termWeightValue);
        }
        return termWeight;
    }

    /**
     * Get the query weight of the query.
     * It is the number of times each word appears in the query.
     * @param query the query to be calculated
     * @return the query weight of the query
     * @throws IOException if there is an error in the database
     */
    public HashMap<Integer, Integer> getQueryWeight(Vector<String> query)
            throws IOException {
        HashMap<Integer, Integer> queryWeight = new HashMap<>();
        for (String keyword : query) {
            int wordId = wordIdDatabase.getId(keyword);
            if (wordId != -1) {
                queryWeight.put(wordId, queryWeight.getOrDefault(wordId, 0) + 1);
            }
        }
        return queryWeight;
    }

    /**
     * Get the term frequency (TF) of the query on each page.
     * @param query the query to be calculated
     * @param title whether the query is for the title or the body
     * @return the term frequency (TF) of the query
     * @throws IOException if there is an error in the database
     */
    private HashMap<Integer, HashMap<Integer, Integer>> getTF(
            Vector<String> query, boolean title) throws IOException {
        HashMap<Integer, HashMap<Integer, Integer>> tf = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Integer>> forwardIndex = title ?
                titleForwardIndexDatabase.getAllEntries() :
                bodyForwardIndexDatabase.getAllEntries();
        for (int pageId: forwardIndex.keySet()) {
            HashMap<Integer, Integer> wordTF = forwardIndex.get(pageId);
            HashMap<Integer, Integer> tfValue = new HashMap<>();
            for (String keyword : query) {
                int wordId = wordIdDatabase.getId(keyword);
                if (wordId != -1) {
                    tfValue.put(wordId, wordTF.getOrDefault(wordId, 0));
                }
            }
            tf.put(pageId, tfValue);
        }
        return tf;
    }

    /**
     * Get the maximum term frequency (maxTF) for each page.
     * @param title whether the query is for the title or the body
     * @return the maximum term frequency (maxTF) for each page
     * @throws IOException if there is an error in the database
     */
    private HashMap<Integer, Integer> getMaxTF(boolean title)
            throws IOException {
        HashMap<Integer, Integer> maxTF = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Integer>> forwardIndex = title ?
                titleForwardIndexDatabase.getAllEntries() :
                bodyForwardIndexDatabase.getAllEntries();
        for (int pageId: forwardIndex.keySet()) {
            int max = 0;
            HashMap<Integer, Integer> wordTF = forwardIndex.get(pageId);
            for (int wordId: wordTF.keySet()) {
                if (wordTF.get(wordId) > max) {
                    max = wordTF.get(wordId);
                }
            }
            maxTF.put(pageId, max);
        }
        return maxTF;
    }

    /**
     * Get the document frequency (DF) of the query.
     * @param query the query to be calculated
     * @param title whether the query is for the title or the body
     * @return the document frequency (DF) of the query
     * @throws IOException if there is an error in the database
     */
    private HashMap<Integer, Integer> getDF(Vector<String> query,
                                            boolean title)
            throws IOException {
        HashMap<Integer, Integer> df = new HashMap<>();
        for (String keyword : query) {
            int wordId = wordIdDatabase.getId(keyword);
            HashMap<Integer, Integer> invertedIndex = title ?
                    titleInvertedIndexDatabase.getEntry(wordId) :
                    bodyInvertedIndexDatabase.getEntry(wordId);
            if (invertedIndex != null && wordId != -1) {
                int count = 0;
                for (int documentId : invertedIndex.keySet()) {
                    if (invertedIndex.get(documentId) > 0) {
                        count++;
                    }
                }
                df.put(wordId, count);
            }
            else {
                df.put(wordId, 0);
            }
        }
        return df;
    }

    /**
     * Get the inverse document frequency (IDF) of the query.
     * It is calculated as the logarithm (base 2) of the total number of documents divided by the
     * document frequency (DF) of the query.
     * @param query the query to be calculated
     * @param title whether the query is for the title or the body
     * @return the inverse document frequency (IDF) of the query
     * @throws IOException if there is an error in the database
     */
    private HashMap<Integer, Double> getIDF(Vector<String> query,
                                            boolean title)
            throws IOException {
        HashMap<Integer, Integer> df = getDF(query, title);
        HashMap<Integer, Double> idf = new HashMap<>();
        for (int wordId : df.keySet()) {
            if (df.get(wordId) != 0) {
                double idfValue = Math.log((double) totalNumberOfDocuments / (df.get(wordId)))
                        / Math.log(2);
                idf.put(wordId, idfValue);
            }
            else {
                idf.put(wordId, 0.0);
            }
        }
        return idf;
    }

    /**
     * Finalize all the databases.
     */
    public void finalizeAllDatabases() {
        nodePropertyDatabase.finish();
        titleInvertedIndexDatabase.finish();
        bodyInvertedIndexDatabase.finish();
        titleForwardIndexDatabase.finish();
        bodyForwardIndexDatabase.finish();
        wordIdDatabase.finish();
    }

}
