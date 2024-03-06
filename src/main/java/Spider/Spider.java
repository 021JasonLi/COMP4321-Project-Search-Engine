package Spider;

import Indexer.PageIdToUrlIndexer;
import Indexer.URLIndexer;
import Indexer.UrlToPageIdIndexer;
import org.htmlparser.beans.LinkBean;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class Spider {
    public static final String URL = "https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm";
    public static final int MAX_INDEX_PAGES = 30;

    private final URLIndexer urlToPageIdIndexer;
    private final URLIndexer pageIdToUrlIndexer;

    public Spider() {
        urlToPageIdIndexer = new UrlToPageIdIndexer("urlToPageIdIndexer", "url");
        pageIdToUrlIndexer = new PageIdToUrlIndexer("pageIdToUrlIndexer", "pageId");
    }

    private Vector<String> extractLinks(String url) throws ParserException {
        LinkBean linkBean = new LinkBean();
        linkBean.setURL(url);
        URL[] urls = linkBean.getLinks();
        Vector<String> links = new Vector<>();
        for (URL link : urls) {
            links.add(link.toString());
        }
        return links;
    }

    public void bfs() throws IOException, ParserException{
        Queue<String> queue = new LinkedList<>();
        queue.add(URL);
        HashSet<String> visited = new HashSet<>();
        int count = 0;

        while (!queue.isEmpty() && count < MAX_INDEX_PAGES) {
            String url = queue.poll();
            if (!visited.contains(url)) {
                visited.add(url);
                count++;
                urlToPageIdIndexer.addEntry(url);
                pageIdToUrlIndexer.addEntry(url);
                System.out.println("Visited: " + url);
                // Extract links
                Vector<String> links = extractLinks(url);
                queue.addAll(links);

            }
        }

        urlToPageIdIndexer.finish();
        pageIdToUrlIndexer.finish();
    }

}
