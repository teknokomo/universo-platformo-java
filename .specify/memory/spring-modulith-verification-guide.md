# Spring Modulith Architecture Verification Guide

**Version**: 1.0.0  
**Date**: 2025-11-18  
**Purpose**: Guide for implementing Spring Modulith architecture verification in Universo Platformo Java  
**Sources**: Spring Modulith documentation, Context7 /spring-projects/spring-modulith

## Overview

Spring Modulith provides automated verification of modular architecture, ensuring modules remain properly isolated and dependencies follow documented patterns. This guide explains how to integrate Spring Modulith into the Universo Platformo Java project to enforce architectural constraints automatically.

## Why Spring Modulith?

**Problems It Solves**:
1. **Dependency Violations**: Prevents unintended dependencies between modules
2. **Circular Dependencies**: Detects and prevents circular module dependencies
3. **Internal Package Access**: Ensures modules don't access each other's internal implementation
4. **Architecture Drift**: Catches violations early in development (at build time)
5. **Documentation**: Auto-generates architecture diagrams and documentation

**Benefits for Universo Platformo**:
- Enforces Constitution Principle I (Monorepo Package Architecture)
- Validates modular structure matches documented patterns
- Prevents future refactoring difficulties
- Provides living documentation of module relationships
- Integrates with CI/CD to fail builds on violations

## Integration Steps

### Step 1: Add Spring Modulith Dependency

**Add to Root POM** (`pom.xml`):

```xml
<dependencyManagement>
    <dependencies>
        <!-- Existing BOMs... -->
        
        <!-- Spring Modulith BOM -->
        <dependency>
            <groupId>org.springframework.modulith</groupId>
            <artifactId>spring-modulith-bom</artifactId>
            <version>1.1.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

**Add to Module POM** (`packages/core-srv/base/pom.xml`):

```xml
<dependencies>
    <!-- Existing dependencies... -->
    
    <!-- Spring Modulith Test Support -->
    <dependency>
        <groupId>org.springframework.modulith</groupId>
        <artifactId>spring-modulith-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Step 2: Create Architecture Verification Test

**Create Test Class** (`packages/core-srv/base/src/test/java/pro/universo/platformo/core/ModularityVerificationTest.java`):

```java
package pro.universo.platformo.core;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import java.io.IOException;

/**
 * Architecture verification test using Spring Modulith.
 * 
 * This test ensures the modular structure is maintained according to project
 * constitution and best practices. It verifies:
 * - No circular dependencies between modules
 * - Internal packages are not accessed from other modules
 * - Dependencies follow documented architectural patterns
 * 
 * This test should be run on every build to catch architectural violations early.
 */
class ModularityVerificationTest {

    /**
     * Verify the modular structure of the application.
     * 
     * This test loads the application module structure and verifies it against
     * Spring Modulith's rules:
     * - Modules cannot have circular dependencies
     * - Modules cannot access other modules' internal packages
     * - All dependencies must be explicitly allowed
     * 
     * If this test fails, it indicates an architectural violation that must be fixed.
     */
    @Test
    void verifyModularity() {
        ApplicationModules modules = ApplicationModules.of(CoreServerApplication.class);
        
        // Verify all architectural constraints
        modules.verify();
    }
    
    /**
     * Print the detected module structure.
     * 
     * This test prints information about detected modules, useful for:
     * - Understanding how Spring Modulith interprets the package structure
     * - Verifying module detection is correct
     * - Debugging module organization issues
     */
    @Test
    void printModuleStructure() {
        ApplicationModules modules = ApplicationModules.of(CoreServerApplication.class);
        
        System.out.println("=== Detected Application Modules ===");
        modules.forEach(module -> {
            System.out.println("\nModule: " + module.getName());
            System.out.println("  Display Name: " + module.getDisplayName());
            System.out.println("  Base Package: " + module.getBasePackage());
            System.out.println("  Dependencies: " + module.getDependencies().size());
            
            if (!module.getDependencies().isEmpty()) {
                System.out.println("  Depends on:");
                module.getDependencies().forEach(dep -> 
                    System.out.println("    - " + dep.getName())
                );
            }
        });
    }
    
    /**
     * Generate architecture documentation.
     * 
     * This test generates PlantUML diagrams and Asciidoctor documentation
     * showing the application's module structure and relationships.
     * 
     * Output location: target/modulith-docs/
     * - modules.puml: Overall component diagram
     * - module-*.puml: Individual module diagrams
     * - module-*.adoc: Detailed module documentation
     * 
     * These files can be committed to documentation or generated in CI/CD.
     */
    @Test
    void generateArchitectureDocumentation() throws IOException {
        ApplicationModules modules = ApplicationModules.of(CoreServerApplication.class);
        
        new Documenter(modules)
            .writeModulesAsPlantUml()           // Overall component diagram
            .writeIndividualModulesAsPlantUml() // Per-module diagrams
            .writeModuleCanvases();              // Detailed module documentation
        
        System.out.println("Architecture documentation generated in target/modulith-docs/");
    }
}
```

### Step 3: Define Module Structure

