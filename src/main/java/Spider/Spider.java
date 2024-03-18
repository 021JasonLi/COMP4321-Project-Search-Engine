package Spider;

import Database.NodeLinkDatabase;
import Database.PageIdToUrlDatabase;
import Database.UrlToPageIdDatabase;
import org.htmlparser.beans.LinkBean;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * A web crawler to extract web content and store the results in databases.
 */
public class Spider {
    public final String url;
    public final int maxIndexPages;

    private final UrlToPageIdDatabase urlToPageIdDatabase;
    private final PageIdToUrlDatabase pageIdToUrlDatabase;
    private final NodeLinkDatabase parentToChildDatabase;
    private final NodeLinkDatabase childToParentDatabase;

    /**
     * Create a Spider instance.
     * @param url The URL to start the crawling.
     * @param maxIndexPages The maximum number of pages to crawl.
     * @throws IOException If an I/O error occurs when creating the databases.
     */
    public Spider(String url, int maxIndexPages) throws IOException {
        this.url = url;
        this.maxIndexPages = maxIndexPages;
        urlToPageIdDatabase = new UrlToPageIdDatabase(
                "urlToPageIdDatabase", "url");
        pageIdToUrlDatabase = new PageIdToUrlDatabase(
                "pageIdToUrlDatabase", "pageId");
        parentToChildDatabase = new NodeLinkDatabase(
                "parentToChildDatabase", "parent");
        childToParentDatabase = new NodeLinkDatabase(
                "childToParentDatabase", "child");
    }

    /**
     * Perform a breadth-first search on the web pages starting from the given URL
     * with at most specified number of pages,
     * and store the results in the databases.
     * @throws IOException If an I/O error occurs when crawling the web pages.
     */
    public void bfs() throws IOException{
        Queue<String> queue = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        HashMap<String, Vector<String>> childLink = new HashMap<>();
        queue.add(url);
        int count = 0;

        while (!queue.isEmpty() && count < maxIndexPages) {
            String url = queue.poll();
            if (!visited.contains(url)) {
                visited.add(url);
                // Convert URL to page ID and store in database
                urlToPageIdDatabase.addEntry(url);
                pageIdToUrlDatabase.addEntry(url);
                count++;
                // Extract links
                Vector<String> links = extractLinks(url);
                queue.addAll(links);
                childLink.put(url, links);
            }
        }
        constructParentChildDatabase(childLink); // Construct parent-child database

        urlToPageIdDatabase.finish();
        pageIdToUrlDatabase.finish();
        parentToChildDatabase.finish();
        childToParentDatabase.finish();
    }

    /**
     * Extract links from the given URL.
     * @param url The URL to extract links from.
     * @return A vector of links extracted from the given URL.
     */
    private Vector<String> extractLinks(String url) {
        LinkBean linkBean = new LinkBean();
        linkBean.setURL(url);
        URL[] urls = linkBean.getLinks();
        Vector<String> links = new Vector<>();
        for (URL link : urls) {
            links.add(link.toString());
        }
        return links;
    }

    /**
     * Construct parent-child database (bidirectional) from the parent-child link map.
     * @param childLink The parent-child link map.
     * @throws IOException If an I/O error occurs when accessing the databases.
     */
    private void constructParentChildDatabase(HashMap<String, Vector<String>> childLink) throws IOException {
        for (String parent : childLink.keySet()) {
            Vector<String> children = childLink.get(parent);
            int parentId = urlToPageIdDatabase.getEntry(parent);
            for (String child : children) {
                int childId = urlToPageIdDatabase.getEntry(child);
                if (childId != -1) {
                    parentToChildDatabase.addEntry(parentId, childId);
                    childToParentDatabase.addEntry(childId, parentId);
                }
            }
        }
    }

}
