import Database.URLDatabase.PageIdToUrlDatabase;
import Database.URLDatabase.URLDatabase;
import Database.URLDatabase.UrlToPageIdDatabase;

public class Tester {
    public static void main(String[] args) {
        System.out.println("Testing URL to Page ID Indexer");
        URLDatabase urlToPageIdIndexer = new UrlToPageIdDatabase("urlToPageIdIndexer", "url");
        try {
            urlToPageIdIndexer.printDbInfo();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        urlToPageIdIndexer.finish();

        System.out.println("Testing Page ID to URL Indexer");
        URLDatabase pageIdToUrlDatabase = new PageIdToUrlDatabase("pageIdToUrlIndexer", "pageId");
        try {
            pageIdToUrlDatabase.printDbInfo();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        pageIdToUrlDatabase.finish();
    }

}
