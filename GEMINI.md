# Banking System Project

## Project Overview
This is a Java-based Banking System application built using Spring Boot. It provides core banking functionality including user authentication, account management, and financial transaction processing (deposits, withdrawals, and transfers). The system uses JPA for database persistence with MySQL.

## Technologies
- **Java 17**
- **Spring Boot 4.0.6**
- **Spring Data JPA**
- **Spring Security**
- **Spring Web MVC**
- **MySQL Database**
- **Maven** (via `mvnw`)

## Docker
The project supports containerization using Docker and Docker Compose.

- **Run with Docker Compose:**
  `docker-compose up --build`
- **Stop the application:**
  `docker-compose down`

## Development Conventions
- **Project Structure:** Standard Spring Boot layered architecture (`controller`, `service`, `repo`, `entity`, `dto`, `security`, `exception`).
- **Data Transfer:** Use DTOs for request and response handling to decouple the API from the internal entities.
- **Transactions:** Use `@Transactional` in service layer methods that perform multiple database operations (e.g., transfers).
- **Security:** Use Spring Security for authentication. Currently, `/auth/**` endpoints are public, and all other endpoints require authentication.
- **Naming:** Follow standard Java camelCase naming conventions for classes, methods, and variables.
