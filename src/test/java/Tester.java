import Database.NodeLinkDatabase;
import Database.PageIdToUrlDatabase;
import Database.UrlToPageIdDatabase;

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

        System.out.println("Testing Parent to Child Database");
        try {
            NodeLinkDatabase parentToChildDatabase = new NodeLinkDatabase(
                    "parentToChildDatabase", "parent");
            parentToChildDatabase.printDbInfo();
            parentToChildDatabase.finish();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        System.out.println("Testing Child to Parent Database");
        try {
            NodeLinkDatabase childToParentDatabase = new NodeLinkDatabase(
                    "childToParentDatabase", "child");
            childToParentDatabase.printDbInfo();
            childToParentDatabase.finish();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

}
