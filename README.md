# Banking System API

## Overview

The Banking System API is a backend banking application built with Java, Spring Boot, Spring Security, JWT Authentication, MySQL, and Docker. The project simulates real-world banking operations such as account creation, deposits, withdrawals, money transfers, authentication, authorization, and audit logging.

This project was designed to strengthen backend development skills and demonstrate enterprise-level software architecture commonly used in modern Java applications.

---

## Features

### User Management

* User Registration
* User Login
* JWT Authentication
* Password Encryption using BCrypt
* Role-Based Authorization (USER / ADMIN)

### Account Management

* Create Bank Accounts
* View Account Information
* Account Status Management
* Freeze Accounts
* Unfreeze Accounts
* Close Accounts

### Transaction Management

* Deposit Funds
* Withdraw Funds
* Transfer Funds Between Accounts
* Transaction History

### Security

* Spring Security
* JWT Token Authentication
* Protected Endpoints
* Role-Based Access Control
* Ownership-Based Authorization

### Monitoring & Auditing

* Audit Logging
* Global Exception Handling
* Validation and Error Responses

### Deployment

* Docker Containerization
* Docker Compose Configuration
* MySQL Container Integration

---

## Technology Stack

### Backend

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate

### Database

* MySQL

### Security

* JWT (JSON Web Tokens)
* BCrypt Password Encoding

### Testing

* JUnit 5
* Mockito

### Documentation

* Swagger / OpenAPI

### DevOps

* Docker
* Docker Compose

---

## Project Architecture

Client
↓
Controllers
↓
Services
↓
Repositories
↓
MySQL Database

The application follows a layered architecture pattern:

### Controllers

Handle incoming HTTP requests and return responses.

Examples:

* AuthController
* AccountController
* TransactionController

### Services

Contain business logic and validation.

Examples:

* AuthService
* AccountService
* TransactionService
* AuditLogService

### Repositories

Handle database operations using Spring Data JPA.

Examples:

* UserRepository
* AccountRepository
* TransactionRepository

### Entities

Represent database tables.

Examples:

* Users
* Account
* Transaction
* AuditLog

### DTOs

Transfer data between the client and server while protecting internal entity structures.

Examples:

* RegisterRequest
* LoginRequest
* CreateAccountRequest
* DepositRequest
* WithdrawRequest
* TransferRequest
* AccountResponse
* TransactionResponse
* AuthResponse

---

## Authentication Flow

1. User registers an account.
2. Password is encrypted using BCrypt.
3. User logs in with email and password.
4. AuthService validates credentials.
5. JWT token is generated.
6. Client sends JWT token with future requests.
7. Spring Security validates the token before granting access.

---

## Transaction Flow

### Deposit

Client Request
↓
TransactionController
↓
TransactionService
↓
AccountRepository
↓
Database Update
↓
Transaction Recorded
↓
Audit Log Created

### Transfer

Sender Account
↓
Balance Validation
↓
Withdraw Funds
↓
Deposit Funds
↓
Save Transaction
↓
Create Audit Log
↓
Return Response

---

## API Documentation

Swagger UI:

http://localhost:8080/swagger-ui/index.html

Swagger provides interactive API documentation and allows testing endpoints directly from the browser.

---

## Running Locally

### Prerequisites

* Java 17
* Maven
* MySQL

### Build Application

mvn clean package

### Run Application

mvn spring-boot:run

---

## Running with Docker

### Build and Start Containers

docker compose up --build

### Stop Containers

docker compose down

After startup:

Application:
http://localhost:8080

Swagger:
http://localhost:8080/swagger-ui/index.html

---

## Testing

Run all tests:

mvn test

Unit tests are implemented using:

* JUnit 5
* Mockito

Test coverage includes:

* Deposits
* Withdrawals
* Transfers
* Authentication
* Account Management

---

## Lessons Learned

Through this project I gained experience with:

* Spring Boot Application Development
* REST API Design
* Spring Security
* JWT Authentication
* Database Design and Relationships
* DTO Design Patterns
* Service Layer Architecture
* Repository Pattern
* Global Exception Handling
* Unit Testing
* Docker Containerization
* API Documentation with Swagger

---

## Future Enhancements

Potential improvements include:

* Fraud Detection Rules
* Email Notifications
* Account Locking
* Transaction Limits
* Kafka Event Streaming
* Microservices Architecture
* Monitoring with Prometheus and Grafana
* CI/CD Pipeline Integration
* Cloud Deployment (AWS)

---

## Author

Sajjad Haider

Computer Science Student | Java Backend Developer

Built as a portfolio project to strengthen enterprise Java and Spring Boot development skills.
