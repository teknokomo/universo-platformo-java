# Research: Initial Universo Platformo Java Project Setup

**Feature**: 001-initial-project-setup  
**Date**: 2025-11-17  
**Phase**: Phase 0 - Research & Technology Decisions

## Overview

This document captures the research findings and technology decisions for establishing the Universo Platformo Java project. Since this is a foundational setup feature based on clear requirements from the user and the React reference implementation, most decisions are predetermined. This research validates those choices and documents alternatives considered.

## Technology Stack Decisions

### Decision 1: Java Version - Java 17+ LTS (Java 21 Recommended)

**Rationale**:
- Java 17 is the current LTS (Long-Term Support) version with support until September 2029
- Java 21 (newer LTS, released September 2023) offers enhanced features like Virtual Threads (Project Loom), Pattern Matching, and Record Patterns
- Both versions are well-supported by Spring Boot 3.x and Vaadin 24.x
- LTS versions ensure long-term stability and security updates
- Java 21 virtual threads can significantly improve scalability for I/O-bound operations (database access, API calls)

**Alternatives Considered**:
- **Java 11**: Older LTS (support ends September 2026); lacks modern language features; Spring Boot 3.x moving away from Java 11 support
- **Java 8**: Too old, not compatible with Spring Boot 3.x; missing modern language improvements
- **Java 23** (non-LTS): Cutting edge but not LTS; less stable for enterprise production use

**Decision**: Target Java 17 as minimum, recommend Java 21 for production deployments

### Decision 2: Build Tool - Maven with Multi-Module Project

**Rationale**:
- Maven is the de facto standard for enterprise Java projects with mature ecosystem
- Multi-module Maven projects naturally support monorepo structure required by Constitution Principle I
- Maven BOM (Bill of Materials) pattern provides centralized dependency management
- Spring Boot and Vaadin both provide excellent Maven integration and starter POMs
- Maven's declarative nature and convention-over-configuration aligns with project goals
- Better IDE support across IntelliJ IDEA, Eclipse, and VS Code compared to Gradle for multi-module projects

**Alternatives Considered**:
- **Gradle with multi-project**: More flexible build scripts; better incremental build performance; however, steeper learning curve and more complex configuration for multi-module setups
- **Gradle with Kotlin DSL**: Type-safe build scripts; however, adds complexity and another language to the stack
- **PNPM (from React version)**: JavaScript package manager; not applicable to Java ecosystem

**Decision**: Maven 3.9+ with multi-module project structure, parent POM with BOM dependency management

### Decision 3: Frontend Framework - Vaadin 24.3.x (Flow)

**Rationale**:
- Vaadin Flow provides full-stack Java development (no separate JavaScript required)
- Server-side rendering with automatic client-server communication
- Built-in Material Design-inspired Lumo theme matches requirement for Material UI equivalent
- Component-based architecture similar to React but in pure Java
- Strong Spring Boot integration (official Spring Boot Starter available)
- Type-safe navigation and routing with Java annotations
- Built-in i18n support with ResourceBundle integration
- Vaadin 24 is LTS version with extended support

**Alternatives Considered**:
- **Thymeleaf + Alpine.js**: Lightweight; however, requires JavaScript knowledge and doesn't provide rich UI components out of box
- **JSF (JavaServer Faces)**: Older technology; component-based but less modern, declining community support
- **Separate React Frontend**: Would require two language stacks (Java + JavaScript); increases complexity; violates Constitution Principle V (Technology Stack Integrity)

**Decision**: Vaadin 24.3.x (latest stable LTS) with Lumo theme

### Decision 4: Backend Framework - Spring Boot 3.2.x with Spring Framework 6.x

**Rationale**:
- Spring Boot is the industry standard for enterprise Java applications
- Spring Boot 3.x requires Java 17+ and uses Spring Framework 6.x (native Java EE → Jakarta EE migration)
- Comprehensive ecosystem: Spring Data JPA (database), Spring Security (authentication), Spring Web (REST)
- Auto-configuration reduces boilerplate while maintaining flexibility
- Production-ready features: health checks, metrics, monitoring (Actuator)
- Excellent documentation and community support
- Seamless Vaadin integration via official starter

