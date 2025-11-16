<!--
Sync Impact Report - Constitution v1.1.0

Version Change: 1.0.0 → 1.1.0 (Audit-driven clarifications and reference implementation link)

Changes Made:
- ADDED: Reference Implementation section linking to teknokomo/universo-platformo-react
- CLARIFIED: Build Tool preference (Maven for multi-module monorepo)
- ADDED: Implementation Details subsection (Authentication, UI Theme, Supabase Integration)
- FIXED: Bilingual documentation compliance (created README-RU.md)

Principles Established (unchanged):
- I. Monorepo Package Architecture
- II. Bilingual Documentation (NON-NEGOTIABLE)
- III. Database Abstraction
- IV. GitHub Workflow Compliance (NON-NEGOTIABLE)
- V. Technology Stack Integrity
- VI. Specification-Driven Development (NON-NEGOTIABLE)

Templates Status:
✅ plan-template.md - Aligned with all six principles
✅ spec-template.md - Aligned with specification principles
✅ tasks-template.md - Aligned with task organization principles
⚠ Command files in .specify/templates/commands/ - Not present in current repository structure

Audit Findings Addressed:
✅ Added explicit React repository reference
✅ Clarified Maven vs Gradle decision
✅ Documented Spring Security + Supabase integration approach
✅ Specified Vaadin Lumo theme strategy
✅ Created missing README-RU.md (bilingual compliance)

Follow-up TODOs:
- Monitor alignment as project evolves and React reference implementation changes
- Consider adding security and performance principles as project matures
- Create .specify/templates/commands/ directory when command templates are needed
- Verify GitHub labels exist in repository
- Begin first feature specification (suggested: Clusters functionality)

Rationale for Version 1.1.0:
- MINOR version increment for clarifications and additions
- Added Implementation Details without changing core principles
- Enhanced Technology Stack Standards section
- Addressed audit findings from deep review process
- Constitution remains backward-compatible with v1.0.0
-->

# Universo Platformo Java Constitution

## Reference Implementation

This Java implementation is based on the concepts from **Universo Platformo React** reference implementation:
- **Repository**: https://github.com/teknokomo/universo-platformo-react
- **Purpose**: Provides conceptual guidance for feature structure and organization
- **Note**: The React implementation is partially complete and contains legacy Flowise code that will be removed
- **Adaptation**: Best practices from Java/Vaadin/Spring ecosystem take precedence over direct React pattern porting

**Monitoring Strategy**: Regularly analyze the React repository for new functionality to implement in this Java version, adapting patterns to Java ecosystem conventions.

## Core Principles

### I. Monorepo Package Architecture

All functionality MUST be organized into discrete packages within a monorepo structure:

- Packages reside in `packages/` directory at repository root
- Frontend and backend functionality MUST be separated into distinct packages (e.g., `packages/clusters-frt` and `packages/clusters-srv`)
- Each package MUST contain a root `base/` folder to support future multiple implementations
- Package management MUST support dependency isolation and independent versioning
- Packages MUST be self-contained with clear boundaries and minimal cross-package coupling

**Rationale**: The monorepo structure enables modular development while maintaining unified versioning and build coordination. The `base/` folder requirement ensures extensibility when multiple technology implementations are needed for the same functional domain.

### II. Bilingual Documentation (NON-NEGOTIABLE)

All documentation MUST be provided in both English and Russian:

- English is the primary standard and MUST be written first
- Russian translation MUST exactly mirror English content in structure and line count
- File naming convention: `README.md` (English) and `README-RU.md` (Russian)
- Both versions MUST be updated atomically in the same commit
- Verification MUST confirm identical structure and line count between language versions
- This principle applies to ALL documentation: README files, specification documents, GitHub Issues descriptions, and inline code comments where appropriate

**Rationale**: Bilingual documentation ensures accessibility for the primary stakeholder community spanning English and Russian speakers. Identical structure requirement prevents documentation drift and ensures consistency.

### III. Database Abstraction

Database access MUST be abstracted to support multiple database backends:

- Supabase is the PRIMARY database implementation for initial development
- Data access layer MUST use abstraction patterns (repositories, DAOs) that do not leak vendor-specific APIs
- Database-specific code MUST be isolated to adapter/implementation layers
- Core business logic MUST remain database-agnostic
- Future DBMS support (PostgreSQL, MySQL, MongoDB, etc.) MUST be achievable by implementing new adapters without modifying business logic

**Rationale**: While Supabase is the current choice, the project must remain flexible for future database requirements. Proper abstraction prevents vendor lock-in and enables testing with alternative backends.

### IV. GitHub Workflow Compliance (NON-NEGOTIABLE)

All development work MUST follow strict GitHub workflow procedures:

- Issues MUST be created before implementation work begins
- Issues MUST follow the template in `.github/instructions/github-issues.md` with bilingual content
- Labels MUST be applied according to `.github/instructions/github-labels.md` using only existing repository labels
- Pull Requests MUST follow the template in `.github/instructions/github-pr.md`
- PRs MUST reference the originating Issue number
- Specification work MUST precede implementation work
- All documentation updates MUST follow `.github/instructions/i18n-docs.md` guidelines

