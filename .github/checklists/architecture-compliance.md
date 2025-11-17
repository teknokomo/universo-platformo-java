# Architecture Compliance Checklist

**Purpose**: Verify that all code changes comply with Constitution Principle I (Monorepo Package Architecture - NON-NEGOTIABLE)

**Reference**: `.specify/memory/constitution.md` - Principle I  
**Pattern Source**: [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react) (32+ modular packages)

## Critical Requirements (Must Pass)

### 1. Package Location ✅

- [ ] All new functionality is located in `packages/` directory
- [ ] No feature code exists in repository root
- [ ] No feature code exists in `.specify/` directory
- [ ] No feature code exists in `.github/` directory (except workflow infrastructure)
- [ ] Only permitted files in root: `pom.xml`, `README.md`, `README-RU.md`, `.gitignore`, documentation

**Why**: Feature code MUST be modular to enable future extraction into separate repositories.

### 2. Frontend/Backend Separation ✅

- [ ] Frontend code is in packages with `-frt` suffix (e.g., `packages/clusters-frt/`)
- [ ] Backend code is in packages with `-srv` suffix (e.g., `packages/clusters-srv/`)
- [ ] No mixing of frontend and backend code within a single package
- [ ] Each package has clear purpose and responsibility

**Why**: Clear separation enables independent deployment and scaling of frontend/backend.

### 3. Base Folder Structure ✅

- [ ] Each package contains `base/` folder at root level
- [ ] Implementation code is inside `base/` directory
- [ ] Package structure: `packages/{feature}-{frt|srv}/base/src/`
- [ ] POM file exists at both package root and `base/` level

**Why**: `base/` folder supports future multiple implementations of same functionality.

### 4. Package Naming Conventions ✅

- [ ] Package names follow pattern: `{feature}-{frt|srv}`
- [ ] Feature name is lowercase, hyphenated (e.g., `clusters`, `user-profile`)
- [ ] Suffix is either `-frt` (frontend) or `-srv` (backend)
- [ ] Package names are descriptive and self-explanatory
- [ ] No ambiguous or generic names (avoid `common`, `shared`, `utils` as package names)

**Why**: Consistent naming enables easy navigation and understanding of codebase.

### 5. Java Package Naming ✅

- [ ] Java packages follow convention: `pro.universo.{feature}.{layer}`
- [ ] Backend uses `pro.universo.{feature}.api.*` namespace
- [ ] Frontend uses `pro.universo.{feature}.ui.*` namespace
- [ ] Layer structure follows: controller, service, repository, model, dto, etc.
- [ ] No violation of Java package naming conventions

**Why**: Standard Java conventions enable tooling support and developer familiarity.

## Additional Verification

### 6. Maven Module Configuration ✅

- [ ] Root `pom.xml` declares new packages in `<modules>` section
- [ ] Each package has own `pom.xml` with proper parent reference
- [ ] Dependencies properly declared (no circular dependencies)
- [ ] Version numbers managed via parent POM (BOM pattern)
- [ ] Build executes successfully: `mvn clean install`

**Why**: Proper Maven configuration ensures buildability and dependency management.

### 7. Package Self-Containment ✅

- [ ] Package has minimal dependencies on other packages
- [ ] Cross-package coupling is justified and documented
- [ ] Package can be understood independently
- [ ] Package has clear API boundary (public interfaces)
- [ ] Internal implementation details are not exposed

**Why**: Self-contained packages can be extracted to separate repositories more easily.

### 8. Bilingual Documentation ✅

- [ ] Each package has `README.md` (English)
- [ ] Each package has `README-RU.md` (Russian)
- [ ] Both README files have identical structure and line count
- [ ] READMEs document package purpose, dependencies, and usage
- [ ] READMEs updated atomically with code changes

**Why**: Constitution Principle II (Bilingual Documentation - NON-NEGOTIABLE).

## Pattern Compliance

### 9. React Repository Alignment ✅

- [ ] Package structure mirrors React repository patterns where applicable
- [ ] Adaptations from React patterns are documented with rationale
- [ ] Three-tier entity pattern followed if applicable (Primary/Secondary/Tertiary)
- [ ] Deviations from standard patterns are justified in specification

**Why**: Constitution Principle VIII (React Repository Alignment - NON-NEGOTIABLE).

### 10. Future Extraction Readiness ✅

- [ ] Package could theoretically be moved to separate repository
- [ ] All dependencies are explicitly declared
- [ ] No hidden dependencies on repository-specific infrastructure
- [ ] Package version can be managed independently
- [ ] Package has clear release/deployment strategy

**Why**: Packages will eventually be extracted as platform matures.

## Review Decision

**Pass Criteria**: All items in sections 1-5 MUST be checked. Items in sections 6-10 are HIGHLY RECOMMENDED.

**Reviewer Decision**:
- [ ] ✅ APPROVED - Full compliance with modular architecture requirements
- [ ] ⚠️ APPROVED WITH NOTES - Minor issues documented, to be addressed in follow-up
- [ ] ❌ REJECTED - Architecture violations must be fixed before merge

**Reviewer Notes**:
```
[Document any concerns, exceptions, or follow-up items here]
```

## Enforcement

Constitution states:

> **PROHIBITION**: Functionality MUST NOT be implemented outside the `packages/` directory structure. Common infrastructure files (build configuration, root-level documentation, CI/CD workflows) are exempt, but ALL feature code MUST reside in appropriate packages. Creating functionality outside this structure violates project constitution and will be rejected in code review.

**This is NON-NEGOTIABLE**. Reviewers MUST reject PRs that violate modular architecture requirements.

## References

- **Constitution**: `.specify/memory/constitution.md` - Principle I
- **Pattern Translation**: `.specify/memory/react-to-java-patterns.md`
- **Architecture Analysis**: `.specify/memory/react-architecture-analysis.md`
- **React Reference**: https://github.com/teknokomo/universo-platformo-react
- **Audit Document**: `.specify/memory/modular-architecture-audit.md`

## Quick Verification Commands

```bash
# Check package structure
ls -la packages/

# Verify no Java code in root
find . -maxdepth 1 -name "*.java" | wc -l  # Should be 0

# Check Maven modules
grep -A 20 "<modules>" pom.xml

# Verify base folders exist
find packages/ -type d -name "base" | wc -l  # Should equal number of packages

# Build verification
mvn clean install -DskipTests
```

---

**Last Updated**: 2025-11-17  
**Constitution Version**: 2.1.0