Spring Modulith detects modules from package structure. For Universo Platformo, modules should be organized within each package:

**Example Structure** (`packages/core-srv/base/src/main/java/`):

```
pro.universo.platformo.core/
├── CoreServerApplication.java    # Main application class
├── config/                        # Module: config
│   └── SecurityConfig.java
├── domain/                        # Module: domain
│   ├── Cluster.java
│   └── internal/                  # Internal package (not accessible)
│       └── ClusterValidator.java
├── repository/                    # Module: repository
│   └── ClusterRepository.java
└── service/                       # Module: service
    ├── ClusterService.java
    └── internal/                  # Internal package
        └── ClusterServiceImpl.java
```

**Module Detection Rules**:
1. First-level packages under the application package are detected as modules
2. Packages named `internal` are considered internal to their module
3. Modules can only access other modules' public packages
4. Internal packages are not accessible from other modules

### Step 4: Configure Module Boundaries (Optional)

For explicit module configuration, use `@ApplicationModule` annotation:

```java
package pro.universo.platformo.core.clusters;

import org.springframework.modulith.ApplicationModule;

/**
 * Clusters module handles cluster management functionality.
 * 
 * Allowed dependencies:
 * - core.domain (for shared domain objects)
 * - core.config (for configuration)
 * 
 * This module does NOT allow direct database access - use repositories.
 */
@ApplicationModule(
    displayName = "Cluster Management",
    allowedDependencies = {"domain", "config"}
)
public class ClustersModule {
    // Marker class for module configuration
}
```

## Module Organization Patterns

### Pattern 1: Layer-Based Modules (Horizontal)

**Structure**:
```
pro.universo.platformo.core/
├── api/              # REST API endpoints
├── domain/           # Domain entities and logic
├── repository/       # Data access
└── service/          # Business logic
```

**Dependency Flow**: api → service → repository → domain

**Best For**: Small applications, simple hierarchies

### Pattern 2: Feature-Based Modules (Vertical)

**Structure**:
```
pro.universo.platformo.core/
├── clusters/         # Everything related to clusters
│   ├── api/
│   ├── domain/
│   ├── repository/
│   └── service/
├── domains/          # Everything related to domains
│   ├── api/
│   ├── domain/
│   ├── repository/
│   └── service/
└── shared/           # Shared across modules
    └── dto/
```

**Dependency Flow**: clusters → shared ← domains (no cross-feature dependencies)

**Best For**: Large applications, Domain-Driven Design, Universo Platformo (recommended)

### Pattern 3: Hybrid (Recommended for Universo Platformo)

**Structure**:
```
pro.universo.platformo/
├── core-srv/base/
│   └── pro.universo.platformo.core/
│       ├── shared/         # Shared utilities, DTOs
│       ├── config/         # Configuration
│       └── security/       # Security infrastructure
└── clusters-srv/base/
    └── pro.universo.clusters/
        ├── api/            # Cluster REST API
        ├── domain/         # Cluster entities
        ├── repository/     # Cluster data access
        └── service/        # Cluster business logic
```

**Benefits**:
- Package-level separation matches module-level separation
- Each package (core-srv, clusters-srv) is independently verifiable
- Shared code in core-srv, features in feature packages
- Aligns with Constitution Principle I

## Verification Rules

### Rule 1: No Circular Dependencies

**Example Violation**:
```
clusters → domains → clusters  ❌
```

**How to Fix**:
- Extract shared code to `shared` module
- Refactor dependencies to be unidirectional
- Use events instead of direct dependencies

### Rule 2: No Internal Package Access

**Example Violation**:
```java
// In clusters module
import pro.universo.domains.internal.DomainValidator;  ❌
```

**How to Fix**:
- Only import public packages from other modules
- Move shared functionality to public packages
- Use interfaces in public packages, implementations in internal

### Rule 3: Respect Allowed Dependencies

**Example Configuration**:
```java
@ApplicationModule(
    allowedDependencies = {"domain", "shared"}
)
```

**Example Violation**:
```java
// In module with allowed dependencies ["domain", "shared"]
import pro.universo.repository.ClusterRepository;  ❌
```

**How to Fix**:
- Add dependency to `allowedDependencies` if justified
- Refactor to remove unnecessary dependency
- Use interfaces from allowed modules

## CI/CD Integration

### Maven Configuration

The verification test runs automatically with:

```bash
mvn test
```

### Fail Build on Violations

Spring Modulith verification test throws exception on violations, causing build to fail:

```
[ERROR] Tests run: 1, Failures: 1, Errors: 0, Skipped: 0
[ERROR] Failures: 
[ERROR]   ModularityVerificationTest.verifyModularity
    Architectural violations:
    - Module 'clusters' accesses 'domains.internal.DomainValidator'
    - Circular dependency detected: clusters → domains → clusters
```

### GitHub Actions Integration

```yaml
name: Architecture Verification

on: [push, pull_request]

jobs:
  verify:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '17'
      - name: Verify Architecture
        run: mvn test -Dtest=ModularityVerificationTest
```

## Documentation Generation