**Alternatives Considered**:
- **Quarkus**: Faster startup, smaller memory footprint; however, newer with smaller ecosystem; overkill for non-cloud-native requirements
- **Micronaut**: Similar benefits to Quarkus; however, less mature Spring Data alternative
- **Java EE / Jakarta EE**: More heavyweight; requires application server; less modern development experience

**Decision**: Spring Boot 3.2.x with Spring Framework 6.x, Spring Data JPA, Spring Security

### Decision 5: Database Access - Supabase with Spring Data JPA Abstraction

**Rationale**:
- Supabase specified in user requirements as primary database solution
- Supabase is PostgreSQL-based, compatible with standard JPA/Hibernate
- Spring Data JPA provides repository abstraction pattern required by Constitution Principle III
- JPA/Hibernate isolates vendor-specific code, enabling future DBMS support
- Repository interfaces define database operations without PostgreSQL/Supabase specifics
- Can use Supabase REST API for authentication while using PostgreSQL connection for data access

**Alternatives Considered**:
- **Direct Supabase REST API**: Would create vendor lock-in; violates database abstraction requirement
- **JOOQ**: Type-safe SQL; however, less abstraction, more vendor-specific code
- **MyBatis**: XML-based SQL mapping; however, more boilerplate, less abstraction than JPA

**Decision**: Spring Data JPA with PostgreSQL driver connecting to Supabase PostgreSQL instance

### Decision 6: Authentication - Spring Security with Supabase JWT Validation

**Rationale**:
- Spring Security is enterprise-standard for Java authentication/authorization
- Supabase provides JWT (JSON Web Token) based authentication
- Spring Security OAuth2 Resource Server can validate Supabase JWT tokens
- Custom JwtDecoder can verify tokens against Supabase public keys
- Enables use of Supabase Auth UI while maintaining Spring Security benefits
- Supports future extension to other auth providers (OAuth2, SAML, etc.)

**Alternatives Considered**:
- **Supabase Client Library**: JavaScript-focused; no mature Java client with Spring integration
- **Custom JWT validation**: Reinventing the wheel; Spring Security provides battle-tested solution
- **Spring Security with separate auth database**: Duplicates Supabase user management

**Decision**: Spring Security 6.x with OAuth2 Resource Server for Supabase JWT validation

### Decision 7: Testing Strategy - JUnit 5, Mockito, Spring Test, Vaadin TestBench

**Rationale**:
- **JUnit 5** (Jupiter): Modern Java testing framework with improved assertions, parameterized tests, and extension model
- **Mockito**: Industry-standard mocking framework for unit tests
- **Spring Test**: Testing support for Spring applications (MockMvc, TestRestTemplate, test slices)
- **Vaadin TestBench**: Official Vaadin UI testing tool built on Selenium for end-to-end tests
- Testing pyramid: 70% unit (JUnit + Mockito), 25% integration (Spring Test), 5% UI (TestBench)
- Aligns with Constitution Principle IX (Testing Standards)

**Alternatives Considered**:
- **AssertJ**: Better fluent assertions; can be added as enhancement to JUnit 5
- **WireMock**: API mocking; useful for external service testing
- **Testcontainers**: Docker-based integration tests; valuable for database testing with real PostgreSQL

**Decision**: JUnit 5, Mockito, Spring Test, Vaadin TestBench as core; Testcontainers for database integration tests

### Decision 8: UI Component Library - Vaadin Lumo Theme (Material Design Inspired)

**Rationale**:
- Lumo is Vaadin's default modern theme inspired by Material Design principles
- Matches user requirement for Material UI equivalent from React version
- Provides consistent component styling out of the box
- Customizable via CSS variables for branding
- Responsive design support built-in
- No need for external UI library (components included with Vaadin)

**Alternatives Considered**:
- **Material Theme for Vaadin**: Available but Lumo is more actively maintained
- **Custom CSS framework**: Unnecessary complexity; Lumo meets requirements
- **Bootstrap integration**: Possible but conflicts with Vaadin's component architecture

