<!--
Sync Impact Report - Constitution v2.1.0

Version Change: 2.0.0 → 2.1.0 (Minor update: Strengthened Principle I enforcement)

Changes Made:
- ENHANCED: Principle I - Monorepo Package Architecture now marked as NON-NEGOTIABLE
- ADDED: PROHIBITION clause explicitly forbidding non-modular implementation
- ADDED: ENFORCEMENT clause defining code review verification requirements
- ADDED: REFERENCE PATTERN clause linking to React repository as architectural model
- ENHANCED: Rationale expanded to explain future package extraction requirement

Principles Status:
- I. Monorepo Package Architecture (NON-NEGOTIABLE) **ENHANCED**
- II. Bilingual Documentation (NON-NEGOTIABLE) (unchanged)
- III. Database Abstraction (unchanged)
- IV. GitHub Workflow Compliance (NON-NEGOTIABLE) (unchanged)
- V. Technology Stack Integrity (unchanged)
- VI. Specification-Driven Development (NON-NEGOTIABLE) (unchanged)
- VII. Feature Pattern Consistency (NON-NEGOTIABLE) (unchanged)
- VIII. React Repository Alignment (NON-NEGOTIABLE) (unchanged)
- IX. Testing Standards (NON-NEGOTIABLE) (unchanged)

Rationale for Version 2.1.0:
- MINOR version increment for material expansion to existing principle
- Principle I elevated to NON-NEGOTIABLE status (same level as II, IV, VI, VII, VIII, IX)
- Added explicit prohibition to prevent misunderstanding
- Added enforcement mechanisms for code reviews
- This change makes it IMPOSSIBLE to implement functionality non-modularly
- Aligns with user requirement for UNAMBIGUOUS and UNCONDITIONAL modular architecture

Impact Assessment:
- All future PRs must verify modular architecture compliance
- Code reviews must explicitly check package structure
- No functionality can be added outside packages/ directory
- Strengthens alignment with React repository reference pattern
- Makes future package extraction into separate repositories feasible
- Existing implementation already compliant (no code changes needed)

Follow-up Actions Required:
- Update README.md and README-RU.md with architecture warnings
- Update .github/instructions/github-pr.md with architecture compliance checklist
- Create .github/checklists/architecture-compliance.md for reviewers
- Update spec template to include architecture compliance section
- Verify all changes maintain bilingual consistency

Previous Changes (v2.0.0):
- ADDED: Principle VII - Feature Pattern Consistency (NON-NEGOTIABLE)
- ADDED: Principle VIII - React Repository Alignment (NON-NEGOTIABLE)
- ADDED: Principle IX - Testing Standards (NON-NEGOTIABLE)
- ENHANCED: Reference Implementation section with monitoring strategy
- ENHANCED: Technology Stack Standards with testing frameworks

Previous Changes (v1.0.0 → v1.1.0):
- ADDED: Reference Implementation section linking to teknokomo/universo-platformo-react
- CLARIFIED: Build Tool preference (Maven for multi-module monorepo)
- ADDED: Implementation Details subsection (Authentication, UI Theme, Supabase Integration)
- FIXED: Bilingual documentation compliance (created README-RU.md)

Templates Status:
✅ plan-template.md - Requires update for strengthened Principle I
⚠ spec-template.md - Requires Architecture Compliance section addition
✅ tasks-template.md - Compatible with all principles
⚠ github-pr.md - Requires Architecture Compliance checklist addition
-->

<!--
Sync Impact Report - Constitution v2.0.0

Version Change: 1.1.0 → 2.0.0 (Major update: Added new NON-NEGOTIABLE principles)

Changes Made:
- ADDED: Principle VII - Feature Pattern Consistency (NON-NEGOTIABLE)
- ADDED: Principle VIII - React Repository Alignment (NON-NEGOTIABLE)
- ADDED: Principle IX - Testing Standards (NON-NEGOTIABLE)
- ENHANCED: Reference Implementation section with monitoring strategy
- ENHANCED: Technology Stack Standards with testing frameworks

Principles Established:
- I. Monorepo Package Architecture (changed to NON-NEGOTIABLE in v2.1.0)
- II. Bilingual Documentation (NON-NEGOTIABLE) (unchanged)
- III. Database Abstraction (unchanged)
- IV. GitHub Workflow Compliance (NON-NEGOTIABLE) (unchanged)
- V. Technology Stack Integrity (unchanged)
- VI. Specification-Driven Development (NON-NEGOTIABLE) (unchanged)
- VII. Feature Pattern Consistency (NON-NEGOTIABLE) **NEW**
- VIII. React Repository Alignment (NON-NEGOTIABLE) **NEW**
- IX. Testing Standards (NON-NEGOTIABLE) **NEW**

