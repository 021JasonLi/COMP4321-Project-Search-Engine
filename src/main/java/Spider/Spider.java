package Spider;

import Database.NodeDatabase.ChildToParentNodeDatabase;
import Database.NodeDatabase.ParentToChildNodeDatabase;
import Database.URLDatabase.PageIdToUrlDatabase;
import Database.URLDatabase.UrlToPageIdDatabase;
import org.htmlparser.beans.LinkBean;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Spider {
    public final String url;
    public final int maxIndexPages;

    private final UrlToPageIdDatabase urlToPageIdDatabase;
    private final PageIdToUrlDatabase pageIdToUrlDatabase;
    private final ParentToChildNodeDatabase parentToChildDatabase;
    private final ChildToParentNodeDatabase childToParentDatabase;

    public Spider(String url, int maxIndexPages) throws IOException {
        this.url = url;
        this.maxIndexPages = maxIndexPages;
        urlToPageIdDatabase = new UrlToPageIdDatabase("urlToPageIdDatabase", "url");
        pageIdToUrlDatabase = new PageIdToUrlDatabase("pageIdToUrlDatabase", "pageId");
        parentToChildDatabase = new ParentToChildNodeDatabase("parentToChildDatabase", "parent");
        childToParentDatabase = new ChildToParentNodeDatabase("childToParentDatabase", "child");
    }

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
                // Convert URL to page ID
                urlToPageIdDatabase.addEntry(url);
                pageIdToUrlDatabase.addEntry(url);
                count++;
                // Extract links
                Vector<String> links = extractLinks(url);
                queue.addAll(links);
                childLink.put(url, links);
            }
        }
        constructParentChildDatabase(childLink);

        urlToPageIdDatabase.finish();
        pageIdToUrlDatabase.finish();
    }

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

    private void constructParentChildDatabase(HashMap<String, Vector<String>> childLink) {

    }

}
