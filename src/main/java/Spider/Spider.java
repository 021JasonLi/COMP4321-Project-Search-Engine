package Spider;

import org.htmlparser.beans.LinkBean;

import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class Spider {
    public static final String URL = "https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm";
    public static final int MAX_INDEX_PAGES = 30;

    public Spider() {

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

    public void bfs() {
        Queue<String> queue = new LinkedList<>();
        queue.add(URL);
        HashSet<String> visited = new HashSet<>();
        int count = 0;

        while (!queue.isEmpty() && count < MAX_INDEX_PAGES) {
            String url = queue.poll();
            if (!visited.contains(url)) {
                visited.add(url);
                count++;
                System.out.println("Visited: " + url);
                // Extract links
                Vector<String> links = extractLinks(url);
                queue.addAll(links);

            }

        }
    }

    public static void main(String[] args) {
        Spider spider = new Spider();
        spider.bfs();
    }
}
