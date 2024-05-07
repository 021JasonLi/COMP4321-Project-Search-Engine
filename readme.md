# COMP4321 Project Group 44

The project contains spider and indexer to crawl and index webpages, 
and search function and interface to search the indexed webpages.

## Development Environment and Tools

This project uses Java 17 to develop. The OS used in the development is Windows.
Tomcat with version 10.1.18 is used to host the search engine.

HTML parser and JDBM libraries (provided in labs) are also used and included in the folder.

## Building Project 

In the root directory of the folder:

* Compile the project 
```shell
javac -cp ./WEB-INF/lib/* "@sources.txt"
```

* Run the crawler
```shell
java -cp "./WEB-INF/lib/*;./WEB-INF/classes;." Main
```

* Run the tester for crawler
```shell
java -cp "./WEB-INF/lib/*;./WEB-INF/classes;." Tester
```

* Start up server
```shell
%CATALINA_HOME%\bin\startup.bat
C:\UST\23-24_2_Spring\COMP4321\Project\apache-tomcat-10.1.18\bin\startup.bat
http://localhost:8080/Search-Engine/index.html
```

* Access the search engine in localhost
```shell
http://localhost:8080/SearchEngine/index.html
```

* Shut down server
```shell
C:\UST\23-24_2_Spring\COMP4321\Project\apache-tomcat-10.1.18\bin\shutdown.bat
```
