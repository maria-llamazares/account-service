# Bank Account Management API

## Overview

The **Bank Account Management API** is a Spring Boot-based microservice that enables users to manage bank accounts. The API provides functionalities for checking account balances, performing deposits, and executing debits. It is a key tool for digital financial management, offering simple and secure operations for managing account balances.

---

## Features

- Retrieve account balance using an **IBAN**.
- Deposit funds into a bank account.
- Debit funds from a bank account with proper balance validation.
- Simulate external system notifications via public APIs (e.g., logging or monitoring).

---

## Technology Stack

The application is built with the following key technologies:

- **Java 23**: Primary programming language.
- **Spring Boot 3.4.3**: Framework for building the REST API.
- **Spring MVC**: RESTful services and routing.
- **Spring Data JPA**: Data persistence and ORM powered by **Hibernate**.
- **H2 Database (Development)**: Used as the in-memory database for testing.
- **Swagger 3/OpenAPI**: API documentation and testing.
- **Lombok**: Reduces boilerplate code for entities and DTOs.
- **Jakarta Validation**: Request validation for input consistency.
- **HTTP Client (RestTemplate/WebClient)**: Simulates external system calls.
- **Maven**: Project build and dependency management.
- **Spring Boot Starter Test:** For testing purposes, includes libraries like JUnit and Mockito.

---

## Prerequisites

Before starting the application, ensure you have the following installed:

- **Java Development Kit (JDK) 23**
- **Maven 3.9.9** for dependency management
- **Git** for cloning and managing the repository.

---

## Installation and Setup

1. **Clone the Repository**  
   Clone this repository to your local machine:
   ```bash
   git clone https://github.com/maria-llamazares/account-service
   cd account-service
   ```

2. **Build the Project**  
   Use Maven to build and package the application:
   ```bash
   mvn clean install
   ```

3. **Run the Application**  
   Start the application using the following Maven command or by running the `AccountServiceApplication` class:
   ```bash
   mvn spring-boot:run
   ```

   Alternatively:
   ```bash
   java -jar target/account-service-0.0.1-SNAPSHOT.jar
   ```

4. **Access API Documentation via Swagger**  
   Once the application is running, you can access the Swagger/OpenAPI documentation at:  
   [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Testing the API

You can test the API using the following methods:

### 1. Swagger UI (Web Interface)
- Open [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) in your browser.
- Use the interactive interface to explore and test endpoints.
- Available endpoints include:
   - **`GET /accounts/{iban}/balances`**: Retrieve account balance.
   - **`POST /accounts/{iban}/deposit`**: Deposit funds into an account.
   - **`POST /accounts/{iban}/debit`**: Debit funds from an account.

### 2. Postman (API Client)
- A Postman collection is included in the project directory under:  
  `postman/Bank Account Mangement API.postman_collection.json`
- Import this collection into Postman and directly test the predefined requests:
   1. Open Postman.
   2. Go to *File -> Import*.
   3. Select the `Bank Account Mangement API.postman_collection.json` file from the project directory.

You can also customize the requests as needed to test different use cases.

---

## Database Access

The API uses an **in-memory H2 database** for development and testing. You can access the database console by navigating to:  
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

Use the following credentials to log in:
- **JDBC URL:** `jdbc:h2:mem:account-db`
- **Username:** `sa`
- **Password:** *(leave blank)*

This console allows you to query the database and verify data such as account balances.

---

### Simulated External System Call

Certain operations, such as deposits or debits, trigger a simulated external system call using the [HTTPStat API](https://httpstat.us). This could represent logging or notification in a real-world scenario.
