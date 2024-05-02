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
    private final static String STOP_WORD_LIST_DIRECTORY =
            "./src/main/resources/stopwords.txt";
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
            if (!stopWords.contains(word.toLowerCase())) {
                result.add(word);
            }
        }
        return result;
    }

    /**
     * Check if a list of words contains any stop word.
     * @param words A list of words to be checked.
     * @return True if the list of words does not contain any stop word.
     * False if it contains any stop word.
     */
    public boolean notContainStopWord(Vector<String> words) {
        for (String word : words) {
            if (stopWords.contains(word.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a word is a stop word.
     * @param word A word to be checked.
     * @return True if the word is not a stop word. False otherwise.
     */
    public boolean notStopWord(String word) {
        return !stopWords.contains(word.toLowerCase());
    }

}