Rationale for Version 2.0.0:
- MAJOR version increment for addition of three new NON-NEGOTIABLE principles
- These principles fundamentally change development requirements
- All future features must comply with new pattern consistency requirements
- React repository alignment is now mandatory, not optional
- Testing standards are now enforceable requirements
- Breaking change: Existing code may not meet new testing standards
- Constitution now provides comprehensive governance for all development aspects

Impact Assessment:
- Existing features may need testing additions to meet 70% coverage requirement
- All new features must document pattern selection and React alignment
- Monthly React repository reviews are now mandatory
- Code reviews must verify pattern consistency and testing compliance
- Specification template should be updated to include pattern identification section

Follow-up TODOs:
- Update spec template to include Feature Pattern section
- Create React Repository Review checklist template
- Create Testing Compliance checklist for code reviews
- Add pattern validation to PR review process
- Document example implementations of standard patterns
- Create pattern translation guide (React→Java)

Previous Changes (v1.0.0 → v1.1.0):
- ADDED: Reference Implementation section linking to teknokomo/universo-platformo-react
- CLARIFIED: Build Tool preference (Maven for multi-module monorepo)
- ADDED: Implementation Details subsection (Authentication, UI Theme, Supabase Integration)
- FIXED: Bilingual documentation compliance (created README-RU.md)

Templates Status:
✅ plan-template.md - Requires update for new principles
⚠ spec-template.md - Requires Feature Pattern section addition
✅ tasks-template.md - Compatible with all principles
⚠ Command files in .specify/templates/commands/ - Not present in current repository structure
-->

# Universo Platformo Java Constitution

## Reference Implementation

This Java implementation is based on the concepts from **Universo Platformo React** reference implementation:
- **Repository**: https://github.com/teknokomo/universo-platformo-react
- **Purpose**: Provides conceptual guidance for feature structure and organization
- **Note**: The React implementation is partially complete and contains legacy Flowise code that will be removed
- **Adaptation**: Best practices from Java/Vaadin/Spring ecosystem take precedence over direct React pattern porting

**Monitoring Strategy**: Regularly analyze the React repository for new functionality to implement in this Java version, adapting patterns to Java ecosystem conventions.

**Key Reference Documents**:
- **Architecture Analysis**: `.specify/memory/react-architecture-analysis.md` - Comprehensive analysis of React repository patterns and features
- **Pattern Translation**: `.specify/memory/react-to-java-patterns.md` - React to Java/Spring/Vaadin pattern translations with code examples
- **System Patterns**: React repository's `memory-bank/systemPatterns.md` contains proven patterns (RLS, i18n, pagination, testing)

## Core Principles

### I. Monorepo Package Architecture (NON-NEGOTIABLE)

All functionality MUST be organized into discrete packages within a monorepo structure:

- Packages reside in `packages/` directory at repository root
- Frontend and backend functionality MUST be separated into distinct packages (e.g., `packages/clusters-frt` and `packages/clusters-srv`)
- Each package MUST contain a root `base/` folder to support future multiple implementations
- Package management MUST support dependency isolation and independent versioning
- Packages MUST be self-contained with clear boundaries and minimal cross-package coupling

**PROHIBITION**: Functionality MUST NOT be implemented outside the `packages/` directory structure. Common infrastructure files (build configuration, root-level documentation, CI/CD workflows) are exempt, but ALL feature code MUST reside in appropriate packages. Creating functionality outside this structure violates project constitution and will be rejected in code review.

**ENFORCEMENT**: Code reviews MUST verify that:
- All new functionality is in `packages/` directory
- Frontend and backend are properly separated into `-frt` and `-srv` packages
- Each package has `base/` folder structure
- No feature code exists in repository root or other non-package locations
- Package naming follows documented conventions

