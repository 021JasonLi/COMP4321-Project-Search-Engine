package Indexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

public class StopWordRemoval {
    private final static String STOP_WORD_LIST_DIRECTORY = "./src/main/resources/stopwords.txt";
    private final HashSet<String> stopWords;

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

    public Vector<String> removeStopWord(Vector<String> words) {
        words.removeIf(stopWords::contains);
        return words;
    }

}
