import Spider.Spider;

public class Main {
    public static final String URL = "https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm";
    public static final int MAX_INDEX_PAGES = 30;

    public static void main(String[] args) {
        Spider spider = new Spider(URL, MAX_INDEX_PAGES);
        try {
            spider.bfs();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
