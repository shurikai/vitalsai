# Systolic API

Systolic is a secure, web-based API for managing and visualizing personal blood pressure data. This backend is built with Java and Spring Boot, providing a robust, stateless, and secure foundation for a corresponding frontend application.

## Core Technologies

-   **Framework**: Spring Boot 3.x
-   **Language**: Java 17+
-   **Security**: Spring Security 6.x with JWT for stateless authentication.
-   **Database**: PostgreSQL
-   **Data Access**: Spring Data JPA (Hibernate)
-   **Database Migrations**: Flyway
-   **DTO Mapping**: MapStruct
-   **API Documentation**: Swagger / OpenAPI 3
-   **Build Tool**: Maven

## Features

-   **User Authentication**: Secure patient registration and login endpoints using `BCrypt` password hashing.
-   **Token-Based Security**: Stateless, JWT-based authentication protects all sensitive API endpoints.
-   **Blood Pressure Reading Management**: Full CRUD (Create, Read, Update, Delete) operations for blood pressure readings.
-   **Data Isolation**: Users can only access and manage their own data, enforced at the service layer.
-   **Custom Exception Handling**: Clear, specific exceptions for resource not found and authorization errors.
-   **Automated Database Migrations**: Flyway manages the database schema, ensuring consistency across all environments.
-   **Interactive API Documentation**: A Swagger UI is available for exploring and testing the API endpoints.

## Prerequisites

Before you begin, ensure you have the following installed:
-   JDK 17 or newer
-   Apache Maven 3.6+
-   A running PostgreSQL instance

## Getting Started

Follow these steps to get the application running locally.

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd backend
```

### 2. Configure the Database

1. Access your PostgreSQL instance and create a new database and patient for the application.
```sql
    CREATE DATABASE systolic_db;
    CREATE USER systolic WITH PASSWORD 'example';
    GRANT ALL PRIVILEGES ON DATABASE systolic_db TO systolic;
```
2. The application uses Flyway to manage the schema, so no manual table creation is needed.

### 3. Configure the Application

The project uses profile-specific property files. For local development, you will use the
dev profile.
1. The base configuration is in src/main/resources/application.properties.
2. Create a new file src/main/resources/application-dev.properties and add the following content.
This file contains your local database credentials and a development-only JWT secret. This file
is git-ignored and should not be checked into source control.

```properties
    # --- Development Profile Settings ---

    # Your local database connection details
    spring.datasource.url=jdbc:postgresql://localhost:5432/systolic_db
    spring.datasource.username=systolic
    spring.datasource.password=example

    # Enable detailed SQL logging for debugging
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.use_sql_comments=true

    # 'validate' is safe for dev to ensure entities match the Flyway schema
    spring.jpa.hibernate.ddl-auto=validate

    # A non-production secret key for development and testing.
    # Generate your own long, secure, base64-encoded string.
    jwt.secret=yourSuperSecretKeyThatIsLongAndSecureAndBase64EncodedStringForDevOnly
    
```

### 4. Buld and Run the Application

The dev profile is activated by default in application.properties for convenience during local development.
1. Build the application using Maven:
```bash
    mvn clean install
```
2. Run the application:
```bash
    mvn spring-boot:run
```

The application will start on the configured port (defaults to 8080).

## API Documentation

Once the application is running, you can access the interactive Swagger UI to explore and test the API endpoints.
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI Spec: http://localhost:8080/v3/api-docs

The API base path is /api.

### Key Endpoints

- POST /api/auth/register: Register a new patient.
- POST /api/auth/login: Log in to receive a JWT.
- GET /api/readings: Get all blood pressure readings for the authenticated patient.
- POST /api/readings: Create a new blood pressure reading.
- GET /api/readings/{id}: Get a specific reading by its ID.
- PUT /api/readings/{id}: Update an existing reading.
- DELETE /api/readings/{id}: Delete a reading.

## Production Configuration

For production deployments, it is critical to externalize your configuration. The application-prod.properties file is configured to read sensitive values from environment variables.

You would run the application with the prod profile active:
```bash
export DB_URL=...
export DB_USER=...
export DB_PASS=...
export JWT_SECRET_KEY=...

java -jar -Dspring.profiles.active=prod target/systolic-0.0.1-SNAPSHOT.jar
```
