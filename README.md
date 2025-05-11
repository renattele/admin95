# Admin95

Admin95 is a Spring Boot application that provides a web interface for managing Docker projects and containers. It
allows users to create, update, and monitor Docker projects with an easy-to-use dashboard.

## Technologies

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- Docker/Docker Compose
- Gradle

## Features

- Create and manage Docker projects
- View and edit Docker Compose files
- Start, stop, and monitor container states
- View container logs
- Secure access with authentication

## Prerequisites

- JDK 11+
- Docker and Docker Compose
- PostgreSQL or other compatible database

## Setup

1. Clone the repository
2. Configure database connection in `admin.properties` (see Configuration)
3. Build the application:
   ```bash
   ./gradlew build
   ```
4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## Configuration

The application uses external configuration through the `admin.properties` file:

```properties
# Docker configuration
docker.path=/path/to/docker/projects
docker.default-compose-file=docker-compose.yml
# Database configuration
db.url=jdbc:postgresql://localhost:5432/admin95
db.username=postgres
db.password=your_password
db.driver=org.postgresql.Driver
db.jpa-driver=org.hibernate.dialect.PostgreSQLDialect
```

## Usage

1. Access the application at `http://localhost:8080`
2. Login with default credentials:
    - Username: `admin`
    - Password: `admin`
3. Create a new Docker project and add your Docker Compose configuration
4. Use the dashboard to manage your Docker containers

## Error Handling

The application includes custom error pages and proper error handling for API requests.

## Security

The application uses Spring Security for authentication. It is recommended to change the default credentials for
production use.