# COMP4321 Project Phase 1 Group 44

The project phase 1 contains spider and indexer to crawl and index webpages.

## Development Environment and Tools

This project uses Java 17 to develop. The OS used in the development is Windows.

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

* Run the search engine (offline)
```shell
java -cp "./WEB-INF/lib/*;./WEB-INF/classes;." SearchEngine.Retrieval
```

* Start up server
```shell
%CATALINA_HOME%\bin\startup.bat
C:\UST\23-24_2_Spring\COMP4321\Project\apache-tomcat-10.1.18\bin\startup.bat
http://localhost:8080/Search-Engine/index.html
```

* Shut down server
```shell
C:\UST\23-24_2_Spring\COMP4321\Project\apache-tomcat-10.1.18\bin\shutdown.bat
```

## Folders

* `./db` contains the db files generated in the spider and indexer.
* `./libs` contains the libraries used in the project.
* `./src/main` contains the source codes of the spider and indexer.
* `./src/test` contains the tester program.
* `./target` contains the compiled class files.
