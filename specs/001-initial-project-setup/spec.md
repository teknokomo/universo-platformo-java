# Feature Specification: Initial Universo Platformo Java Project Setup

**Feature Branch**: `001-initial-project-setup`  
**Created**: 2025-11-16  
**Status**: Draft  
**Input**: User description: "Setup initial Universo Platformo Java project with Vaadin/Spring stack, following Universo Platformo React concepts"

**Note**: This specification describes the repository setup and organizational structure needed to begin development. While the user request mentions specific technologies (Vaadin/Spring/Java), this spec focuses on the organizational requirements that are technology-independent.

## Reference Implementation Context

This project is based on the **Universo Platformo React** reference implementation:
- **Repository**: https://github.com/teknokomo/universo-platformo-react
- **Relationship**: This Java implementation follows the same conceptual architecture but adapted for Java/Vaadin/Spring ecosystem
- **Monitoring Requirement**: Regular analysis of React repository for new features to implement in this Java version
- **Adaptation Strategy**: Translate React/Express patterns into Vaadin/Spring patterns while maintaining conceptual consistency

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Repository Foundation Setup (Priority: P1)

As a developer setting up Universo Platformo Java, I need a properly structured repository with clear documentation so that I can understand the project architecture and begin development work.

**Why this priority**: Without a proper repository foundation, no development can begin. This is the absolute minimum needed to start any work on the project.

**Independent Test**: Can be fully tested by cloning the repository, reading the README files (both English and Russian versions), and verifying that the project structure follows enterprise application conventions for monorepo projects.

**Acceptance Scenarios**:

1. **Given** an empty repository, **When** the setup is complete, **Then** the repository contains a comprehensive English README.md explaining the project purpose, architecture, and setup instructions.
2. **Given** the English README exists, **When** checking for internationalization, **Then** a Russian version README-RU.md exists with identical structure and content in Russian.
3. **Given** the repository is set up, **When** examining the project structure, **Then** it follows enterprise monorepo best practices with clearly defined package structure.

---

### User Story 2 - Issue Management System Configuration (Priority: P2)

As a project manager or developer, I need properly configured GitHub labels and issue templates so that I can organize, categorize, and track work items effectively.

**Why this priority**: Once the repository exists, we need to be able to track work. Without labels and proper issue management, coordination becomes chaotic.

**Independent Test**: Can be tested by navigating to the GitHub Issues page and verifying all required labels exist with appropriate descriptions and colors, and that issues can be created following the bilingual template format.

**Acceptance Scenarios**:

1. **Given** the repository exists, **When** viewing GitHub labels, **Then** all base labels (bug, documentation, enhancement, feature) and project-specific labels (platformo, backend, frontend, i18n, architecture) are present.
2. **Given** labels are configured, **When** creating a new issue, **Then** the issue template supports bilingual content with English as primary and Russian in collapsible section.
3. **Given** issue labels exist, **When** filtering issues, **Then** developers can easily find issues by type, area, and priority.

---

### User Story 3 - Application Project Structure Initialization (Priority: P3)

As a developer, I need the basic application project structure with build configuration so that I can start implementing frontend and backend packages.

**Why this priority**: After documentation and issue tracking are in place, we need the actual project scaffolding to begin coding. This provides the foundation for all future feature development.

**Independent Test**: Can be tested by running the build system and verifying that the monorepo structure compiles successfully with placeholder packages for frontend and backend.

**Acceptance Scenarios**:

1. **Given** the repository structure is defined, **When** examining the packages directory, **Then** it contains a base structure with separate frontend and backend package directories.
2. **Given** the application project exists, **When** running the build command, **Then** the project compiles without errors even with minimal placeholder code.
3. **Given** the monorepo structure exists, **When** adding a new package, **Then** clear conventions exist for naming packages to distinguish frontend and backend components.

---

### User Story 4 - Database and Authentication Foundation (Priority: P4)

