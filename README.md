> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details
Project Name: Server (Sprint 2)

Team Members: cbutulis and dhu34

Contributions: 
    -cbutulis: csv endpoints, package organization
    -dhu34: broadband endpoint, testing
    -shared: caching

Total Estimated Time: 21 hours 

GitHub Link: `https://github.com/cs0320-s24/server-cbutulis-dhu34.git`

# Design Choices

Design Choices: The most crucial design choices were passing a CachedDataSource into the 
BroadBandHandler to enable caching, creating a CensusAPIUtilities class to handle a lot 
of common functionality statically, and creating a Handler abstract class to share code 
between all of the Handlers, to reduce repeated code. 

Structure Overview: 
Essentially, the program works by having a Server class with endpoints. The loadcsv endpoint 
loads a provided csv, given parameters for a file name and a boolean of whether there 
are headers. An example query could be:
    
    /loadcsv?filePath=data/census/dol_ri_earnings_disparity.csv&hasHeader=true

The viewcsv endpoint views a csv in its entirety. An example query could be: 

    /viewcsv

The searchcsv endpoint searches a csv. An example query could be: 
    
    /searchcsv?target=RI

The broadband endpoint queries the Census Bureau's information for broadband access by county. An
example query could be: 

    /broadband?state=California&county=Orange%20County,%20California
    

Explain the relationships between classes/interfaces: The CreatorFromRowInterface in parsing is 
used to allow generic types to be passed into Parser, to parse the csv into objects. This is 
implemented by DefaultCreator and RecordCreator. Furthermore, there is a Datasource interface
which allows for mock testing, by having a common sendRequest method. 

Discuss any specific data structures you used, why you created it, and other high level 
explanations: Hashmaps are used to store state and county codes for the purposes of API requests
to the Census Bureau. A LoadingCache is also used in CachedDatasource to reduce the load on 
the server due to repeated API queries. 

Runtime/ space optimizations you made: Caching definitely comes to mind here. We immediately noticed
a query could take a substantial amount of time when testing using a browser, however subsequent 
API queries were much faster once the cache was working. 

# Errors/Bugs

None discovered, and no substantial checkstyle errors. 

# Tests

The testing suite is comprised by two files: TestBroadbandHandler and TestCSVHandlers. Testing 
the broadband handlers, there are tests to check a basic successful request, a request where the
queried county doesn't exist, and a mocked test, where an actual API query does not occur. Testing
the CSV handlers, there are tests to check loading a valid file, loading a nonexistent file, 
viewing said file, checking viewing without a file loaded produces an error message, searching 
with and without headers, and ensuring that an invalid search results in an empty result. 

# How to

Server: To run the server, run the command `mvn package`, and then run `./run`
Tests: To run the tests, click the arrow at the top of TestBroadbandHandler, and on top of
TestCSVHandlers as well. 