### Generate Diagrams

```bash
mvn test -Dtest=ModularityVerificationTest#generateArchitectureDocumentation
```

**Output** (`target/modulith-docs/`):

1. **modules.puml**: Overall component diagram
   ```plantuml
   @startuml
   component [clusters]
   component [domains]
   component [shared]
   
   [clusters] --> [shared]
   [domains] --> [shared]
   @enduml
   ```

2. **module-clusters.puml**: Individual module diagram showing internal structure

3. **module-clusters.adoc**: Detailed documentation in Asciidoctor format

### Include in Project Documentation

**Option 1**: Commit generated files to `/docs` directory
**Option 2**: Generate in CI/CD and publish to GitHub Pages
**Option 3**: Include in project README with PlantUML rendering

## Best Practices

### 1. Run Verification on Every Build

**Why**: Catches violations immediately, prevents architecture drift

**How**: Verification test runs automatically with `mvn test`

### 2. Use Internal Packages for Implementation

**Pattern**:
```
clusters/
├── ClusterService.java          # Public API
└── internal/
    └── ClusterServiceImpl.java  # Internal implementation
```

**Benefits**:
- Clear separation between API and implementation
- Prevents coupling to implementation details
- Enables refactoring without breaking other modules

### 3. Document Module Dependencies

**In Module Documentation**:
```java
/**
 * Clusters Module
 * 
 * Dependencies:
 * - core.domain: For shared domain objects (Cluster, Domain)
 * - core.config: For Spring configuration
 * - core.shared: For common DTOs and utilities
 * 
 * This module does NOT depend on:
 * - domains module (to avoid circular dependency)
 * - repository directly (uses service layer)
 */
@ApplicationModule(
    displayName = "Cluster Management",
    allowedDependencies = {"domain", "config", "shared"}
)
```

### 4. Review Module Structure During Code Reviews

**Checklist**:
- [ ] New dependencies documented and justified
- [ ] No circular dependencies introduced
- [ ] Internal packages used for implementation
- [ ] Public API minimal and well-designed
- [ ] Module boundaries respected

### 5. Generate and Review Architecture Diagrams

**Frequency**: Monthly or after significant changes

**Review Questions**:
- Are dependencies still appropriate?
- Have any unintended dependencies appeared?
- Is module structure still clear and logical?
- Do diagrams match documented architecture?

## Troubleshooting

### Problem: Modules Not Detected

**Symptom**: `ApplicationModules.of()` finds no modules

**Causes**:
1. Package structure too flat (no sub-packages)
2. Application class in wrong package
3. No Spring components in packages

**Solution**:
```java
// Ensure application class is at root of module packages
package pro.universo.platformo.core;  // Root package

@SpringBootApplication
public class CoreServerApplication { ... }

// Sub-packages become modules
package pro.universo.platformo.core.clusters;  // Module: clusters
package pro.universo.platformo.core.domains;   // Module: domains
```

### Problem: False Positive Violations

**Symptom**: Test fails but dependency seems correct

**Causes**:
1. Missing `@ApplicationModule` with `allowedDependencies`
2. Accessing internal package unintentionally
3. Transitive dependency violation

**Solution**:
1. Add explicit allowed dependencies
2. Check imports for `internal` packages
3. Review dependency chain

### Problem: Verification Too Strict

**Symptom**: Many violations for shared utilities

**Solution**: Create `shared` module for common code:
```
pro.universo.platformo.core/
└── shared/
    ├── dto/
    ├── exception/
    └── util/
```

All modules can depend on `shared` without causing circular dependencies.

## Integration with Constitution

Spring Modulith verification **enforces** Constitution Principle I:

**Principle I Requirements**:
- ✅ All functionality in `packages/` directory
- ✅ Frontend/Backend separation
- ✅ Base folder structure
- ✅ Clear module boundaries
- ✅ No circular dependencies

**Enforcement Mechanism**:
- Manual: Code review checklist
- Automated: Spring Modulith verification test
- Build-time: Test fails on violations
- CI/CD: Prevents merging violations

## Next Steps

1. **Add Dependency**: Add Spring Modulith to root POM
2. **Create Test**: Implement ModularityVerificationTest
3. **Run Verification**: Execute test to baseline current state
4. **Fix Violations**: Address any detected violations
5. **Enable CI/CD**: Add to build pipeline
6. **Generate Docs**: Create architecture diagrams
7. **Document**: Update project README with architecture info
8. **Train Team**: Ensure developers understand module boundaries

## References

- **Spring Modulith Documentation**: https://docs.spring.io/spring-modulith/
- **Context7 Patterns**: /spring-projects/spring-modulith
- **Best Practices Doc**: `.specify/memory/java-vaadin-spring-best-practices.md` Section 1.3
- **Constitution**: `.specify/memory/constitution.md` Principle I
- **Validation Report**: `.specify/memory/best-practices-validation-2025-11-18.md` Section VII.1.A

---

**Document Status**: Active  
**Review Frequency**: Quarterly or after significant architecture changes  
**Next Review**: 2026-02-18  
**Version**: 1.0.0
