import Database.NodeRelationDatabase;
import Database.NodePropertyDatabase;
import Database.UrlToPageIdDatabase;

public class Tester {
    public static void main(String[] args) {
        System.out.println("Testing URL to Page ID Database");
        try {
            UrlToPageIdDatabase urlToPageIdIndexer = new UrlToPageIdDatabase(
                    "urlToPageIdDatabase", "url");
            urlToPageIdIndexer.printDbInfo();
            urlToPageIdIndexer.finish();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        System.out.println("Testing Node Property Database");
        try {
            NodePropertyDatabase nodePropertyDatabase = new NodePropertyDatabase(
                    "nodePropertyDatabase", "property");
            nodePropertyDatabase.printDbInfo();
            nodePropertyDatabase.finish();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        System.out.println("Testing Parent to Child Database");
        try {
            NodeRelationDatabase parentToChildDatabase = new NodeRelationDatabase(
                    "parentToChildDatabase", "parent");
            parentToChildDatabase.printDbInfo();
            parentToChildDatabase.finish();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        System.out.println("Testing Child to Parent Database");
        try {
            NodeRelationDatabase childToParentDatabase = new NodeRelationDatabase(
                    "childToParentDatabase", "child");
            childToParentDatabase.printDbInfo();
            childToParentDatabase.finish();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

}
