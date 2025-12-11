# learnproj1

Java test automation project using Selenium, Cucumber, Rest Assured, Maven, and TestNG.

## Structure
- `src/main/java`: Application code (if any)
- `src/test/java`: Test classes (UI, API, Cucumber step definitions)
- `src/test/resources`: Cucumber feature files
- `pom.xml`: Maven configuration
- `testng.xml`: TestNG suite configuration

## How to Run
1. Install Java and Maven.
2. Run `mvn clean test` to execute all tests.
3. For TestNG, use `mvn test -DsuiteXmlFile=testng.xml`.

## Sample Tests
- UI test: Opens Google homepage and checks title.
- API test: Sends GET request and validates response.
- Cucumber: Sample feature and step definitions for Google search.

## Dependencies
- Selenium
- Cucumber
- Rest Assured
- TestNG

## Notes
- ChromeDriver must be available in your PATH for UI tests.
- Update feature files and step definitions as needed for your scenarios.
