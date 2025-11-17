# [Package Name] [Frontend/Backend]

✨ **Modern Package** - Part of the Universo Platformo Java architecture

## Overview

[Brief description of the package purpose and functionality. Describe the three-tier architecture if applicable: Primary → Secondary → Tertiary entities]

## Package Information

- **Package**: `pro.universo.[feature]-[frt/srv]`
- **Version**: `0.1.0`
- **Type**: [Vaadin Frontend / Spring Backend] Package
- **Framework**: [Vaadin 24.x / Spring Boot 3.x]
- **Build System**: Maven
- **Testing**: JUnit 5, [Vaadin TestBench / Spring Test]

## Key Features

### 🌍 [Feature Name] Management
- **Hierarchical Organization**: Three-tier architecture ([Primary] → [Secondary] → [Tertiary])
- **Complete Data Isolation**: [Entities] from different [primary entities] are completely separated
- **Role-Based Access**: User roles and permissions for [feature] access control
- **Context-Aware Navigation**: [Feature]-aware routing with breadcrumbs

### 🎨 User Interface (Frontend packages only)
- **Vaadin Integration**: Consistent UI components with Lumo theme
- **Responsive Design**: Optimized for desktop and mobile experiences
- **Grid Views**: Flexible data presentation with pagination and search
- **Dialog Forms**: Modal forms for creating and editing entities

### 🔧 Technical Features
- **Type-Safe**: Full Java type safety with generics
- **Data Access**: JPA repositories with Spring Data
- **Internationalization**: English and Russian translations with ResourceBundle
- **Form Validation**: Bean Validation (JSR-380) with custom validators
- **API Integration**: RESTful API with Spring Web

## Installation & Setup

### Prerequisites
```bash
# System requirements
Java 17 or later (LTS)
Maven 3.9.x or later
PostgreSQL database (or Supabase account)
```

### Installation
```bash
# Install dependencies
mvn clean install

# Build the package
mvn --projects packages/[feature]-[frt/srv]/base clean package

# Run in development mode
mvn --projects packages/[feature]-[frt/srv]/base spring-boot:run
```

### Integration
```java
// Import in your Spring Boot application (Backend)
import pro.universo.[feature].api.controller.[Feature]Controller;
import pro.universo.[feature].api.service.[Feature]Service;

// Import in your Vaadin application (Frontend)
import pro.universo.[feature].ui.views.[Feature]ListView;
```

## Architecture

### Three-Tier Entity Model
- **[Primary]**: Top-level organizational units providing complete data isolation
- **[Secondary]**: Logical groupings within [primary] (e.g., "Categories", "Groups")
- **[Tertiary]**: Individual items belonging to specific [secondary] within [primary]

### Data Isolation Strategy
- Complete separation between [primary entities] - no cross-[primary] visibility
- All operations maintain [primary] context through URL routing
- Frontend and backend validation preventing orphaned entities
- Role-based access control for [primary] permissions

## Usage

### Basic Components (Frontend)
```java
@Route(value = "[feature]", layout = MainLayout.class)
@PageTitle("[Feature]")
@PermitAll
public class [Feature]ListView extends VerticalLayout {
    
    private final [Feature]Service service;
    private final Grid<[Feature]> grid = new Grid<>([Feature].class);
    
    public [Feature]ListView([Feature]Service service) {
        this.service = service;
        configureGrid();
        add(createToolbar(), grid);
    }
}
```

### API Integration (Backend)
```java
@RestController
@RequestMapping("/api/[feature]")
public class [Feature]Controller {
    
    private final [Feature]Service service;
    
    @GetMapping
    public ResponseEntity<List<[Feature]Response>> list() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @PostMapping
    public ResponseEntity<[Feature]Response> create(
            @Valid @RequestBody Create[Feature]Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.create(request));
    }
}
```

## File Structure

