# Implementation Plan: Initial Universo Platformo Java Project Setup

**Branch**: `001-initial-project-setup` | **Date**: 2025-11-17 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-initial-project-setup/spec.md`

**Note**: This document outlines the implementation plan for setting up the initial Universo Platformo Java project structure with Vaadin/Spring stack.

## Summary

This feature establishes the foundational repository structure, documentation framework, and organizational standards for the Universo Platformo Java project. It includes creating bilingual README files, configuring GitHub labels and issue templates, setting up the monorepo structure with Maven multi-module build system, providing database and authentication configuration templates, and documenting standard entity relationship patterns. The project follows the conceptual architecture from Universo Platformo React but adapted for Java/Vaadin/Spring ecosystem best practices.

## Technical Context

**Language/Version**: Java 17+ LTS (Java 21 recommended for production)  
**Primary Dependencies**: Spring Boot 3.2.x, Vaadin 24.3.x, Spring Security 6.x, Spring Data JPA  
**Storage**: Supabase (PostgreSQL-based) with abstracted data access layer supporting future DBMS
**Testing**: JUnit 5, Spring Test, Mockito, Vaadin TestBench for UI tests  
**Target Platform**: JVM-based servers (Linux/Windows), containerized deployment (Docker/Kubernetes)
**Project Type**: Full-stack web application (Vaadin frontend + Spring backend in monorepo)  
**Performance Goals**: Repository setup has no runtime performance goals; future features target <500ms response time  
**Constraints**: Must support bilingual documentation, must abstract database access, must follow monorepo structure  
**Scale/Scope**: Initial project setup (0 users, foundational structure for future 10+ feature modules)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

Verify compliance with Universo Platformo Java Constitution (see `.specify/memory/constitution.md`):

- [x] **I. Monorepo Package Architecture**: Feature will establish `packages/` directory with frontend (-frt) and backend (-srv) separation, each with `base/` folder structure
- [x] **II. Bilingual Documentation (NON-NEGOTIABLE)**: All README files will be created in English first, then Russian (README.md + README-RU.md with identical structure)
- [x] **III. Database Abstraction**: Configuration templates will demonstrate abstracted patterns using Spring Data JPA with repository interfaces, isolating Supabase-specific code
- [x] **IV. GitHub Workflow Compliance (NON-NEGOTIABLE)**: This work follows spec-before-implementation; will create labels following `.github/instructions/github-labels.md`
- [x] **V. Technology Stack Integrity**: Uses Vaadin/Spring patterns exclusively; no React legacy code ported; follows Java enterprise best practices
- [x] **VI. Specification-Driven Development (NON-NEGOTIABLE)**: Full spec exists (spec.md); this plan.md being created; tasks.md will be generated before implementation
- [x] **VII. Feature Pattern Consistency (NON-NEGOTIABLE)**: This feature documents the standard three-tier pattern (Primary/Secondary/Tertiary) for future features to follow
- [x] **VIII. React Repository Alignment (NON-NEGOTIABLE)**: Documentation will include process for monitoring React repository and adapting new features
- [x] **IX. Testing Standards (NON-NEGOTIABLE)**: Testing strategy and framework documentation will be included; no runtime code in this setup phase

**Violations requiring justification**: None. This is a foundational setup feature that establishes the infrastructure for all constitution principles.

## Project Structure

### Documentation (this feature)

```text
specs/[###-feature]/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```text
# Maven Multi-Module Monorepo Structure
universo-platformo-java/
├── pom.xml                           # Parent POM with BOM dependency management
├── README.md                         # English documentation
├── README-RU.md                      # Russian documentation
├── .gitignore                        # Git ignore rules
├── .github/
│   ├── instructions/
│   │   ├── github-issues.md         # Issue creation guidelines (bilingual)
│   │   ├── github-labels.md         # Label usage guidelines (bilingual)
│   │   ├── github-pr.md             # PR guidelines (bilingual)
│   │   └── i18n-docs.md             # Documentation i18n guidelines
│   └── workflows/                    # CI/CD workflows (future)
├── specs/                            # Feature specifications
│   └── 001-initial-project-setup/
│       ├── spec.md
│       ├── plan.md
│       └── tasks.md
├── packages/
│   ├── core-frt/                    # Core frontend package
│   │   ├── README.md                # English
│   │   ├── README-RU.md             # Russian
│   │   ├── pom.xml                  # Module POM
│   │   └── base/
│   │       └── src/
│   │           ├── main/java/pro/universo/core/ui/
│   │           │   ├── views/
│   │           │   ├── components/
│   │           │   └── layouts/
│   │           ├── main/resources/
│   │           │   ├── messages_en.properties
│   │           │   └── messages_ru.properties
│   │           └── test/java/
│   └── core-srv/                    # Core backend package
│       ├── README.md                # English
│       ├── README-RU.md             # Russian
│       ├── pom.xml                  # Module POM
│       └── base/
│           └── src/
│               ├── main/java/pro/universo/core/api/
│               │   ├── controller/
│               │   ├── service/
│               │   ├── repository/
│               │   ├── model/
│               │   ├── dto/
│               │   ├── exception/
│               │   └── config/
│               ├── main/resources/
│               │   ├── application.yml
│               │   ├── application-dev.yml
│               │   ├── application-prod.yml
│               │   └── db/migration/
│               └── test/java/
└── docs/                            # Future: will be moved to separate repository
```

**Structure Decision**: Maven multi-module monorepo pattern selected for Universo Platformo Java. This structure:
- Follows Java enterprise conventions with standard Maven directory layout
- Implements monorepo principle with `packages/` containing independent modules
- Each package has dedicated `base/` folder for future multiple implementations
- Separates frontend (-frt) and backend (-srv) concerns into distinct modules
- Uses parent POM for centralized dependency management (BOM pattern)
- Supports independent package versioning and parallel development
- Java package naming: `pro.universo.{feature}.{layer}` (e.g., `pro.universo.core.api.service`)

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

No constitutional violations. This setup feature establishes the infrastructure that enables all nine constitution principles to be followed by future features.
