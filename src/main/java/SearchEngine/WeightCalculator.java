package SearchEngine;

import Database.ForwardIndexDatabase;
import Database.InvertedIndexDatabase;
import Database.NodePropertyDatabase;
import Database.WordIdDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class WeightCalculator {
    private final NodePropertyDatabase nodePropertyDatabase;
    private final InvertedIndexDatabase titleInvertedIndexDatabase;
    private final InvertedIndexDatabase bodyInvertedIndexDatabase;
    private final ForwardIndexDatabase titleForwardIndexDatabase;
    private final ForwardIndexDatabase bodyForwardIndexDatabase;
    private final WordIdDatabase wordIdDatabase;

    private final int totalNumberOfDocuments;

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

    public void finalizeAllDatabases() {
        nodePropertyDatabase.finish();
        titleInvertedIndexDatabase.finish();
        bodyInvertedIndexDatabase.finish();
        titleForwardIndexDatabase.finish();
        bodyForwardIndexDatabase.finish();
        wordIdDatabase.finish();
    }

}
