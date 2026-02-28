# Universo Platformo Java

Implementation of Universo Platformo / Universo MMOOMM / Universo Kiberplano built on Vaadin / Spring and related stack in Java.

## Overview

Universo Platformo Java is a comprehensive full-stack platform implementation using Java enterprise technologies. This project follows the conceptual architecture of [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react) while adapting patterns to Java/Vaadin/Spring ecosystem best practices.

## Technology Stack

- **Language**: Java 17+ (LTS)
- **Frontend Framework**: Vaadin 24.x (Flow) — server-side UI components rendered in the browser
- **Backend Framework**: Spring Boot 3.x with Spring Framework 6.x
- **Authentication**: Supabase Auth REST API (called exclusively from backend services)
- **Database**: Supabase (PostgreSQL-based) with abstracted data access
- **Build Tool**: Maven (multi-module monorepo)
- **Testing**: JUnit 5, Spring Test, Mockito
- **UI Theme**: Vaadin Lumo theme with custom styling

## Project Structure

**⚠️ MANDATORY ARCHITECTURE REQUIREMENT ⚠️**

ALL functionality in this project MUST be implemented as modular packages in the `packages/` directory. Creating functionality outside this structure violates project constitution (Principle I - NON-NEGOTIABLE) and will be rejected in code review.

This modular architecture is based on the proven pattern from [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react) which successfully implements 32+ modular packages. The modular structure is REQUIRED because individual packages will eventually be extracted into separate repositories as the platform matures.

**PROHIBITION**: Feature code MUST NOT be created outside `packages/` directory. Only common infrastructure files (build configuration, root documentation, CI/CD) are exempt.

This project uses a monorepo architecture with packages organized under `packages/` directory:

```
universo-platformo-java/
├── packages/
│   ├── core-srv/          # Core backend services
│   │   └── base/          # Base implementation (Spring Boot, JPA, Security)
│   ├── core-frt/          # Core frontend UI — Spring Boot application entry point
│   │   └── base/          # Vaadin application shell, depends on start-* packages
│   ├── start-srv/         # Start page backend services
│   │   └── base/          # Supabase auth HTTP client, DTOs, configuration
│   └── start-frt/         # Start page frontend views
│       └── base/          # Guest page, authenticated onboarding, login view
├── .specify/              # Specification tooling and templates
└── pom.xml                # Root Maven configuration
```

### Package Naming Convention

- `-srv` suffix: Backend/server packages (REQUIRED for all backend functionality)
- `-frt` suffix: Frontend/UI packages (REQUIRED for all frontend functionality)
- `base/` directory: Base implementation (REQUIRED in each package, supports future multiple implementations)

### Architecture: Frontend — Backend — Supabase

The frontend never calls Supabase directly. All Supabase API calls go through the backend:

```
Browser ─── (Vaadin WebSocket/HTTP) ──► Vaadin Views (start-frt)
                                              │
                                              ▼
                                     SupabaseAuthService (start-frt)
                                     [session management only]
                                              │
                                              ▼
                                     SupabaseAuthClient (start-srv)
                                     [HTTP client with timeouts]
                                              │
                                              ▼
                                       Supabase REST API
```

`SupabaseAuthClient` (`start-srv`) is the single component allowed to call Supabase.
`SupabaseAuthService` (`start-frt`) manages the Vaadin session; it never calls Supabase directly.

## Getting Started

### Prerequisites

- Java 17 or later (LTS version recommended)
- Maven 3.9.x or later
- A Supabase project (for authentication) or PostgreSQL database

### Installation

1. Clone the repository:
```bash
git clone https://github.com/teknokomo/universo-platformo-java.git
cd universo-platformo-java
```

2. Build the project:
```bash
mvn clean install
```

3. Configure environment variables (see Configuration section below)

