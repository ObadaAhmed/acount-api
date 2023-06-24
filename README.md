# Java Assignment

Showcasing the basic structure and setup for a Spring Boot application.

## Prerequisites

Before running this application, ensure that you have the following prerequisites installed:

- Java Development Kit (JDK) 17 or above
- Maven
- Your favorite IDE (e.g., IntelliJ, Eclipse) or a text editor

## Getting Started

Follow these steps to get the project up and running:

1. Clone the repository to your local machine using the following command:
   ```shell
   git clone https://github.com/ObadaAhmed/acount-api.git
2. Build the project using Maven. You can do this either through the IDE's interface or by running the following command in the project's root directory:
   ```shell 
   mvn clean install
3. Run the application using Maven by executing the following command:
   ```shell
   mvn spring-boot:run
4. Once the application starts successfully, you can access it at `http://localhost:8081` in your web browser.


## Project Structure

The project follows a standard Maven project structure with the following directories:

- `src/main/java`: Contains the Java source code for the application.
- `src/main/resources`: Contains the application configuration files and static resources.
- `src/test/java`: Contains the unit tests for the application.
- `pom.xml`: The Maven project object model (POM) file that manages the project's dependencies and build settings.

## Dependencies

The project includes the following dependencies:

- Spring Boot: A framework for building production-ready Spring applications.
- Spring Web: Provides web-related features and the development of RESTful web services.
- Spring Data JPA: Simplifies the implementation of database interactions using the Java Persistence API (JPA).
- Spring Security: Resposible for Authentication and Autherization control of api users.
- Database: MS accessdb.
## Configuration

The application configuration can be found in the `application.properties` file located in the `src/main/resources` directory. Update the configuration properties as needed for your specific setup (e.g., database connection settings, server port).

## Testing

The project includes unit tests written using JUnit and the Spring Testing framework. You can find the test files in the `src/test/java` directory. Run the tests using Maven with the following command:
```shell
mvn test
