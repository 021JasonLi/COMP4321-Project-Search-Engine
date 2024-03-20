import Database.ForwardIndexDatabase;
import Database.NodeRelationDatabase;
import Database.NodePropertyDatabase;
import Database.WordIdDatabase;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import static java.lang.Long.parseLong;

/**
 * A test program to read data from the jdbm and outputs a plain-text file.
 */
public class Tester {
    public static final String OUTPUT_FILE = "spider_result.txt";
    public static final int MAX_KEYWORD_DISPLAY = 10;
    public static final int MAX_CHILD_LINK_DISPLAY = 10;
    public static final String SEPARATOR = "----------------------------------------";

    public static void main(String[] args) {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE);
             PrintWriter printWriter = new PrintWriter(writer)) {
            NodePropertyDatabase nodePropertyDatabase = new NodePropertyDatabase(
                    "nodePropertyDatabase", "property");
            NodeRelationDatabase parentToChildDatabase = new NodeRelationDatabase(
                    "parentToChildDatabase", "parent");
            ForwardIndexDatabase titleForwardIndexDatabase = new ForwardIndexDatabase(
                    "titleForwardIndexDatabase",
                    "forwardIndex", false);
            ForwardIndexDatabase bodyForwardIndexDatabase = new ForwardIndexDatabase(
                    "bodyForwardIndexDatabase",
                    "forwardIndex", false);
            WordIdDatabase wordIdDatabase = new WordIdDatabase(
                    "wordIdDatabase", "word");

            HashMap<Integer, HashMap<String, String>> properties = nodePropertyDatabase.getAllEntries();
            HashMap<Integer, HashSet<Integer>> childLinks = parentToChildDatabase.getAllEntries();
            HashMap<Integer, HashMap<Integer, Integer>> keywords = mergeKeywordMap(
                    titleForwardIndexDatabase.getAllEntries(),
                    bodyForwardIndexDatabase.getAllEntries());

            for (int i = 0; i < properties.size(); i++) {
                // Properties of a page
                printWriter.println(properties.get(i).get("title"));
                printWriter.println(properties.get(i).get("url"));
                printWriter.print(new Date(parseLong(properties.get(i).get("lastModified"))) + ", ");
                printWriter.println(properties.get(i).get("size"));
                // Keyword frequency
                for (int j = 0; j < MAX_KEYWORD_DISPLAY && j < keywords.get(i).size(); j++) {
                    if (j != 0) {
                        printWriter.print("; ");
                    }
                    int index = (int)keywords.get(i).keySet().toArray()[j];
                    String keyword = wordIdDatabase.getWord(index);
                    int frequency = keywords.get(i).get(index);
                    printWriter.print(keyword + " " + frequency);
                }
                printWriter.println();
                // Child links
                for (int j = 0; j < MAX_CHILD_LINK_DISPLAY && j < childLinks.get(i).size(); j++) {
                    printWriter.println(
                            nodePropertyDatabase.getUrl((int)childLinks.get(i).toArray()[j]));
                }
                printWriter.println(SEPARATOR);
            }

            nodePropertyDatabase.finish();
            parentToChildDatabase.finish();
            titleForwardIndexDatabase.finish();
            bodyForwardIndexDatabase.finish();
            wordIdDatabase.finish();
            System.out.println("Output file generated: " + OUTPUT_FILE);
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

    /**
     * Merge the title keyword frequency map and the body keyword frequency map.
     * This is to display the keyword frequency of a page.
     * @param map1 The title keyword frequency map.
     * @param map2 The body keyword frequency map.
     * @return The merged keyword frequency map.
     */
    private static HashMap<Integer, HashMap<Integer, Integer>> mergeKeywordMap(
            HashMap<Integer, HashMap<Integer, Integer>> map1,
            HashMap<Integer, HashMap<Integer, Integer>> map2) {
        HashMap<Integer, HashMap<Integer, Integer>> result = new HashMap<>();
        for (Integer key : map1.keySet()) {
            result.put(key, map1.get(key));
        }
        for (Integer key : map2.keySet()) {
            if (result.containsKey(key)) {
                HashMap<Integer, Integer> record = result.get(key);
                for (Integer wordId : map2.get(key).keySet()) {
                    record.merge(wordId, map2.get(key).get(wordId), Integer::sum);
                }
                result.put(key, record);
            }
            else {
                result.put(key, map2.get(key));
            }
        }
        return result;
    }

}