4. Run the application:
```bash
cd packages/core-frt/base
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## Configuration

### Supabase Authentication

Set the following environment variables to enable Supabase authentication:

```bash
export SUPABASE_URL=https://your-project-id.supabase.co
export SUPABASE_ANON_KEY=your-anon-public-key
export SUPABASE_JWT_SECRET=your-jwt-secret
```

Find these values in your Supabase project under **Settings → API**.

### Database Configuration

Set the following environment variables for PostgreSQL database connection:

```bash
export DATABASE_URL=jdbc:postgresql://your-supabase-host:5432/your-database
export DATABASE_USERNAME=your-username
export DATABASE_PASSWORD=your-password
```

### JWT Configuration

```bash
export JWT_SECRET=your-supabase-jwt-secret
```

**Important**: Never commit credentials to the repository. Always use environment variables or external configuration files.

## Start Pages

The `start-frt/base` module provides two start pages driven by authentication state:

- **Guest page** (`/`): Landing page with hero section, testimonials and footer. Includes a "Sign In" button and a "To the future" call-to-action that navigate to `/login`.
- **Authenticated page** (`/`): Three-step onboarding wizard (Welcome → Select interests → Complete) shown after login. Displays the user's email and a logout button.
- **Login page** (`/login`): Tabbed form for email/password sign-in and registration via Supabase.

## Development Guidelines

### Constitution

This project follows strict governance principles defined in [`.specify/memory/constitution.md`](.specify/memory/constitution.md):

1. **Monorepo Package Architecture**: All functionality organized in discrete packages
2. **Bilingual Documentation**: All documentation in English and Russian (NON-NEGOTIABLE)
3. **Database Abstraction**: Data access abstracted to support multiple DBMS
4. **GitHub Workflow Compliance**: Issue-first development (NON-NEGOTIABLE)
5. **Technology Stack Integrity**: Java/Vaadin/Spring best practices
6. **Specification-Driven Development**: Templates-first approach (NON-NEGOTIABLE)

### Workflow

1. Create GitHub Issue following [`.github/instructions/github-issues.md`](.github/instructions/github-issues.md)
2. Apply labels per [`.github/instructions/github-labels.md`](.github/instructions/github-labels.md)
3. Create specification using templates from `.specify/templates/`
4. Implement following the specification
5. Create Pull Request following [`.github/instructions/github-pr.md`](.github/instructions/github-pr.md)

### Documentation

All documentation must follow bilingual requirements per [`.github/instructions/i18n-docs.md`](.github/instructions/i18n-docs.md):
- English version first (primary standard)
- Russian version with identical structure and line count
- Both versions updated atomically

## Testing

Run all tests:
```bash
mvn test
```

Run tests for a specific package:
```bash
cd packages/start-srv/base
mvn test
```

## Building for Production

Build production artifacts:
```bash
mvn clean package -Pproduction
```

This will create optimized builds with Vaadin production mode enabled.

## Reference Implementation

This implementation is based on concepts from [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react). While following the same conceptual architecture, this project adapts patterns to Java ecosystem conventions and best practices.

**Key Reference Documents**:
- **Best Practices Validation**: [`.specify/memory/best-practices-validation-2025-11-18.md`](.specify/memory/best-practices-validation-2025-11-18.md)
- **Spring Modulith Guide**: [`.specify/memory/spring-modulith-verification-guide.md`](.specify/memory/spring-modulith-verification-guide.md)
- **Java/Vaadin/Spring Best Practices**: [`.specify/memory/java-vaadin-spring-best-practices.md`](.specify/memory/java-vaadin-spring-best-practices.md)
- **Pattern Translation Guide**: [`.specify/memory/react-to-java-patterns.md`](.specify/memory/react-to-java-patterns.md)
- **Architecture Analysis**: [`.specify/memory/react-architecture-analysis.md`](.specify/memory/react-architecture-analysis.md)
- **Feature Roadmap**: [`.specify/memory/feature-implementation-roadmap.md`](.specify/memory/feature-implementation-roadmap.md)
- **Gap Analysis**: [`.specify/memory/gap-analysis.md`](.specify/memory/gap-analysis.md)

**Note**: The React implementation is partially complete and contains legacy code that is being refactored. This Java implementation focuses on clean, enterprise-grade patterns.

## Contributing

1. Follow the constitution principles
2. Create issues before implementation
3. Maintain bilingual documentation
4. Follow specification-driven development
5. Ensure all tests pass
6. Update documentation as needed

## License

[To be determined]

## Links

- Documentation: [docs.universo.pro](https://docs.universo.pro) (coming soon)
- Reference Implementation: [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react)
- Pattern Translation Guide: [`.specify/memory/react-to-java-patterns.md`](.specify/memory/react-to-java-patterns.md)
- Project Constitution: [`.specify/memory/constitution.md`](.specify/memory/constitution.md)