As a developer, I need the basic configuration for Supabase integration and authentication setup so that future features can build upon a consistent data access and security layer.

**Why this priority**: While not immediately needed for repository setup, having the foundation for database access and authentication allows subsequent feature development to proceed smoothly.

**Independent Test**: Can be tested by verifying configuration files exist for Supabase connection and authentication, with clear documentation on how to set up credentials (without committing secrets).

**Acceptance Scenarios**:

1. **Given** the application project structure exists, **When** reviewing configuration files, **Then** database connection configuration templates are present with environment variable placeholders.
2. **Given** authentication setup is documented, **When** a developer reads the setup guide, **Then** they understand how to configure authentication with the chosen database backend.
3. **Given** the database foundation exists, **When** planning future features, **Then** developers know the patterns for adding new database entities and queries.

---

### User Story 5 - Feature Pattern Foundation (Priority: P2)

As a developer, I need clear documentation of the standard entity relationship patterns so that I can implement features consistently across the platform.

**Why this priority**: Establishing feature patterns early ensures consistency as the project grows and multiple features are developed.

**Independent Test**: Can be tested by reviewing the documentation and verifying it clearly describes the Clusters/Domains/Resources pattern and its variations.

**Acceptance Scenarios**:

1. **Given** the repository documentation exists, **When** reviewing feature patterns, **Then** the standard three-tier entity structure (like Clusters/Domains/Resources) is clearly documented.
2. **Given** the feature pattern is documented, **When** planning new features, **Then** developers can identify which pattern variation applies to their feature.
3. **Given** multiple features are implemented, **When** reviewing code, **Then** all features follow consistent entity relationship patterns appropriate to their domain.

---

### User Story 6 - React Repository Integration Process (Priority: P3)

As a project maintainer, I need a documented process for monitoring the React repository and adapting new features so that the Java implementation stays aligned with the reference implementation.

**Why this priority**: Without a clear process, the Java implementation may diverge from the conceptual design or miss important features.

**Independent Test**: Can be tested by verifying documentation exists describing how to monitor the React repo, select features for porting, and document adaptation decisions.

**Acceptance Scenarios**:

1. **Given** the integration process is documented, **When** a new feature appears in the React repo, **Then** developers know how to evaluate it for inclusion in the Java version.
2. **Given** a feature is selected for porting, **When** implementing it, **Then** clear guidelines exist for adapting React/Express patterns to Vaadin/Spring.
3. **Given** features are ported, **When** documenting the implementation, **Then** differences and adaptation decisions are recorded for future reference.

---

### Edge Cases

