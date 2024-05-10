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

* Run the search function offline
```shell
java -cp "./WEB-INF/lib/*;./WEB-INF/classes;." SearchEngine.Retrieval
```

## Running the Search Engine

Copy the folder to the webapps folder of Tomcat. 
And set the environment variable `CATALINA_HOME` to the path of the Tomcat folder.

* Start up server
```shell
%CATALINA_HOME%/bin/startup.bat
```
Access the search engine in localhost: `http://localhost:8080/SearchEngine/index.html`

* Shut down server
```shell
%CATALINA_HOME%/bin/shutdown.bat
```
