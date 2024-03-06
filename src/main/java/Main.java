import Spider.Spider;

public class Main {
    public static void main(String[] args) {
        Spider spider = new Spider();
        try {
            spider.bfs();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