- What happens when a developer tries to build the project without setting up required environment variables (database credentials)?
- How does the system handle missing or invalid configuration files?
- What if someone tries to create documentation files without following the bilingual requirement?
- How do we ensure new contributors understand both the Universo Platformo React concepts and the implementation differences in this version?
- What if build tool versions are incompatible with the project requirements?
- How do we handle features in the React repo that cannot be directly adapted to Java/Vaadin architecture?
- What happens when the React repository introduces breaking changes to the conceptual model?
- How do we maintain feature parity if the React implementation removes or significantly changes existing features?
- What if the standard entity pattern doesn't fit a particular feature's requirements?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Repository MUST contain a comprehensive README.md file in English that explains the project purpose, architecture overview, technology stack, and setup instructions.
- **FR-002**: Repository MUST contain a README-RU.md file that is an exact translation of README.md with identical structure and line count.
- **FR-003**: Repository MUST have GitHub labels configured including type labels (bug, feature, enhancement, documentation), area labels (frontend, backend, i18n), and project-specific labels (platformo, architecture, repository).
- **FR-004**: Repository MUST follow monorepo structure with a `packages/` directory containing separate packages for frontend and backend components.
- **FR-005**: Package naming MUST follow a clear convention for distinguishing between frontend and backend packages.
- **FR-006**: Each package MUST contain a `base/` directory at its root to support future multiple implementations.
- **FR-007**: Project MUST use a build tool suitable for monorepo management.
- **FR-008**: Documentation MUST NOT include an AI agents rules directory (users will create their own).
- **FR-009**: Documentation MUST NOT include a `docs/` directory (to be hosted separately at docs.universo.pro).
- **FR-010**: Repository MUST include configuration templates for database connection without hardcoded credentials.
- **FR-011**: Repository MUST include setup instructions for authentication with database integration.
- **FR-012**: All documentation files MUST follow the bilingual pattern: create English version first, then Russian version with identical structure.
- **FR-013**: Issue templates MUST support bilingual content with English as primary text and Russian translation in `<details><summary>In Russian</summary>` collapsible section.
- **FR-014**: Pull Request templates MUST support bilingual content following the same pattern as issues.
- **FR-015**: Project structure MUST be designed to support future addition of multiple database management systems.
- **FR-016**: Repository MUST include documentation of standard entity relationship patterns, specifically the three-tier pattern (e.g., Clusters/Domains/Resources).
- **FR-017**: Repository MUST document pattern variations for different feature types (e.g., Metaverses/Sections/Entities, Uniks with extended hierarchies).
- **FR-018**: Repository MUST include a process document for monitoring the Universo Platformo React repository for new features.
- **FR-019**: Repository MUST include guidelines for adapting React/Express patterns to Vaadin/Spring architecture.
- **FR-020**: Package structure MUST support multi-module build configuration with parent-level dependency management.
- **FR-021**: Repository MUST document the layered architecture pattern: Presentation Layer → Service Layer → Repository Layer → Database Layer.
- **FR-022**: Repository MUST include guidelines for implementing CRUD operations consistently across all features.
- **FR-023**: Repository MUST document the UI component hierarchy and Material Design adaptation for Vaadin.
- **FR-024**: Repository MUST include authentication flow documentation showing Spring Security integration with database backend.
- **FR-025**: Repository MUST document the data access abstraction layer that enables multiple DBMS support.
- **FR-026**: Repository MUST include testing strategy documentation covering unit, integration, and UI testing approaches.
- **FR-027**: Repository MUST document build process requirements including compilation, testing, and packaging steps.
- **FR-028**: Repository MUST include environment configuration guidelines showing how to set up development, staging, and production environments.
- **FR-029**: Feature implementations MUST follow documented entity relationship patterns unless explicitly justified in specification.
- **FR-030**: All features ported from React repository MUST document adaptation decisions and differences from original implementation.

### Key Entities