**Decision**: Vaadin Lumo theme with custom CSS variables for branding if needed

### Decision 9: Internationalization (i18n) - Java ResourceBundle + Vaadin I18NProvider

**Rationale**:
- Java ResourceBundle is the standard Java i18n mechanism
- Properties files for each locale: `messages_en.properties`, `messages_ru.properties`
- Vaadin provides `I18NProvider` interface for framework-level i18n integration
- Consistent with Constitution Principle II (Bilingual Documentation)
- Backend can use Spring MessageSource for API error message localization
- Namespace keys match React pattern for consistency: `clusters.list.title`

**Alternatives Considered**:
- **Database-driven i18n**: Overkill for UI labels; better for user-generated content translation
- **Custom JSON translation files**: Non-standard for Java; ResourceBundle is proven approach

**Decision**: Java ResourceBundle with properties files, Vaadin I18NProvider implementation

## Best Practices Research

### Maven Multi-Module Monorepo Structure

**Research Finding**: Maven multi-module projects effectively support monorepo architecture when properly configured:

1. **Parent POM Pattern**:
   - Single parent POM at repository root defines all child modules
   - `<modules>` section lists all packages (core-frt, core-srv, clusters-frt, clusters-srv, etc.)
   - `<dependencyManagement>` section centralizes dependency versions using BOM pattern
   - Child POMs reference parent and declare dependencies without versions

2. **BOM (Bill of Materials) Import**:
   - Import Spring Boot BOM for Spring ecosystem consistency
   - Import Vaadin BOM for Vaadin component versions
   - Define project-specific dependency versions in parent POM
   - Eliminates version conflicts across modules

3. **Inter-Module Dependencies**:
   - Backend modules can depend on other backend modules
   - Frontend modules can depend on backend modules (for shared DTOs)
   - Declare with `<dependency>` using `${project.groupId}` and `${project.version}`

4. **Build Profiles**:
   - Development profile: fast builds, Vaadin dev mode, verbose logging
   - Production profile: optimized Vaadin bundle, minification, error-only logging
   - Use `<profiles>` section in parent POM with activation rules

### Vaadin + Spring Boot Integration Patterns

**Research Finding**: Vaadin and Spring Boot integrate seamlessly with proper configuration:

1. **Single Application Entry Point**:
   - One `@SpringBootApplication` class serves both Vaadin UI and REST API
   - Vaadin views as Spring beans with dependency injection
   - REST controllers coexist with Vaadin routes on same server

2. **Service Layer Sharing**:
   - Backend service classes (`@Service`) used by both REST controllers and Vaadin views
   - Business logic centralized in service layer
   - Vaadin views call services directly (server-side rendering advantage)

3. **Security Integration**:
   - Spring Security protects both REST endpoints and Vaadin routes
   - Annotation-based security: `@PermitAll`, `@RolesAllowed`, `@DenyAll`
   - Login view as Vaadin component with Spring Security authentication

4. **Separate Frontend/Backend Modules**:
   - While Vaadin runs server-side, separating -frt and -srv modules maintains architecture clarity
   - Frontend module contains Vaadin views and UI components
   - Backend module contains services, repositories, and domain logic
   - Frontend depends on backend (compile-time dependency is acceptable for Vaadin)

### Database Abstraction with Spring Data JPA

**Research Finding**: Spring Data JPA provides excellent database abstraction:

1. **Repository Interface Pattern**:
   - Define repository interfaces extending `JpaRepository<Entity, ID>`
   - Spring Data generates implementations automatically
   - No database-specific code in interfaces

2. **Query Methods**:
   - Method name-based queries: `findByName(String name)`, `findByCreatedAtAfter(LocalDateTime date)`
   - `@Query` annotation for custom JPQL (database-agnostic)
   - Native queries only when JPQL insufficient (mark as database-specific)

3. **Entity Relationships**:
   - JPA annotations: `@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@OneToOne`
   - Cascade types for automatic persistence: `CascadeType.ALL`, `CascadeType.PERSIST`
   - Lazy vs. Eager loading strategies