**REFERENCE PATTERN**: This architecture mirrors the proven pattern from **Universo Platformo React** repository (https://github.com/teknokomo/universo-platformo-react) which successfully implements 32+ modular packages. The Java implementation MUST maintain this modular structure to enable future package extraction into separate repositories.

**Rationale**: The monorepo structure enables modular development while maintaining unified versioning and build coordination. The `base/` folder requirement ensures extensibility when multiple technology implementations are needed for the same functional domain. Strict modular architecture is essential because individual packages will eventually be extracted into separate repositories as the platform matures.

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

### VII. Feature Pattern Consistency (NON-NEGOTIABLE)

All features MUST follow documented entity relationship patterns:

- Standard three-tier pattern (Primary/Secondary/Tertiary entities like Clusters/Domains/Resources) is the default
- Features MUST identify which pattern they follow in their specification
- Deviations from standard patterns MUST be explicitly justified in feature specifications
- Each tier MUST have complete CRUD operations and corresponding UI views
- Entity relationships MUST be documented with clear diagrams
- Pattern violations discovered during code review MUST be justified or corrected

**Standard Three-Tier Pattern Variants**:

| Feature | Primary | Secondary | Tertiary | Use Case |
|---------|---------|-----------|----------|----------|
| Clusters | Clusters | Domains | Resources | Organization structure |
| Metaverses | Metaverses | Sections | Entities | Virtual world hierarchy |
| Uniks | Uniks | Spaces | Agents | Workflow/project structure |
| Projects | Projects | Versions | Assets | Version control |

**Key Pattern Characteristics**:
- Primary entity: Top-level container with ownership and member management
- Secondary entity: Organizational subdivision within primary
- Tertiary entity: Concrete items/resources within secondary
- Denormalization: Tertiary entities MAY reference primary directly for efficient queries
- Member Management: Primary entities MUST support role-based access (owner/admin/member)

**Rationale**: Consistent entity patterns enable code reuse, reduce cognitive load for developers, and provide predictable user experiences across features. The three-tier hierarchy has proven effective in the React reference implementation and should be preserved in the Java version.

### VIII. React Repository Alignment (NON-NEGOTIABLE)

Development MUST maintain alignment with the React reference implementation:

- React repository MUST be analyzed monthly or after major releases
- New features in React MUST be evaluated for porting to Java version
- Feature porting decisions (Port/Adapt/Skip/Defer) MUST be documented with rationale
- Ported features MUST document differences from React implementation
- Pattern translation guide MUST be maintained showing React→Java equivalents
- Breaking changes in React reference model MUST trigger specification reviews

**Rationale**: The React repository serves as the conceptual reference for Universo Platformo. While implementation details differ, maintaining alignment ensures both versions serve the same user needs and can evolve together. Documentation of adaptation decisions prevents drift and knowledge loss.

### IX. Testing Standards (NON-NEGOTIABLE)

All code MUST meet testing requirements:

- Service layer code MUST have 70%+ unit test coverage
- Each REST endpoint MUST have integration tests
- Each feature MUST have at least one end-to-end UI test for primary workflow
- All tests MUST pass before code can be merged
- Tests MUST run in CI/CD environment without manual setup
- Test doubles (mocks/stubs) MUST be used to isolate external dependencies

**Rationale**: Comprehensive testing ensures reliability, enables confident refactoring, and prevents regressions. The testing pyramid approach (70% unit, 25% integration, 5% UI) provides good coverage while keeping test suites maintainable and fast.

## Architectural Patterns (From React Reference)

The following patterns have been identified in the React reference implementation and should be adapted for Java implementation:

### Critical Patterns to Implement

1. **Row-Level Security (RLS) Pattern**
   - Purpose: Database-enforced multi-tenant data isolation
   - React: JWT context propagation via TypeORM QueryRunner
   - Java: Spring Security filter + JPA transaction callbacks
   - Status: Pattern documented, implementation pending
   - Reference: `.specify/memory/react-to-java-patterns.md` (RLS section)

2. **Universal Pagination Pattern**
   - Purpose: Consistent list views with search, sort, filter
   - React: TanStack Query + Zod schemas + TypeORM
   - Java: Spring Data Specification + Pageable + Vaadin lazy loading
   - Status: Pattern documented, implementation pending
   - Reference: `.specify/memory/react-to-java-patterns.md` (Pagination section)

3. **Three-Tier Entity Hierarchy**
   - Purpose: Consistent entity relationships across features
   - Pattern: Primary → Secondary → Tertiary with member management
   - Examples: Clusters/Domains/Resources, Metaverses/Sections/Entities
   - Status: Pattern documented in Constitution Principle VII
   - Reference: `.specify/memory/react-architecture-analysis.md` (Pattern section)

4. **Member Management Pattern**
   - Purpose: Role-based access control for collaborative features
   - Roles: owner (full control), admin (management), member (view/contribute)
   - React: Junction table with enum roles + Passport.js
   - Java: JPA @IdClass + Spring Security PermissionEvaluator
   - Status: Pattern documented, implementation pending
   - Reference: `.specify/memory/react-to-java-patterns.md` (Member Management section)

5. **Centralized i18n Architecture**
   - Purpose: Single source of truth for translations
   - React: Namespace registry in @universo/i18n package
   - Java: ResourceBundle with MessageSource
   - Status: Pattern documented, partial implementation exists
   - Reference: `.specify/memory/react-to-java-patterns.md` (i18n section)

### Specialized Patterns for Future Implementation

6. **UPDL Node System**
   - Purpose: Universal Platform Description Language for multi-platform export
   - Components: 7 core node types (Space, Entity, Component, Event, Action, Data, Universo)
   - React Status: Production-ready with AR.js and PlayCanvas exporters
   - Java Priority: Phase 2-3 (after core features implemented)
   - Reference: `.specify/memory/react-architecture-analysis.md` (UPDL section)

7. **Template-First Export System**
   - Purpose: Reusable export templates for multiple technologies
   - React: Template engine with technology-specific handlers
   - Java Approach: Thymeleaf or custom template engine + Java 3D libraries
   - Status: Architecture planned for Phase 3
   - Reference: React repository `packages/publish-srv`

8. **Real-Time Collaboration Pattern**
   - Purpose: Multi-user editing and synchronization
   - React: Colyseus framework for multiplayer
   - Java: Spring WebSocket + STOMP protocol
   - Status: Architecture planned for Phase 3
   - Reference: React repository `packages/multiplayer-colyseus-srv`

### Pattern Implementation Priority

**Phase 1 (Current)**: Foundation
- [x] Monorepo structure (COMPLETE)
- [x] Constitution and governance (COMPLETE)
- [x] Pattern documentation (COMPLETE)
- [ ] Three-tier entity pattern (first implementation: Clusters)
- [ ] Member management pattern
- [ ] Universal pagination pattern

**Phase 2**: Core Features
- [ ] RLS integration pattern
- [ ] All primary features (Clusters, Metaverses, Uniks, Projects)
- [ ] Centralized logging and monitoring

**Phase 3**: Advanced Systems
- [ ] UPDL schema validation
- [ ] Export template engine
- [ ] Real-time collaboration infrastructure

## Technology Stack Standards

**Primary Stack**:
- **Language**: Java 17+ (LTS version)
- **Frontend Framework**: Vaadin (current stable version)
- **Backend Framework**: Spring Boot 3.x with Spring Framework 6.x
- **Database**: Supabase (PostgreSQL-based) with abstracted data access
- **Build Tool**: Maven (preferred for monorepo multi-module projects) or Gradle (with multi-project support)
- **Testing**: JUnit 5, Spring Test, Mockito, Vaadin TestBench
- **Code Quality**: JaCoCo (coverage), Checkstyle (style), SpotBugs (bugs)

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

**React Repository Monitoring** (Monthly or after major releases):
1. Review React repository commits, PRs, and issues
2. Identify new features and architectural changes
3. Evaluate features using selection criteria (P1/P2/P3)
4. Document porting decisions in decision log
5. Update pattern translation guide with new patterns discovered

**Issue Creation**:
1. Analyze Universo Platformo React for new functionality to replicate
2. Create GitHub Issue following `.github/instructions/github-issues.md`
3. Apply labels per `.github/instructions/github-labels.md`
4. Issue must include both English and Russian descriptions

**Specification Phase**:
1. Create feature specification using `.specify/templates/spec-template.md`
2. Identify entity relationship pattern (standard or variation)
3. Document React feature relationship (if applicable)
4. Create implementation plan using `.specify/templates/plan-template.md`
5. Generate task breakdown using `.specify/templates/tasks-template.md`
6. Obtain stakeholder approval on specifications

**Implementation Phase**:
1. Create feature branch from main
2. Implement tasks in priority order (P1 → P2 → P3)
3. Write tests achieving required coverage (70%+ unit test coverage)
4. Follow documented entity pattern consistently
5. Update both English and Russian documentation atomically
6. Create Pull Request following `.github/instructions/github-pr.md`

**Review & Merge**:
1. Verify constitution compliance (all nine principles)
2. Verify pattern consistency (matches documented pattern)
3. Verify test coverage requirements (70%+ unit, integration tests, UI tests)
4. Verify bilingual documentation completeness
5. Verify GitHub workflow compliance
6. Obtain code review approval
7. Merge to main branch

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

**Version**: 2.1.0 | **Ratified**: 2025-11-16 | **Last Amended**: 2025-11-17
