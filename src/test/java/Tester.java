import Database.URLDatabase.PageIdToUrlDatabase;
import Database.URLDatabase.UrlToPageIdDatabase;

public class Tester {
    public static void main(String[] args) {
        System.out.println("Testing URL to Page ID Indexer");
        try {
            UrlToPageIdDatabase urlToPageIdIndexer = new UrlToPageIdDatabase(
                    "urlToPageIdDatabase", "url");
            urlToPageIdIndexer.printDbInfo();
            urlToPageIdIndexer.finish();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        System.out.println("Testing Page ID to URL Indexer");
        try {
            PageIdToUrlDatabase pageIdToUrlDatabase = new PageIdToUrlDatabase(
                    "pageIdToUrlDatabase", "pageId");
            pageIdToUrlDatabase.printDbInfo();
            pageIdToUrlDatabase.finish();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

}
