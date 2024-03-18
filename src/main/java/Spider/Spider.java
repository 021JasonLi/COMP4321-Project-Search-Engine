package Spider;

import Database.NodeRelationDatabase;
import Database.NodePropertyDatabase;
import Database.UrlToPageIdDatabase;
import org.htmlparser.Parser;
import org.htmlparser.beans.LinkBean;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * A web crawler to extract web content and store the results in databases.
 */
public class Spider {
    public final String url;
    public final int maxIndexPages;

    private final UrlToPageIdDatabase urlToPageIdDatabase;
    private final NodePropertyDatabase nodePropertyDatabase;
    private final NodeRelationDatabase parentToChildDatabase;
    private final NodeRelationDatabase childToParentDatabase;

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
        nodePropertyDatabase = new NodePropertyDatabase(
                "nodePropertyDatabase", "property");
        parentToChildDatabase = new NodeRelationDatabase(
                "parentToChildDatabase", "parent");
        childToParentDatabase = new NodeRelationDatabase(
                "childToParentDatabase", "child");
    }

    /**
     * Perform a breadth-first search on the web pages starting from the given URL
     * with at most specified number of pages,
     * and store the results in the databases.
     * @throws IOException If an I/O error occurs when crawling the web pages.
     */
    public void bfs() throws IOException, ParserException {
        Queue<String> queue = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        HashMap<String, Vector<String>> childLink = new HashMap<>();
        queue.add(url);
        int count = 0;

        while (!queue.isEmpty() && count < maxIndexPages) {
            String url = queue.poll();
            if (!visited.contains(url)) {
                visited.add(url);
                count++;
                // Convert URL to page ID and store in database
                int id = urlToPageIdDatabase.addEntry(url);
                // Extract properties and store in database
                HashMap<String, String> properties = extractProperties(url);
                nodePropertyDatabase.addEntry(id, properties);
                // Extract links
                Vector<String> links = extractLinks(url);
                queue.addAll(links);
                childLink.put(url, links);
            }
        }
        constructParentChildDatabase(childLink); // Construct parent-child database

        urlToPageIdDatabase.finish();
        nodePropertyDatabase.finish();
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
     * Extract properties, including page title, last modified date and size of page,
     * from the given URL.
     * @param url The URL to extract properties from.
     * @return A map of properties extracted from the given URL.
     * @throws ParserException If an error occurs when parsing the URL.
     * @throws IOException If an I/O error occurs when accessing the URL.
     */
    private HashMap<String, String> extractProperties(String url)
            throws ParserException, IOException {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("url", url);

        URLConnection connection = new URL(url).openConnection();
        Parser parser = new Parser(connection);
        TagNameFilter titleFilter = new TagNameFilter("title");
        String title = parser.parse(titleFilter).asString();
        properties.put("title", title);

        long lastModified = connection.getLastModified();
        if (lastModified == 0) {
            properties.put("lastModified", "unknown");
        } else {
            properties.put("lastModified", new Date(lastModified).toString());
        }

        long size = connection.getContentLengthLong();
        if (size == -1) {
            properties.put("size", "unknown");
        } else if (size < 1024) {
            String sizeString = size + " Bytes";
            properties.put("size", sizeString);
        } else {
            String sizeString = String.format("%.2f KB", size / 1024.0);
            properties.put("size", sizeString);
        }

        return properties;
    }

    /**
     * Construct parent-child database (bidirectional) from the parent-child link map.
     * @param childLink The parent-child link map.
     * @throws IOException If an I/O error occurs when accessing the databases.
     */
    private void constructParentChildDatabase(HashMap<String, Vector<String>> childLink)
            throws IOException {
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
