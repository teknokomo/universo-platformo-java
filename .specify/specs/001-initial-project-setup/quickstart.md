# Quickstart Guide: Universo Platformo Java

**Version**: 1.0.0  
**Last Updated**: 2025-11-17  
**For**: Initial Project Setup (001-initial-project-setup)

## Overview

This quickstart guide helps developers set up and run the Universo Platformo Java project locally. It covers prerequisites, installation, configuration, and common development tasks.

## Prerequisites

### Required Software

- **Java Development Kit (JDK)**: Java 17 or later (Java 21 LTS recommended)
  - Download: [Adoptium Eclipse Temurin](https://adoptium.net/) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
  - Verify: `java -version` should show version 17+

- **Maven**: Version 3.8 or later
  - Download: [Apache Maven](https://maven.apache.org/download.cgi)
  - Or use Maven Wrapper (included): `./mvnw` (Linux/macOS) or `mvnw.cmd` (Windows)
  - Verify: `mvn -version` should show version 3.8+

- **Git**: Version 2.0 or later
  - Download: [Git SCM](https://git-scm.com/)
  - Verify: `git --version`

- **IDE** (recommended):
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/) (Ultimate or Community)
  - [Eclipse IDE for Enterprise Java Developers](https://www.eclipse.org/downloads/packages/)
  - [Visual Studio Code](https://code.visualstudio.com/) with Java Extension Pack

### Optional Software

- **Docker**: For running PostgreSQL locally or using Testcontainers
  - Download: [Docker Desktop](https://www.docker.com/products/docker-desktop/)

- **PostgreSQL Client**: For database management
  - [pgAdmin](https://www.pgadmin.org/)
  - [DBeaver](https://dbeaver.io/)

- **Postman or Insomnia**: For API testing
  - [Postman](https://www.postman.com/)
  - [Insomnia](https://insomnia.rest/)

## Quick Setup (5 Minutes)

### 1. Clone the Repository

```bash
git clone https://github.com/teknokomo/universo-platformo-java.git
cd universo-platformo-java
```

### 2. Configure Environment Variables

Create a `.env` file in the project root (or set environment variables):

```bash
# Supabase Configuration
SUPABASE_URL=https://your-project.supabase.co
SUPABASE_ANON_KEY=your-anon-key
SUPABASE_SERVICE_KEY=your-service-role-key
SUPABASE_JWT_SECRET=your-jwt-secret

# Database Configuration (Supabase PostgreSQL)
DB_URL=jdbc:postgresql://db.your-project.supabase.co:5432/postgres
DB_USERNAME=postgres
DB_PASSWORD=your-database-password

# Application Configuration
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
```

**Note**: Never commit the `.env` file with real credentials. Use `.env.example` as a template.

### 3. Build the Project

Using Maven Wrapper (recommended):

```bash
./mvnw clean install
```

Or using installed Maven:

```bash
mvn clean install
```

This will:
- Compile all Java source code
- Run unit tests
- Package each module into JAR files
- Install artifacts to local Maven repository

**Expected output**: `BUILD SUCCESS` (build time ~2-5 minutes on first run)

### 4. Run the Application

```bash
./mvnw spring-boot:run -pl packages/core-srv/base
```

Or run the main application class from your IDE:
- **Class**: `pro.universo.core.api.UniversoPlatformoApplication`
- **Module**: `packages/core-srv/base`

The application will start on `http://localhost:8080`

**Expected output**:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

2025-11-17T06:00:00.000Z  INFO --- [main] p.u.c.a.UniversoPlatformoApplication: Started UniversoPlatformoApplication in 8.5 seconds
```

### 5. Access the Application

- **Vaadin UI**: http://localhost:8080/
- **REST API**: http://localhost:8080/api/
- **API Documentation** (Swagger UI): http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

## Detailed Setup

### Setting Up Supabase

1. **Create a Supabase Project**:
   - Visit [Supabase](https://supabase.com/)
   - Create a new project
   - Note your project URL and API keys

2. **Get Database Credentials**:
   - Go to Project Settings → Database
   - Copy the connection string
   - Format: `postgresql://postgres:[PASSWORD]@db.[PROJECT].supabase.co:5432/postgres`

3. **Get JWT Secret**:
   - Go to Project Settings → API
   - Copy JWT Secret (needed for token validation)

4. **Enable Authentication** (optional):
   - Go to Authentication → Providers
   - Enable Email, Google, or other providers

### Running with Docker

Run PostgreSQL locally with Docker (alternative to Supabase):

```bash
docker run -d \
  --name universo-postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=universo \
  -p 5432:5432 \
  postgres:15
```

Update `.env`:
```bash
DB_URL=jdbc:postgresql://localhost:5432/universo
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run tests for specific module
./mvnw test -pl packages/core-srv/base

# Run integration tests only
./mvnw verify -P integration-tests

# Run with code coverage
./mvnw clean verify jacoco:report
```

**Coverage reports**: `target/site/jacoco/index.html` in each module

### Building for Production

```bash
# Build with production profile
./mvnw clean package -P production

# Skip tests (not recommended)
./mvnw clean package -DskipTests

# Build Docker image (future)
./mvnw spring-boot:build-image
```

Production artifacts: `packages/*/base/target/*.jar`

## Development Workflow

### Creating a New Feature

1. **Create Feature Specification**:
   ```bash
   # Create new feature directory
   mkdir -p specs/002-feature-name
   
   # Copy spec template
   cp .specify/templates/spec-template.md specs/002-feature-name/spec.md
   
   # Edit spec.md with feature requirements
   ```

2. **Generate Implementation Plan**:
   ```bash
   # Run planning command (if using custom agent)
   # This generates plan.md, research.md, data-model.md, etc.
   ```

3. **Create Feature Branch**:
   ```bash
   git checkout -b 002-feature-name
   ```

4. **Create GitHub Issue**:
   - Follow `.github/instructions/github-issues.md`
   - Add bilingual description
   - Apply appropriate labels

5. **Implement Feature**:
   - Create package structure: `packages/feature-frt` and `packages/feature-srv`
   - Add Maven modules to parent POM
   - Implement entities, repositories, services, views
   - Write tests (70%+ coverage)

6. **Create Pull Request**:
   - Follow `.github/instructions/github-pr.md`
   - Reference issue number
   - Include bilingual description

### Common Development Tasks

#### Adding a New Maven Module

1. Create module directory:
   ```bash
   mkdir -p packages/newfeature-srv/base/src/main/java
   mkdir -p packages/newfeature-srv/base/src/test/java
   ```

2. Create `pom.xml` in module:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project>
       <parent>
           <groupId>pro.universo</groupId>
           <artifactId>universo-platformo-parent</artifactId>
           <version>1.0.0-SNAPSHOT</version>
           <relativePath>../../../pom.xml</relativePath>
       </parent>
       <artifactId>newfeature-srv-base</artifactId>
       <name>New Feature Service</name>
       
       <dependencies>
           <!-- Dependencies without versions (managed by parent) -->
       </dependencies>
   </project>
   ```

3. Add module to parent POM:
   ```xml
   <modules>
       <module>packages/core-srv/base</module>
       <module>packages/newfeature-srv/base</module>
   </modules>
   ```

#### Running in Development Mode

Vaadin development mode with hot reload:

```bash
./mvnw spring-boot:run -P dev
```

Features:
- Live reload on save (no restart needed for UI changes)
- Debug logging enabled
- H2 console enabled: http://localhost:8080/h2-console

#### Debugging

IntelliJ IDEA:
1. Run → Edit Configurations
2. Add "Spring Boot" configuration
3. Main class: `pro.universo.core.api.UniversoPlatformoApplication`
4. Set breakpoints and debug

VS Code:
1. Create `.vscode/launch.json`:
   ```json
   {
     "type": "java",
     "name": "Spring Boot",
     "request": "launch",
     "mainClass": "pro.universo.core.api.UniversoPlatformoApplication",
     "projectName": "core-srv-base"
   }
   ```
2. Press F5 to debug

#### Database Migrations

Creating a new migration (Flyway):

1. Create SQL file in `src/main/resources/db/migration/`:
   ```
   V006__add_user_preferences_table.sql
   ```

2. Write SQL:
   ```sql
   CREATE TABLE user_preferences (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       user_id UUID NOT NULL,
       preferences JSONB,
       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
       updated_at TIMESTAMP
   );
   
   CREATE INDEX idx_user_preferences_user_id ON user_preferences(user_id);
   ```

3. Migration runs automatically on application startup

#### Adding i18n Translations

1. Edit `messages_en.properties`:
   ```properties
   newfeature.list.title=New Features
   newfeature.create.button=Create New Feature
   ```

2. Edit `messages_ru.properties`:
   ```properties
   newfeature.list.title=Новые функции
   newfeature.create.button=Создать новую функцию
   ```

3. Use in Vaadin:
   ```java
   String title = getTranslation("newfeature.list.title");
   ```

## Troubleshooting

### Build Fails with "Java version" Error

**Problem**: Maven requires Java 17+ but finds older version

**Solution**:
```bash
# Check Java version
java -version

# Set JAVA_HOME (Linux/macOS)
export JAVA_HOME=/path/to/jdk-17

# Set JAVA_HOME (Windows)
set JAVA_HOME=C:\path\to\jdk-17

# Or use specific Java for Maven
./mvnw clean install -Djava.home=/path/to/jdk-17
```

### Database Connection Fails

**Problem**: Cannot connect to Supabase PostgreSQL

**Solutions**:
1. Check environment variables are set correctly
2. Verify Supabase project is not paused
3. Check firewall allows outbound connections on port 5432
4. Test connection with psql:
   ```bash
   psql "postgresql://postgres:[PASSWORD]@db.[PROJECT].supabase.co:5432/postgres"
   ```

### Vaadin UI Not Loading

**Problem**: http://localhost:8080 shows 404 or blank page

**Solutions**:
1. Check Vaadin is in production mode (`vaadin.productionMode=false` for dev)
2. Run with dev profile: `./mvnw spring-boot:run -P dev`
3. Clear browser cache and hard reload (Ctrl+Shift+R)
4. Check console for JavaScript errors

### Port 8080 Already in Use

**Problem**: `java.net.BindException: Address already in use`

**Solutions**:
1. Change port in application.yml or environment variable:
   ```bash
   SERVER_PORT=8081 ./mvnw spring-boot:run
   ```

2. Find and kill process using port 8080:
   ```bash
   # Linux/macOS
   lsof -ti:8080 | xargs kill -9
   
   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   ```

### Tests Failing

**Problem**: Tests fail with database errors

**Solutions**:
1. Use Testcontainers (automatically starts PostgreSQL in Docker):
   ```bash
   ./mvnw test -P testcontainers
   ```

2. Or use H2 in-memory database for tests:
   - Already configured in `application-test.yml`
   - Tests should use `@ActiveProfiles("test")`

## Next Steps

After completing this quickstart:

1. **Read Documentation**:
   - [README.md](../../../README.md) - Project overview
   - [README-RU.md](../../../README-RU.md) - Russian version
   - [Constitution](.specify/memory/constitution.md) - Development principles

2. **Explore Example Feature**:
   - Review Clusters feature implementation (when available)
   - Study entity pattern, repository, service, view structure

3. **Set Up IDE**:
   - Install recommended plugins (Vaadin, Spring Boot, Lombok if used)
   - Configure code style (follow project conventions)
   - Set up live reload for faster development

4. **Join Community**:
   - Review open issues: https://github.com/teknokomo/universo-platformo-java/issues
   - Check React reference: https://github.com/teknokomo/universo-platformo-react
   - Read contribution guidelines

## Resources

### Official Documentation
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Vaadin Documentation](https://vaadin.com/docs/latest)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Supabase Documentation](https://supabase.com/docs)

### Tutorials
- [Building a Vaadin Application](https://vaadin.com/docs/latest/tutorial)
- [Spring Boot with PostgreSQL](https://spring.io/guides/gs/accessing-data-jpa/)
- [REST API with Spring Boot](https://spring.io/guides/tutorials/rest/)

### Tools
- [Maven Documentation](https://maven.apache.org/guides/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

---

**Having issues?** Create an issue: https://github.com/teknokomo/universo-platformo-java/issues/new
