package SearchEngine;

import Database.ForwardIndexDatabase;
import Database.NodePropertyDatabase;
import Database.NodeRelationDatabase;
import Database.WordIdDatabase;
import Indexer.Porter;
import Indexer.StopWordRemoval;

import java.io.IOException;
import java.util.*;

/**
 * The main class to start the retrieval process.
 */
public class Retrieval {
    private static final int MAX_RESULT = 50;
    private static final int MAX_KEYWORD_DISPLAY = 5;
    private static final int MAX_PARENT_LINK_DISPLAY = 5;
    private static final int MAX_CHILD_LINK_DISPLAY = 5;

    private final StopWordRemoval stopWordRemoval;
    private final Porter porter;
    private final WeightCalculator weightCalculator;

    private final NodePropertyDatabase nodePropertyDatabase;
    private final NodeRelationDatabase parentToChildDatabase;
    private final NodeRelationDatabase childToParentDatabase;
    private final ForwardIndexDatabase titleForwardIndexDatabase;
    private final ForwardIndexDatabase bodyForwardIndexDatabase;
    private final WordIdDatabase wordIdDatabase;

    /**
     * Constructor to initialize the stop word removal, stemming, and weight calculator.
     * @throws IOException if an I/O error occurs
     */
    public Retrieval() throws IOException {
        stopWordRemoval = new StopWordRemoval();
        porter = new Porter();
        weightCalculator = new WeightCalculator();
        nodePropertyDatabase = new NodePropertyDatabase(
                "nodePropertyDatabase", "property");
        parentToChildDatabase = new NodeRelationDatabase(
                "parentToChildDatabase", "parent");
        childToParentDatabase = new NodeRelationDatabase(
                "childToParentDatabase", "child");
        titleForwardIndexDatabase = new ForwardIndexDatabase(
                "titleForwardIndexDatabase", "forwardIndex");
        bodyForwardIndexDatabase = new ForwardIndexDatabase(
                "bodyForwardIndexDatabase", "forwardIndex");
        wordIdDatabase = new WordIdDatabase(
                "wordIdDatabase", "word");
    }

    /**
     * Search the query and display the results.
     * @param query the query to be searched
     * @throws IOException if an I/O error occurs
     */
    public ArrayList<HashMap<String, String>> search(String query)
            throws IOException {
        Vector<String> queryVector = extractQuery(query);
        HashMap<Integer, Double> results = evaluateQuery(queryVector);
        return formatResults(results);
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

    private ArrayList<HashMap<String, String>> formatResults(
            HashMap<Integer, Double> results) throws IOException {
        ArrayList<HashMap<String, String>> formattedResults = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : results.entrySet()) {
            HashMap<String, String> formattedResult = new HashMap<>();
            int pageId = entry.getKey();
            double score = entry.getValue();
            // Page Properties
            formattedResult.put("score", String.format("%.2f", score));
            formattedResult.put("title", nodePropertyDatabase.getEntry(pageId).get("title"));
            formattedResult.put("url", nodePropertyDatabase.getEntry(pageId).get("url"));
            formattedResult.put("lastModified",
                    nodePropertyDatabase.getEntry(pageId).get("lastModified"));
            formattedResult.put("size", nodePropertyDatabase.getEntry(pageId).get("size"));
            // Keywords frequency
            HashMap<Integer, Integer> keywords = mergeKeywordMap(
                    titleForwardIndexDatabase.getEntry(pageId),
                    bodyForwardIndexDatabase.getEntry(pageId));
            StringBuilder keywordString = new StringBuilder();
            for (int j = 0; j < MAX_KEYWORD_DISPLAY && j < keywords.size(); j++) {
                if (j != 0) {
                    keywordString.append("; ");
                }
                int index = (int)keywords.keySet().toArray()[j];
                String keyword = wordIdDatabase.getWord(index);
                int frequency = keywords.get(index);
                keywordString.append(keyword).append(" ").append(frequency);
            }
            formattedResult.put("keywords", keywordString.toString());
            // Parent links
            StringBuilder parentLinksString = new StringBuilder();
            HashSet<Integer> parentLinks = childToParentDatabase.getEntry(pageId);
            if (parentLinks == null || parentLinks.isEmpty()) {
                parentLinksString.append("No parent links.");
            }
            else {
                for (int j = 0; j < MAX_PARENT_LINK_DISPLAY && j < parentLinks.size(); j++) {
                    if (j != 0) {
                        parentLinksString.append(" ");
                    }
                    parentLinksString.append(nodePropertyDatabase.getUrl(
                            (int) parentLinks.toArray()[j]));
                }
            }
            formattedResult.put("parentLinks", parentLinksString.toString());
            // Child Links
            StringBuilder childLinksString = new StringBuilder();
            HashSet<Integer> childLinks = parentToChildDatabase.getEntry(pageId);
            if (childLinks == null || childLinks.isEmpty()) {
                childLinksString.append("No child links.");
            }
            else {
                for (int j = 0; j < MAX_CHILD_LINK_DISPLAY && j < childLinks.size(); j++) {
                    if (j != 0) {
                        childLinksString.append(" ");
                    }
                    childLinksString.append(nodePropertyDatabase.getUrl(
                            (int) childLinks.toArray()[j]));
                }
            }
            formattedResult.put("childLinks", childLinksString.toString());
            formattedResults.add(formattedResult);
        }
        return formattedResults;
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

    private void finalizeAllDatabases() {
        nodePropertyDatabase.finish();
        parentToChildDatabase.finish();
        childToParentDatabase.finish();
        titleForwardIndexDatabase.finish();
        bodyForwardIndexDatabase.finish();
        wordIdDatabase.finish();
    }

    private static HashMap<Integer, Integer> mergeKeywordMap(
            HashMap<Integer, Integer> map1,
            HashMap<Integer, Integer> map2) {
        HashMap<Integer, Integer> result = new HashMap<>();
        for (Integer key : map1.keySet()) {
            result.put(key, map1.get(key));
        }
        for (Integer key : map2.keySet()) {
            result.merge(key, map2.get(key), Integer::sum);
        }
        return result;
    }

}
