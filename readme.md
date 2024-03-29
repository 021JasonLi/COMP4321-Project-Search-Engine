# COMP4321 Project Phase 1 Group 44

The project phase 1 contains spider and indexer to crawl and index webpages.

## Development Environment and Tools

This project uses Java 17 to develop. HTML parser and JDBM libraries (provided in labs) 
are also used and included in the folder.

## Building Project 

### Windows
In the root directory of the folder:

* Compile the project 
```shell
javac -cp ./libs/* -d ./target/ "@sources.txt"
```

* Run the project
```shell
java -cp "./libs/*;./target;." Main
```

* Run the tester
```shell
java -cp "./libs/*;./target;." Tester
```

### Macs
In the root directory of the folder:

* Compile the project
```shell
javac -cp ./libs/* -d ./target/ @sources.txt
```

* Run the project
```shell
java -cp ./libs/*:./target:. Main
```

* Run the tester
```shell
java -cp ./libs/*:./target:. Tester
```

## Folders

* `./db` contains the db files generated in the spider and indexer.
* `./libs` contains the libraries used in the project.
* `./src/main` contains the source codes of the spider and indexer.
* `./src/test` contains the tester program.
* `./target` contains the compiled class files.