```
packages/[feature]-[frt/srv]/base/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── pro/universo/[feature]/[api/ui]/
│   │   │       ├── controller/    # REST endpoints (backend)
│   │   │       ├── service/       # Business logic
│   │   │       ├── repository/    # Data access (backend)
│   │   │       ├── model/         # JPA entities (backend)
│   │   │       ├── dto/           # Data transfer objects
│   │   │       ├── views/         # Vaadin views (frontend)
│   │   │       └── components/    # Reusable UI components (frontend)
│   │   └── resources/
│   │       ├── messages_en.properties  # English i18n
│   │       ├── messages_ru.properties  # Russian i18n
│   │       ├── application.yml         # Configuration
│   │       └── db/migration/           # Database migrations
│   └── test/
│       └── java/
│           └── pro/universo/[feature]/[api/ui]/
│               ├── controller/    # Controller tests
│               ├── service/       # Service unit tests
│               └── repository/    # Repository tests
├── pom.xml
├── README.md             # This file
└── README-RU.md          # Russian version
```

## Development

### Prerequisites
- Java 17+
- Maven 3.9+
- PostgreSQL 15+

### Available Scripts
```bash
# Development
mvn compile              # Compile Java sources
mvn spring-boot:run      # Run with hot reload

# Testing
mvn test                 # Run test suite
mvn test -Dtest=[TestClass]  # Run specific test
mvn verify               # Run tests + integration tests

# Code Quality
mvn checkstyle:check     # Check code style
mvn spotbugs:check       # Find bugs
mvn jacoco:report        # Generate coverage report
```

### Development Guidelines

#### Architecture Patterns
- **Three-tier Model**: [Primary] → [Secondary] → [Tertiary]
- **Data Isolation**: Strict context boundaries between [primary entities]
- **Spring Data JPA**: Centralized data access and transactions
- **Vaadin Components**: Consistent component library usage

#### Validation
```java
@Entity
public class [Feature] {
    @NotBlank(message = "{[feature].name.required}")
    @Size(max = 255, message = "{[feature].name.maxlength}")
    private String name;
}
```

## Testing

### Test Structure
```
src/test/java/
├── controller/
│   └── [Feature]ControllerTest.java
├── service/
│   └── [Feature]ServiceTest.java
└── repository/
    └── [Feature]RepositoryTest.java
```

### Testing Approach
```java
@SpringBootTest
class [Feature]ServiceTest {
    
    @Autowired
    private [Feature]Service service;
    
    @Test
    void shouldCreate[Feature]() {
        Create[Feature]Request request = new Create[Feature]Request();
        request.setName("Test");
        
        [Feature]Response response = service.create(request);
        
        assertNotNull(response.getId());
        assertEquals("Test", response.getName());
    }
}
```

### Running Tests
```bash
mvn test                    # Run all tests
mvn test -Dtest=[Test]*     # Run tests matching pattern
mvn verify                  # Run integration tests
mvn jacoco:report           # Generate coverage report
```

## Configuration

### Environment Variables
```bash
# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/universo
DATABASE_USERNAME=your-username
DATABASE_PASSWORD=your-password

# Authentication
JWT_SECRET=your-jwt-secret

# Application Configuration
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
```

## Contributing

### Code Style
- Follow Google Java Style Guide
- Use meaningful variable names
- Add Javadoc for public methods
- Keep methods small and focused

### Pull Request Process
1. Create feature branch from `main`
2. Implement changes with tests
3. Update documentation (English and Russian)
4. Run full test suite
5. Submit PR with description

### Commit Convention
Follow conventional commits:
```bash
feat([feature]): add search functionality
fix([feature]): handle null values
docs([feature]): update installation guide
test([feature]): add integration tests
```

## Related Packages
- [`pro.universo.[feature]-srv`](../[feature]-srv/base/README.md) - Backend service
- [`pro.universo.[feature]-frt`](../[feature]-frt/base/README.md) - Frontend UI
- [`pro.universo.core-srv`](../core-srv/base/README.md) - Core backend services
- [`pro.universo.core-frt`](../core-frt/base/README.md) - Core frontend UI

---
*Part of [Universo Platformo Java](../../../README.md) - A comprehensive platform implementation*
