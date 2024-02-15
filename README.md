> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details

# Design Choices

# Errors/Bugs

# Tests

# How to

TODO:
-add exception if invalid args to endpoint
-clean up everything
-document
-finish this readme
-refactor into better packages

Testing Requests:
http://localhost:3232/broadband?state=California&county=Orange%20County,%20California
http://localhost:3232/loadcsv
http://localhost:3232/viewcsv
http://localhost:3232/searchcsv?target=Black