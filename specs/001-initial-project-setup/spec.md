# Feature Specification: Initial Universo Platformo Java Project Setup

**Feature Branch**: `001-initial-project-setup`  
**Created**: 2025-11-16  
**Status**: Draft  
**Input**: User description: "Setup initial Universo Platformo Java project with Vaadin/Spring stack, following Universo Platformo React concepts"

**Note**: This specification describes the repository setup and organizational structure needed to begin development. While the user request mentions specific technologies (Vaadin/Spring/Java), this spec focuses on the organizational requirements that are technology-independent.

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

### Edge Cases

- What happens when a developer tries to build the project without setting up required environment variables (database credentials)?
- How does the system handle missing or invalid configuration files?
- What if someone tries to create documentation files without following the bilingual requirement?
- How do we ensure new contributors understand both the Universo Platformo React concepts and the implementation differences in this version?
- What if build tool versions are incompatible with the project requirements?

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

### Key Entities

- **Repository Structure**: The organizational framework containing packages, configuration files, and documentation that defines how the project is laid out.
- **Package**: A self-contained module of functionality that can be either frontend or backend, with clear naming conventions and base directory structure.
- **Documentation Set**: A collection of README files in multiple languages (English and Russian) that maintain identical structure and content.
- **Label System**: A categorization scheme for GitHub issues using type, area, and project-specific labels for organization and filtering.
- **Build Configuration**: Settings and files that define how the monorepo is compiled, tested, and packaged.

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

## Implementation Notes

These technical decisions are recorded for implementation but are not part of the specification requirements:
- Technology stack: Vaadin (frontend), Spring (backend), Java
- Build tool: Maven (preferred for Java enterprise applications)
- Target version: Java 17 or later LTS
- Database: Supabase (with support for future databases)
- Authentication: Spring Security with Supabase integration
- UI framework: Vaadin Flow with Lumo theme
- Package naming: `-frt` suffix for frontend, `-srv` suffix for backend
