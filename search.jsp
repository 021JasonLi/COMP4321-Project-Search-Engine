<%@ page import="java.util.*" %>
<%@ page import="SearchEngine.Retrieval" %>

<html>
<head>
<title>Search Results</title>
<style>
    td {
        vertical-align: top;
    }
</style>
</head>

<body>
    <% 
        String query = request.getParameter("query");
    %>
    <h1>Search Results</h1>
    <form method="get" action="search.jsp">
        <input type="text" name="query" placeholder="Search for..." value="<%= query %>">
        <input type="submit" value="Search">
    </form>
    <%
        if (query == "") {
            out.println("Please enter a query.");
        }
        else {
            Retrieval retrieval = new Retrieval();
            ArrayList<HashMap<String, String>> results = retrieval.search(query);
            out.println("Found " + results.size() + " results.");
            out.println("<table>");
            for (HashMap<String, String> result : results) {
                String score = result.get("score");
                String title = result.get("title");
                String url = result.get("url");
                String lastModified = result.get("lastModified");
                String size = result.get("size");
                String keywords = result.get("keywords");
                String parentLinks = result.get("parentLinks");
                String childLinks = result.get("childLinks");
                String[] parentLinksArray = parentLinks.split(" ");
                String[] childLinksArray = childLinks.split(" ");

                out.println("<tr>");
                out.println("<td width='5%'>" + score + "</td>");
                out.println("<td>" + title + "<br> <a href='" + url + "'>" + url + "</a> <br>");
                out.println(lastModified + ", " + size + " Bytes <br>");
                out.println("Keywords: " + keywords + "<br>");
                if (parentLinksArray[0] == "") {
                    out.println("No parent links. <br>");
                }
                else {
                    out.println("Parent Links: <br>");
                    out.println("<ul>");
                    for (String parentLink : parentLinksArray) {
                        out.println("<li> <a href='" + parentLink + "'>" + parentLink + "</a> </li>");
                    }
                    out.println("</ul>");
                }
                if (childLinksArray[0] == "") {
                    out.println("No child links. <br>");
                }
                else {
                    out.println("Child Links: <br>");
                    out.println("<ul>");
                    for (String childLink : childLinksArray) {
                        out.println("<li> <a href='" + childLink + "'>" + childLink + "</a> </li>");
                    }
                    out.println("</ul>");
                }
                out.println("</td></tr>");
                
            }
            out.println("</table>");
            retrieval.finalizeAllDatabases();
        }
    %>
</body>
</html>