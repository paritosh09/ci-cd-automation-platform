# CI/CD Automation Platform

Enterprise-grade CI/CD Pipeline Automation Platform built with Spring Boot.

## Features

- Pipeline Management with GitHub Actions integration
- Webhook processing for automated builds
- User authentication with JWT
- Docker containerization
- Multi-database support (MySQL, MongoDB, Redis)
- RESTful API for pipeline and user management

## Architecture

- **Backend:** Spring Boot, JPA/Hibernate, Spring Security (JWT)
- **Database:** MySQL (default), MongoDB, Redis (for caching/session)
- **Containerization:** Docker, Docker Compose
- **API Documentation:** OpenAPI/Swagger

## Quick Setup

### 1. Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

### 2. Environment Setup

Copy `.env.example` to `.env` and update environment variables as needed.

```sh
cp .env.example .env
```

### 3. Build & Run (Local)

```sh
mvn clean package
java -jar target/automation-platform-1.0.0.jar
```

### 4. Build & Run (Docker)

```sh
docker-compose up --build
```

### 5. API Endpoints

- **Authentication:** `/api/auth/register`, `/api/auth/login`, `/api/auth/logout`
- **Pipeline Management:** `/api/pipeline/create`, `/api/pipeline/list`, etc.

See [src/main/java/com/cicd/automation/controller/AuthController.java](src/main/java/com/cicd/automation/controller/AuthController.java) for authentication endpoints.

### 6. Configuration

Edit `src/main/resources/application.properties` or `src/main/resources/application-prod.properties` for database and other settings.

### 7. Database Migration

Uses JPA/Hibernate for schema management. For production, consider integrating Flyway or Liquibase.

## Project Structure

```
.
├── src/
│   ├── main/
│   │   ├── java/com/cicd/automation/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── model/
│   │   │   └── CiCdAutomationPlatformApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-prod.properties
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Development

- Use [settings.json](.vscode/settings.json) for recommended VS Code settings.
- Unit and integration tests can be run with:

```sh
mvn test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

## License

MIT

---

For more details, see the source files:
- Main application: [`com.cicd.automation.CiCdAutomationPlatformApplication`](src/main/java/com/cicd/automation/CiCdAutomationPlatformApplication.java)
- User model: [`com.cicd.automation.model.User`](src/main/java/com/cicd/automation/model/User.java)