4. **Database Migration**:
   - Flyway or Liquibase for schema versioning
   - Version-controlled SQL migration scripts
   - Automatic migration on application startup

### Supabase Integration Strategy

**Research Finding**: Supabase can be integrated with Spring Boot applications:

1. **Authentication Flow**:
   - User authenticates via Supabase Auth (email/password, OAuth, magic link)
   - Supabase returns JWT access token and refresh token
   - Frontend sends JWT in Authorization header for API requests
   - Spring Security validates JWT signature using Supabase public key

2. **Database Connection**:
   - Supabase provides PostgreSQL connection string
   - Use PostgreSQL JDBC driver with Spring Data JPA
   - Connection pooling with HikariCP (Spring Boot default)
   - Store credentials in environment variables, not in code

3. **Realtime and Storage**:
   - Supabase Realtime (WebSocket subscriptions) can be used separately if needed
   - Supabase Storage for file uploads - accessible via REST API or PostgreSQL direct
   - Not required for initial setup; can be added later

## Pattern Translation Guide (React to Vaadin)

This research documents common React patterns from Universo Platformo React and their Vaadin equivalents:

| React Pattern | Vaadin Equivalent | Notes |
|--------------|-------------------|-------|
| `useState()` hook | Component field + `getUI().access()` | Vaadin components are stateful by default |
| `useEffect()` hook | Component lifecycle methods or `addAttachListener()` | Server-side rendering changes lifecycle |
| `<Button onClick={...}>` | `button.addClickListener(e -> {...})` | Event listeners as lambda expressions |
| `<TextField value={x} onChange={...}>` | `binder.bind(field, Getter, Setter)` | Vaadin Binder for two-way binding |
| React Router `<Route>` | `@Route` annotation on view classes | Type-safe navigation |
| Context API | Spring dependency injection | Services injected into views |
| `fetch()` API calls | Direct service method calls | No HTTP needed (server-side views) |
| Redux/Zustand state | Spring `@Scope("session")` beans | Session-scoped state management |
| React Query cache | Spring Cache abstraction | Server-side caching |
| Material-UI components | Vaadin Lumo components | Similar component names and APIs |

## Open Questions and Future Research

### Resolved Questions:
- ✅ Build tool selection (Maven vs. Gradle) → **Maven selected**
- ✅ Java version (17 vs. 21) → **Java 17 minimum, 21 recommended**
- ✅ Vaadin integration approach (separate modules vs. single module) → **Separate -frt/-srv modules**
- ✅ Authentication flow with Supabase → **Spring Security JWT validation**
- ✅ Database abstraction strategy → **Spring Data JPA with repository pattern**

### Deferred for Future Features:
- **Realtime subscriptions**: Supabase Realtime integration for live updates (needed for collaborative features)
- **File uploads**: Supabase Storage integration for user file uploads (needed for content management features)
- **Caching strategy**: Redis or in-memory caching for performance (needed when performance testing shows bottlenecks)
- **API rate limiting**: Bucket4j or Spring Cloud Gateway rate limiting (needed when user load testing begins)
- **Monitoring**: Prometheus + Grafana for production monitoring (needed for production deployment)
- **CI/CD pipeline**: GitHub Actions workflows for automated testing and deployment (needed after initial features implemented)

## Conclusion

All technical unknowns have been resolved. The technology stack is well-defined:
- **Language**: Java 17+ (recommend Java 21)
- **Build**: Maven multi-module project
- **Frontend**: Vaadin 24.3.x with Lumo theme
- **Backend**: Spring Boot 3.2.x with Spring Framework 6.x
- **Database**: Supabase (PostgreSQL) via Spring Data JPA
- **Authentication**: Spring Security with Supabase JWT validation
- **Testing**: JUnit 5, Mockito, Spring Test, Vaadin TestBench
- **i18n**: Java ResourceBundle with Vaadin I18NProvider

Ready to proceed to Phase 1 (Design & Contracts).
