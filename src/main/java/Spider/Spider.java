package Spider;

import Database.URLDatabase.PageIdToUrlDatabase;
import Database.URLDatabase.URLDatabase;
import Database.URLDatabase.UrlToPageIdDatabase;
import org.htmlparser.beans.LinkBean;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class Spider {
    public final String url;
    public final int maxIndexPages;

    private final URLDatabase urlToPageIdDatabase;
    private final URLDatabase pageIdToUrlDatabase;

    public Spider(String url, int maxIndexPages) throws IOException {
        this.url = url;
        this.maxIndexPages = maxIndexPages;
        urlToPageIdDatabase = new UrlToPageIdDatabase("urlToPageIdDatabase", "url");
        pageIdToUrlDatabase = new PageIdToUrlDatabase("pageIdToUrlDatabase", "pageId");
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

    public void bfs() throws IOException{
        Queue<String> queue = new LinkedList<>();
        queue.add(url);
        HashSet<String> visited = new HashSet<>();
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

            }
        }

        urlToPageIdDatabase.finish();
        pageIdToUrlDatabase.finish();
    }

}