**Rationale**: Consistent workflow enables traceability, proper project management, and maintainable history. The Issue-first approach ensures all work is planned and tracked.

### V. Technology Stack Integrity

Technology choices MUST align with Java/Vaadin/Spring ecosystem best practices:

- Frontend MUST use Vaadin framework following its component architecture patterns
- Backend MUST use Spring Framework (Spring Boot, Spring Data, Spring Security)
- Do NOT port React-specific patterns or legacy code from Universo Platformo React
- Authentication MUST use Spring Security with appropriate connectors (e.g., Supabase integration)
- UI components SHOULD follow Material Design principles adapted to Vaadin
- Build and dependency management MUST use standard Java tooling (Maven or Gradle)
- Package management within monorepo SHOULD leverage Java multi-module project patterns

**Rationale**: The Java implementation must embrace Java ecosystem best practices rather than mimicking JavaScript patterns. This ensures maintainability, performance, and community support alignment.

### VI. Specification-Driven Development (NON-NEGOTIABLE)

Implementation MUST NOT begin without proper specification:

- Specifications MUST be created using templates from `.specify/templates/`
- User stories MUST be prioritized and independently testable
- Technical design MUST be documented before implementation
- Each feature MUST have: plan.md, spec.md, and tasks.md in `/specs/[###-feature-name]/` directory
- Implementation MUST follow task breakdown from tasks.md
- Changes to specifications MUST trigger review and approval before implementation continues

**Rationale**: Specification-first development ensures stakeholder alignment, reduces rework, and provides living documentation. The template-driven approach ensures consistency across features.

## Technology Stack Standards

**Primary Stack**:
- **Language**: Java 17+ (LTS version)
- **Frontend Framework**: Vaadin (current stable version)
- **Backend Framework**: Spring Boot 3.x with Spring Framework 6.x
- **Database**: Supabase (PostgreSQL-based) with abstracted data access
- **Build Tool**: Maven (preferred for monorepo multi-module projects) or Gradle (with multi-project support)
- **Testing**: JUnit 5, Spring Test, Vaadin TestBench

**Architectural Standards**:
- RESTful APIs for backend services
- Component-based UI architecture with Vaadin
- Dependency injection via Spring
- Layered architecture: Presentation → Service → Repository → Database

**Implementation Details**:
- **Authentication**: Spring Security with JWT token validation from Supabase; custom filter chain for token verification
- **UI Theme**: Vaadin Lumo theme (default) with Material Design-inspired customizations where appropriate
- **Supabase Integration**: REST API client for authentication and database operations, wrapped in repository abstraction layer

**Prohibited**:
- Do NOT create `.github/agents/` or AI agent configuration files (user will create when needed)
- Do NOT create separate `docs/` directory (documentation will be externalized in future)
- Do NOT replicate unfinished or legacy patterns from Universo Platformo React reference implementation

## Development Workflow

**Issue Creation**:
1. Analyze Universo Platformo React for new functionality to replicate
2. Create GitHub Issue following `.github/instructions/github-issues.md`
3. Apply labels per `.github/instructions/github-labels.md`
4. Issue must include both English and Russian descriptions

**Specification Phase**:
1. Create feature specification using `.specify/templates/spec-template.md`
2. Create implementation plan using `.specify/templates/plan-template.md`
3. Generate task breakdown using `.specify/templates/tasks-template.md`
4. Obtain stakeholder approval on specifications

**Implementation Phase**:
1. Create feature branch from main
2. Implement tasks in priority order (P1 → P2 → P3)
3. Write tests first where applicable (test-driven approach preferred but not mandatory)
4. Update both English and Russian documentation atomically
5. Create Pull Request following `.github/instructions/github-pr.md`

**Review & Merge**:
1. Verify constitution compliance (all principles)
2. Verify bilingual documentation completeness
3. Verify GitHub workflow compliance
4. Obtain code review approval
5. Merge to main branch

## Governance

**Supremacy**: This constitution supersedes all other development practices, guidelines, or conventions. In case of conflict between this constitution and other documents, the constitution prevails.

**Amendments**:
- Amendments require explicit documentation of rationale
- Version MUST be incremented per semantic versioning:
  - MAJOR: Backward-incompatible governance changes, principle removals/redefinitions
  - MINOR: New principles added, material expansions to existing principles
  - PATCH: Clarifications, wording improvements, non-semantic refinements
- Amended constitution MUST include Sync Impact Report documenting changes
- All dependent templates and documentation MUST be updated to reflect amendments

**Compliance Verification**:
- All Pull Requests MUST verify compliance with constitution principles
- Constitution violations MUST be justified and documented if approved (complexity tracking in plan.md)
- Reviewers MUST explicitly confirm constitution compliance before approval

**Living Document**:
- Constitution should evolve as project matures
- Regular reviews (quarterly) to assess principle effectiveness
- Community feedback on governance effectiveness is encouraged

**Version**: 1.1.0 | **Ratified**: 2025-11-16 | **Last Amended**: 2025-11-16