- **Repository Structure**: The organizational framework containing packages, configuration files, and documentation that defines how the project is laid out.
- **Package**: A self-contained module of functionality that can be either frontend or backend, with clear naming conventions and base directory structure.
- **Documentation Set**: A collection of README files in multiple languages (English and Russian) that maintain identical structure and content.
- **Label System**: A categorization scheme for GitHub issues using type, area, and project-specific labels for organization and filtering.
- **Build Configuration**: Settings and files that define how the monorepo is compiled, tested, and packaged.
- **Entity Pattern**: A reusable three-tier relationship structure (primary/secondary/tertiary entities) that provides consistent data modeling across features.
- **Feature Port**: A feature from the React reference implementation that has been adapted and implemented in the Java/Vaadin/Spring stack.
- **Abstraction Layer**: A software design layer that isolates business logic from infrastructure concerns (database, authentication, external services).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: New developers can clone the repository, read the README files, and understand the project architecture within 15 minutes.
- **SC-002**: All documentation follows the bilingual requirement with English and Russian versions having identical structure and line count.
- **SC-003**: GitHub issues can be created and properly categorized using the configured label system with at least 10 distinct labels available.
- **SC-004**: The application project structure can be built successfully with the chosen build tool without errors on a clean environment.
- **SC-005**: Repository structure clearly separates frontend and backend concerns through package naming and organization.
- **SC-006**: Database configuration can be set up by a developer following the documentation in under 10 minutes.
- **SC-007**: No secrets or credentials are committed to the repository, with all sensitive configuration using environment variables or external configuration files.
- **SC-008**: Package naming conventions are documented and followed consistently, making it easy to distinguish between frontend and backend packages.
- **SC-009**: The repository structure supports future expansion with additional packages and database integrations without requiring architectural changes.
- **SC-010**: All bilingual documentation can be validated to have matching line counts and section structures between English and Russian versions.
- **SC-011**: Standard entity relationship patterns are documented with at least 3 concrete examples (e.g., Clusters/Domains/Resources, Metaverses/Sections/Entities, Uniks hierarchies).
- **SC-012**: React repository monitoring process is documented with clear criteria for feature selection and adaptation guidelines.
- **SC-013**: Multi-module build structure supports independent package versioning and parallel development by multiple teams.
- **SC-014**: Data access abstraction layer can be validated by demonstrating that business logic has no direct database vendor dependencies.
- **SC-015**: Authentication implementation can be set up and tested by a developer in under 30 minutes following the documentation.
- **SC-016**: Testing strategy documentation covers all three levels: unit tests (70%+ coverage), integration tests (key workflows), and UI tests (critical user paths).
- **SC-017**: New features can be implemented by following documented patterns without requiring architectural decisions from senior developers.
- **SC-018**: Feature porting from React repository to Java implementation takes no more than 50% longer than the original React implementation time after the first feature is completed.
- **SC-019**: Build process executes successfully with clear error messages when dependencies or environment variables are missing.
- **SC-020**: All architectural patterns (layering, abstractions, entity relationships) are documented with diagrams or code examples.

## Assumptions

1. The project will use an enterprise-grade build tool with good monorepo support through multi-module projects.
2. The project will target current LTS versions of the chosen programming language and frameworks.
3. Initial setup will focus on one primary database solution, with architecture designed to support other databases in the future.
4. The frontend framework will provide a cohesive development experience consistent with the backend language.
5. The backend framework will follow industry-standard patterns for enterprise application development.
6. The project will follow a "packages" directory structure similar to modern monorepos, adapted for the chosen language conventions.
7. Authentication will use standard security libraries with custom integration for the chosen database backend.
8. UI components will use framework-native components or compatible design systems.
9. The repository will use Git for version control and GitHub for hosting, issues, and pull requests.
10. All developers working on the project will have access to both English and Russian documentation, but English will be the primary language for code and technical communication.
11. The standard entity pattern (three-tier hierarchy) will be sufficient for 80%+ of features, with justified exceptions documented in feature specifications.
12. Feature development velocity will improve over time as patterns and abstractions mature.
13. The React reference implementation will continue to evolve, requiring periodic reviews to identify new features.
14. Some React features may not be directly portable to Java/Vaadin and will require significant adaptation or alternative approaches.
15. Testing will follow industry-standard practices with focus on critical paths rather than 100% coverage.
16. Advanced features (Spaces/Canvases, node systems, UPDL, LangChain integration) will be added after core functionality is stable.
17. The project will prioritize stability and maintainability over feature parity with React version in the initial phases.

## Feature Pattern Standards *(mandatory)*

### Standard Entity Relationship Pattern

Most features in Universo Platformo follow a three-tier entity relationship pattern that MUST be documented and followed:

**Primary Pattern Example: Clusters Feature**
- **Tier 1 (Primary)**: Clusters - Top-level organizational units
- **Tier 2 (Secondary)**: Domains - Mid-level groupings within Clusters
- **Tier 3 (Tertiary)**: Resources - Individual items within Domains

**Relationship Characteristics**:
- One-to-many relationships between tiers (1 Cluster → many Domains → many Resources)
- Each tier has CRUD operations
- Each tier has its own UI views (list, detail, create, edit)
- Each tier has its own data access layer

**Pattern Variations**:

1. **Metaverses Feature** (Same structure, different domain)
   - Tier 1: Metaverses
   - Tier 2: Sections
   - Tier 3: Entities

