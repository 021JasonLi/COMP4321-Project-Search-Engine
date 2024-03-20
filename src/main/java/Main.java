import Indexer.Indexer;
import Spider.Spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * The main class to start the web crawling and indexing.
 * The URL and the maximum number of pages to crawl are specified here.
 */
public class Main {
    public static final String URL = "https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm";
    public static final int MAX_INDEX_PAGES = 30;

    public static void main(String[] args) {
        try {
            Spider spider = new Spider(URL, MAX_INDEX_PAGES);
            ArrayList<HashMap<Integer, Vector<String>>> result = spider.bfs();
            Indexer indexer = new Indexer(result);
            indexer.index();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
