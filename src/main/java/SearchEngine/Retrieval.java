package SearchEngine;

import Indexer.Porter;
import Indexer.StopWordRemoval;

import java.io.IOException;
import java.util.*;

/**
 * The main class to start the retrieval process.
 */
public class Retrieval {
    private static final int MAX_RESULT = 50;

    private final StopWordRemoval stopWordRemoval;
    private final Porter porter;
    private final WeightCalculator weightCalculator;

    /**
     * Constructor to initialize the stop word removal, stemming, and weight calculator.
     * @throws IOException if an I/O error occurs
     */
    public Retrieval() throws IOException {
        stopWordRemoval = new StopWordRemoval();
        porter = new Porter();
        weightCalculator = new WeightCalculator();
    }

    /**
     * The main method to start the retrieval process.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Retrieval retrieval = new Retrieval();
            System.out.print("Enter your query: ");
            Vector<String> query = retrieval.extractQuery(System.console().readLine());
            System.out.println("Your query: " + query);
            HashMap<Integer, Double> results = retrieval.evaluateQuery(query);
            System.out.println("Results: " + results);
            retrieval.weightCalculator.finalizeAllDatabases();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Extract the query and remove stop words and stemming.
     * @param query the query to be extracted
     * @return the extracted query represented as a vector of strings
     */
    private Vector<String> extractQuery(String query) {
        Vector<String> result = new Vector<>();
        String[] keywords = query.split(" ");
        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].startsWith("\"")) {
                int j = i;
                while (!keywords[j].endsWith("\"")) {
                    j++;
                }
                StringBuilder keywordPhrase = new StringBuilder();
                for (int k = i; k <= j; k++) {
                    keywords[k] = porter.removeSymbols(keywords[k]);
                    if (stopWordRemoval.notStopWord(keywords[k])) {
                        keywordPhrase.append(porter.stripAffixes(keywords[k]));
                        if (k != j) {
                            keywordPhrase.append(" ");
                        }
                    }
                }
                i = j;
                result.add(keywordPhrase.toString());
            } else {
                String keyword = porter.removeSymbols(keywords[i]);
                if (stopWordRemoval.notStopWord(keyword)) {
                    result.add(porter.stripAffixes(keyword));
                }
            }
        }
        return result;
    }

    /**
     * Evaluate the query and return the similarity score of each document.
     * @param query the query to be evaluated
     * @return the similarity score of each document
     * @throws IOException if an I/O error occurs
     */
    private HashMap<Integer, Double> evaluateQuery(Vector<String> query)
            throws IOException {
        HashMap<Integer, HashMap<Integer, Double>> titleTermWeight =
                weightCalculator.getTermWeight(query, true);
        HashMap<Integer, HashMap<Integer, Double>> bodyTermWeight =
                weightCalculator.getTermWeight(query, false);
        HashMap<Integer, Integer> queryWeight = weightCalculator.getQueryWeight(query);
        HashMap<Integer, Double> similarityScore = SimilarityCalculator.getSimilarityScore(
                titleTermWeight, bodyTermWeight, queryWeight);
        return sortSimilarityScore(similarityScore);

    }

    /**
     * Sort the similarity score in descending order and return the top 50 results.
     * @param similarityScore the similarity score to be sorted
     * @return the sorted top 50 (or less) results in descending order
     */
    private HashMap<Integer, Double> sortSimilarityScore(
            HashMap<Integer, Double> similarityScore) {
        List<Map.Entry<Integer, Double>> list = new LinkedList<>(similarityScore.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        list.removeIf(entry -> entry.getValue() == 0.0);
        while (list.size() > MAX_RESULT) {
            list.remove(list.size() - 1);
        }
        HashMap<Integer, Double> results = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : list) {
            results.put(entry.getKey(), entry.getValue());
        }
        return results;
    }


}