2. **Uniks Feature** (Extended hierarchy)
   - May include additional tiers beyond the standard three
   - Follows same principles but with deeper nesting

3. **Simplified Features** (Fewer tiers)
   - Some features may only use 1-2 tiers
   - Must be justified in feature specification

**Documentation Requirements**:
- Each feature specification MUST identify which pattern it follows
- Variations from standard pattern MUST be explicitly justified
- Entity relationships MUST be clearly diagrammed
- CRUD operations MUST be defined for each tier

### Technical Architecture Requirements

**Layered Architecture Pattern**:

1. **Presentation Layer** (Frontend packages with -frt suffix)
   - Vaadin views and components
   - User interaction handling
   - Client-side validation
   - No direct database access

2. **Service Layer** (Backend packages with -srv suffix)
   - Business logic implementation
   - Transaction management
   - Cross-entity operations
   - Coordinates multiple repositories

3. **Repository Layer** (Within backend packages)
   - Data access abstraction
   - CRUD operations
   - Query implementations
   - Database-agnostic interfaces

4. **Database Layer**
   - Actual database (Supabase initially)
   - Connection management
   - Schema definitions

**Package Structure Requirements**:

```
packages/
├── core-frt/              # Core frontend package
│   └── base/              # Base implementation
│       ├── src/
│       │   └── main/java/
│       └── pom.xml
├── core-srv/              # Core backend package
│   └── base/              # Base implementation
│       ├── src/
│       │   ├── main/java/
│       │   │   ├── controller/    # REST endpoints
│       │   │   ├── service/       # Business logic
│       │   │   ├── repository/    # Data access
│       │   │   └── model/         # Domain entities
│       │   └── resources/
│       └── pom.xml
├── clusters-frt/          # Clusters frontend
│   └── base/
├── clusters-srv/          # Clusters backend
│   └── base/
└── [feature]-frt/srv      # Additional features
```

**Multi-Module Build Configuration**:
- Parent POM/build file at repository root
- Each package is an independent module
- Shared dependency versions managed at parent level
- Inter-package dependencies declared explicitly

**Data Access Abstraction Requirements**:

The repository layer MUST provide database abstraction:

1. **Interface-Based Repositories**
   - Define repository interfaces without database specifics
   - Use standard CRUD method naming
   - Return domain entities, not database DTOs

2. **Implementation Isolation**
   - Database-specific code isolated in adapter/implementation classes
   - Supabase client code contained within repository implementations
   - Business logic never imports database-specific libraries

3. **Configuration Externalization**
   - Database connections via environment variables or configuration files
   - No hardcoded credentials or connection strings
   - Support for multiple configuration profiles (dev, staging, prod)

### React Repository Integration Process *(mandatory)*

**Monitoring Process**:

1. **Regular Analysis Schedule**
   - Review React repository monthly (or after major releases)
   - Check commit history, pull requests, and issues
   - Identify new features, architectural changes, and bug fixes

2. **Feature Selection Criteria**
   - **Priority 1**: Core features that affect all users
   - **Priority 2**: Features that extend existing functionality
   - **Priority 3**: Nice-to-have features that can be deferred

3. **Evaluation Questions**
   - Is this feature applicable to the Java/Vaadin implementation?
   - Does this feature conflict with Java ecosystem best practices?
   - What adaptation is required to implement this in Java/Vaadin/Spring?
   - What is the effort estimate compared to the React implementation?

**Adaptation Guidelines**:

1. **Direct Porting** (Preferred when possible)
   - Feature can be implemented with similar structure
   - React components → Vaadin components
   - Express endpoints → Spring REST controllers
   - Same business logic and user experience

