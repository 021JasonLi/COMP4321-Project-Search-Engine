package Indexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

/**
 * A stop word removal class to remove stop words from a list of words.
 */
public class StopWordRemoval {
    private final static String STOP_WORD_LIST_DIRECTORY = "./src/main/resources/stopwords.txt";
    private final HashSet<String> stopWords;

    /**
     * Create a stop word removal instance. Read the stop word list from the file.
     * @throws IOException If an I/O error occurs when reading the stop word list.
     */
    public StopWordRemoval() throws IOException {
        stopWords = new HashSet<>();
        FileReader fr = new FileReader(STOP_WORD_LIST_DIRECTORY);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            stopWords.add(line);
        }
        br.close();
    }

    /**
     * Remove stop words from a list of words.
     * @param words A list of words.
     * @return A list of words without stop words.
     */
    public Vector<String> removeStopWord(Vector<String> words) {
        Vector<String> result = new Vector<>();
        for (String word : words) {
            if (!stopWords.contains(word)) {
                result.add(word);
            }
        }
        return result;
    }

    public boolean notContainStopWord(String words) {
        return !stopWords.contains(words);
    }

}
