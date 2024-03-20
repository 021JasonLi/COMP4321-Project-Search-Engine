import Database.NodeRelationDatabase;
import Database.NodePropertyDatabase;

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

            HashMap<Integer, HashMap<String, String>> properties = nodePropertyDatabase.getAllEntries();
            HashMap<Integer, HashSet<Integer>> childLinks = parentToChildDatabase.getAllEntries();

            for (int i = 0; i < properties.size(); i++) {
                printWriter.println("Page title: " + properties.get(i).get("title"));
                printWriter.println("URL: " + properties.get(i).get("url"));
                printWriter.print("Last modification date: " +
                        new Date(parseLong(properties.get(i).get("lastModified"))) + ", ");
                printWriter.println("Size of page: " + properties.get(i).get("size"));
                printWriter.println("Keyword frequency: TODO");
                for (int j = 0; j < MAX_CHILD_LINK_DISPLAY && j < childLinks.get(i).size(); j++) {
                    printWriter.println("Child link " + (j+1) + ": " +
                            nodePropertyDatabase.getUrl((int)childLinks.get(i).toArray()[j]));
                }
                printWriter.println(SEPARATOR);
            }

            nodePropertyDatabase.finish();
            parentToChildDatabase.finish();
            System.out.println("Output file generated: " + OUTPUT_FILE);
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

}