2. **Adapted Porting** (When direct porting isn't feasible)
   - Core concept is preserved
   - Implementation differs due to platform capabilities
   - User experience may vary but serves same purpose
   - Document why direct porting wasn't possible

3. **Alternative Implementation** (When adaptation required)
   - React feature uses platform-specific capability not available in Java/Vaadin
   - Implement equivalent functionality using Java ecosystem tools
   - Document the alternative approach and rationale
   - Ensure end-user value is preserved

**Documentation Requirements**:

1. **Feature Comparison Document** (for each ported feature)
   - React implementation summary
   - Java implementation summary  
   - Key differences and why
   - Lessons learned

2. **Porting Decision Log**
   - Record all features evaluated from React repo
   - Decision: Port, Adapt, Skip, or Defer
   - Rationale for decision
   - Date evaluated

3. **Pattern Translation Guide**
   - Common React patterns and their Vaadin equivalents
   - Common Express patterns and their Spring equivalents
   - Anti-patterns to avoid when porting

## Testing Strategy *(mandatory)*

### Testing Pyramid

**Unit Tests (70% of tests)**:
- Test individual methods and classes in isolation
- Mock dependencies using testing frameworks
- Fast execution (entire suite under 1 minute)
- Cover business logic, calculations, validations

**Integration Tests (25% of tests)**:
- Test service layer with real repository implementations
- Test REST endpoints with real database (test database)
- Validate transactions and multi-step workflows
- Execution time: 5-10 minutes for full suite

**UI Tests (5% of tests)**:
- Test critical user paths with Vaadin TestBench
- Focus on primary workflows (create entity, edit entity, delete entity)
- Browser-based testing for key interactions
- Execution time: 10-15 minutes for full suite

### Testing Requirements

- **FR-TEST-001**: Each service class MUST have corresponding unit tests
- **FR-TEST-002**: Each REST endpoint MUST have integration tests
- **FR-TEST-003**: Each feature MUST have at least one end-to-end UI test for primary workflow
- **FR-TEST-004**: Test coverage for service layer MUST be above 70%
- **FR-TEST-005**: All tests MUST pass before merging to main branch
- **FR-TEST-006**: Tests MUST be runnable in CI/CD environment
- **FR-TEST-007**: Tests MUST use test doubles (mocks, stubs) to avoid external dependencies

### Build and Deployment Requirements *(mandatory)*

**Build Process Requirements**:

1. **Compilation**
   - All packages MUST compile without errors
   - Compilation MUST complete in under 2 minutes for incremental builds
   - Compilation MUST complete in under 10 minutes for clean builds

2. **Dependency Resolution**
   - All dependencies MUST be resolved from public repositories
   - Private dependencies MUST be documented with access instructions
   - Dependency conflicts MUST be resolved at parent POM level

3. **Testing**
   - All tests MUST run automatically during build
   - Failed tests MUST fail the build
   - Test results MUST be reported in standard format (JUnit XML)

4. **Packaging**
   - Each package MUST produce deployable artifacts
   - Frontend and backend MUST be packaged separately
   - Package versions MUST follow semantic versioning

**Environment Configuration Requirements**:

1. **Development Environment**
   - Local database setup (Supabase local instance or test database)
   - Hot reload enabled for rapid development
   - Debug logging enabled
   - Sample data available for testing

2. **Staging Environment**
   - Mirror of production configuration
   - Separate database instance
   - Info-level logging
   - Used for pre-release validation

3. **Production Environment**
   - Production database connection
   - Error-level logging only
   - Performance monitoring enabled
   - Backup and recovery procedures documented

**Configuration Management**:

- All environment-specific values MUST use environment variables
- Configuration files MUST NOT contain secrets
- Template configuration files MUST be provided with placeholders
- Documentation MUST explain all required environment variables

## Implementation Notes

These technical decisions are recorded for implementation but are not part of the specification requirements:
- Technology stack: Vaadin (frontend), Spring (backend), Java
- Build tool: Maven (preferred for Java enterprise applications)
- Target version: Java 17 or later LTS
- Database: Supabase (with support for future databases)
- Authentication: Spring Security with Supabase integration
- UI framework: Vaadin Flow with Lumo theme
- Package naming: `-frt` suffix for frontend, `-srv` suffix for backend
