package SearchEngine;

import Indexer.Porter;
import Indexer.StopWordRemoval;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class Retrieval {
    private static final int MAX_RESULT = 50;

    private final StopWordRemoval stopWordRemoval;
    private final Porter porter;
    private final WeightCalculator weightCalculator;

    public Retrieval() throws IOException {
        stopWordRemoval = new StopWordRemoval();
        porter = new Porter();
        weightCalculator = new WeightCalculator();
    }

    public static void main(String[] args) {
        try {
            Retrieval retrieval = new Retrieval();
            System.out.print("Enter your query: ");
            Vector<String> query = retrieval.extractQuery(System.console().readLine());
            System.out.println("Your query: " + query);
            HashMap<Integer, HashMap<Integer, Double>> titleTermWeight =
                    retrieval.weightCalculator.getTermWeight(query, true);
            HashMap<Integer, HashMap<Integer, Double>> bodyTermWeight =
                    retrieval.weightCalculator.getTermWeight(query, false);
//            System.out.println("Title term weight: " + titleTermWeight);
            System.out.println("Body term weight: " + bodyTermWeight);
            HashMap<Integer, Integer> queryWeight = retrieval.weightCalculator.getQueryWeight(query);
            System.out.println("Query weight: " + queryWeight);
            retrieval.weightCalculator.finalizeAllDatabases();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public Vector<String> extractQuery(String query) {
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

}
