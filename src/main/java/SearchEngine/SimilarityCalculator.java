package SearchEngine;

import java.util.HashMap;

public class SimilarityCalculator {
    private static final double TITLE_WEIGHT = 0.6;
    private static final double BODY_WEIGHT = 0.4;

    public static HashMap<Integer, Double> getSimilarityScore(
            HashMap<Integer, HashMap<Integer, Double>> titleTermWeight,
            HashMap<Integer, HashMap<Integer, Double>> bodyTermWeight,
            HashMap<Integer, Integer> queryWeight) {
        HashMap<Integer, Double> titleSimilarity = getCosineSimilarity(titleTermWeight, queryWeight);
        HashMap<Integer, Double> bodySimilarity = getCosineSimilarity(bodyTermWeight, queryWeight);
        HashMap<Integer, Double> similarityScore = new HashMap<>();
        for (int pageId : titleSimilarity.keySet()) {
            double score = TITLE_WEIGHT * titleSimilarity.get(pageId) +
                    BODY_WEIGHT * bodySimilarity.get(pageId);
            similarityScore.put(pageId, score);
        }
        return similarityScore;
    }

    public static HashMap<Integer, Double> getCosineSimilarity(
            HashMap<Integer, HashMap<Integer, Double>> termWeight,
            HashMap<Integer, Integer> queryWeight) {
        HashMap<Integer, Double> cosineSimilarity = new HashMap<>();
        for (int pageId : termWeight.keySet()) {
            HashMap<Integer, Double> termWeightValue = termWeight.get(pageId);
            double dotProduct = 0.0;
            double queryWeightNorm = 0.0;
            double termWeightNorm = 0.0;
            for (int wordId : termWeightValue.keySet()) {
                dotProduct += termWeightValue.get(wordId) * queryWeight.get(wordId);
                queryWeightNorm += Math.pow(queryWeight.get(wordId), 2);
                termWeightNorm += Math.pow(termWeightValue.get(wordId), 2);
            }
            queryWeightNorm = Math.sqrt(queryWeightNorm);
            termWeightNorm = Math.sqrt(termWeightNorm);
            if (queryWeightNorm == 0 || termWeightNorm == 0) {
                cosineSimilarity.put(pageId, 0.0);
            }
            else {
                double similarity = dotProduct / (queryWeightNorm * termWeightNorm);
                cosineSimilarity.put(pageId, similarity);
            }
        }
        return cosineSimilarity;
    }

}
