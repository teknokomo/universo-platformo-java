# Core Server Package (core-srv)

Backend services and infrastructure for Universo Platformo.

## Overview

This package provides the core backend functionality including:
- Spring Boot application foundation
- Data access layer and repositories
- Business logic services
- REST API endpoints
- Security and authentication

## Structure

```
core-srv/
└── base/                   # Base implementation
    ├── src/
    │   ├── main/
    │   │   ├── java/      # Java source code
    │   │   └── resources/ # Configuration files
    │   └── test/          # Test code
    └── pom.xml            # Maven configuration
```

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- PostgreSQL Driver

## Configuration

See `src/main/resources/application.properties` for configuration options.

Key environment variables:
- `DATABASE_URL`: PostgreSQL database connection URL
- `DATABASE_USERNAME`: Database username
- `DATABASE_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT token validation

## Running

```bash
cd packages/core-srv/base
mvn spring-boot:run
```

## Testing

```bash
cd packages/core-srv/base
mvn test
```

## Building

```bash
cd packages/core-srv/base
mvn clean package
```
