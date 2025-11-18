# Java/Vaadin/Spring Best Practices for Universo Platformo

**Version**: 1.1.0  
**Date**: 2025-11-18  
**Research Sources**: Vaadin Official Docs, Spring Boot Documentation, Spring Modulith, Context7, Web Research  
**Change Log**: v1.1.0 - Added Spring Modulith verification section, build profiles documentation

## Overview

This document consolidates best practices for building enterprise-grade applications with Java, Vaadin Flow, and Spring Boot, specifically for the Universo Platformo Java project. These practices are derived from official documentation, community expertise, and industry standards.

## 1. Maven Multi-Module Monorepo Architecture

### 1.1 Project Structure Best Practices

**Recommended Directory Layout**:
```
universo-platformo-java/
├── pom.xml (parent/aggregator)
├── packages/
│   ├── core-srv/
│   │   └── base/
│   │       ├── pom.xml
│   │       └── src/main/java/pro/universo/core/
│   ├── core-frt/
│   │   └── base/
│   │       ├── pom.xml
│   │       └── src/main/java/pro/universo/ui/
│   ├── clusters-srv/
│   │   └── base/
│   │       ├── pom.xml
│   │       └── src/main/java/pro/universo/clusters/
│   └── clusters-frt/
│       └── base/
│           ├── pom.xml
│           └── src/main/java/pro/universo/clusters/ui/
└── specs/
```

**Key Principles**:

1. **Parent POM Configuration**:
   - Set `<packaging>pom</packaging>` for aggregator POM
   - Use `<modules>` section to list all sub-modules
   - Define common properties (Java version, encoding, plugin versions)
   - Use `<dependencyManagement>` for centralized version control

2. **BOM (Bill of Materials) Pattern**:
   ```xml
   <dependencyManagement>
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-dependencies</artifactId>
               <version>3.2.0</version>
               <type>pom</type>
               <scope>import</scope>
           </dependency>
           <dependency>
               <groupId>com.vaadin</groupId>
               <artifactId>vaadin-bom</artifactId>
               <version>24.3.0</version>
               <type>pom</type>
               <scope>import</scope>
           </dependency>
       </dependencies>
   </dependencyManagement>
   ```

3. **Inter-Module Dependencies**:
   - Use `${project.groupId}` and `${project.version}` for inter-module references
   - Declare dependencies without versions (inherit from `dependencyManagement`)
   - Example:
   ```xml
   <dependency>
       <groupId>${project.groupId}</groupId>
       <artifactId>core-srv</artifactId>
       <version>${project.version}</version>
   </dependency>
   ```

4. **Build Profiles**:
   - **Development profile**: Fast builds, Vaadin dev mode, verbose logging
   - **Production profile**: Optimized Vaadin bundle, minification, production logging
   ```xml
   <profiles>
       <profile>
           <id>production</id>
           <build>
               <plugins>
                   <plugin>
                       <groupId>com.vaadin</groupId>
                       <artifactId>vaadin-maven-plugin</artifactId>
                       <executions>
                           <execution>
                               <goals>
                                   <goal>build-frontend</goal>
                               </goals>
                               <phase>compile</phase>
                           </execution>
                       </executions>
                   </plugin>
               </plugins>
           </build>
       </profile>
   </profiles>
   ```

### 1.2 Dependency Management Best Practices

**Critical Rules**:

1. **Avoid Scope Import Misuse**: Only use `scope=import` for BOM definitions, never for regular libraries
2. **No Circular Dependencies**: Enforce clean dependency graph (API → Core → Implementation → UI)
3. **Minimize Transitive Dependencies**: Mark optional dependencies as `<optional>true</optional>`
4. **Use Maven Versions Plugin**: For centralized version updates (`mvn versions:set`)
5. **Empty relativePath**: In sub-modules, use `<relativePath/>` to resolve parent from repository

**Example Module Dependency Pattern**:
```
clusters-frt/base → clusters-srv/base → core-srv/base
                                      ↓
                                   Entities
```

## 2. Vaadin Application Architecture

### 2.1 Application Structure

**Single Application Entry Point Pattern**:
```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

**Benefits**:
- One `@SpringBootApplication` serves both Vaadin UI and REST API
- Vaadin views as Spring beans with dependency injection
- REST controllers coexist with Vaadin routes on same server
- Simplified deployment and configuration

### 2.2 Service Layer Pattern

**Best Practice Service Implementation**:
```java
@Service
@PreAuthorize("isAuthenticated()")
public class ClusterService {

    private final Validator validator;
    private final ClusterRepository clusterRepository;
    private final ApplicationEventPublisher eventPublisher;

    // Constructor injection (preferred over field injection)
    ClusterService(Validator validator, 
                   ClusterRepository clusterRepository, 
                   ApplicationEventPublisher eventPublisher) {
        this.validator = validator;
        this.clusterRepository = clusterRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClusterId createCluster(ClusterForm clusterForm) {
        var validationErrors = validator.validate(clusterForm);
        if (!validationErrors.isEmpty()) {
            throw new ConstraintViolationException(validationErrors);
        }
        var cluster = clusterRepository.saveAndFlush(createFromForm(clusterForm));
        eventPublisher.publishEvent(new ClusterCreatedEvent(cluster));
        return cluster.clusterId();
    }
}
```

**Key Points**:
- **Stateless services**: No instance variables for request data
- **Constructor injection**: Preferred for required dependencies
- **Transaction boundaries**: Use `@Transactional` on service methods, not repositories
- **Validation**: Validate at service layer before persistence
- **Event publishing**: Notify other components via Spring events
- **Security annotations**: Apply method-level security with `@PreAuthorize`

### 2.3 View Component Pattern

**Vaadin View Best Practices**:
```java
@Route(value = "clusters", layout = MainLayout.class)
@PageTitle("Clusters")
@PermitAll
public class ClusterListView extends VerticalLayout {

    private final ClusterService clusterService;
    private final Grid<ClusterDto> grid;
    private final TextField searchField;

    public ClusterListView(ClusterService clusterService) {
        this.clusterService = clusterService;
        
        // Initialize components
        this.searchField = new TextField();
        this.grid = new Grid<>(ClusterDto.class, false);
        
        // Configure layout
        configureSearch();
        configureGrid();
        
        // Add components
        add(searchField, grid);
        
        // Load data
        updateList();
    }

    private void configureGrid() {
        grid.addColumn(ClusterDto::getName).setHeader("Name");
        grid.addColumn(ClusterDto::getDescription).setHeader("Description");
        grid.addColumn(ClusterDto::getCreatedAt).setHeader("Created");
        
        // Enable lazy loading for large datasets
        grid.setItems(query -> clusterService.findAll(
            PageRequest.of(query.getPage(), query.getPageSize())
        ).stream());
    }

    private void updateList() {
        // Refresh logic
    }
}
```

**Key Principles**:
- **Constructor injection**: Inject services via constructor
- **Component initialization**: Initialize in constructor, not as fields
- **Separation of concerns**: Separate configuration methods
- **Lazy loading**: Use pagination for large datasets
- **Responsive design**: Use Vaadin responsive components

### 2.4 Multi-Module Separation

**Frontend Module (`clusters-frt/base`)**:
- Vaadin views and components
- UI layouts and themes
- Client-side resources
- Depends on backend module (compile-time dependency acceptable for Vaadin)

**Backend Module (`clusters-srv/base`)**:
- Service layer (business logic)
- Repository interfaces
- Entity classes
- DTOs for data transfer
- Domain logic and validation

**Benefits**:
- Clear architectural boundaries
- Independent testing of layers
- Reusable services (can be called from REST endpoints or Vaadin views)
- Future-proof for API-only deployment

## 3. Spring Boot Integration

### 3.1 Configuration Best Practices

**Application Properties Structure**:
```yaml
# application.yml
spring:
  application:
    name: universo-platformo-java
  
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2
      connection-timeout: 30000
  
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    show-sql: false

  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${SUPABASE_URL}/auth/v1

vaadin:
  productionMode: false

logging:
  level:
    pro.universo: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

**Environment-Specific Configuration**:
- `application.yml`: Base configuration
- `application-dev.yml`: Development overrides
- `application-prod.yml`: Production overrides
- Use Spring profiles: `--spring.profiles.active=prod`

### 3.2 Security Configuration

**Spring Security with Supabase JWT**:
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${supabase.jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder())
                )
            );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(
            jwtSecret.getBytes(), 
            "HmacSHA256"
        );
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}
```

**Best Practices**:
- Use `@EnableMethodSecurity` for method-level security
- Store JWT secrets in environment variables, never in code
- Validate JWT signatures using Supabase public key or shared secret
- Use role-based access control (RBAC) with Spring Security
- Apply `@PreAuthorize`, `@PermitAll`, `@DenyAll` on services and views

## 4. Database Access Patterns

### 4.1 Spring Data JPA Repository Pattern

**Repository Interface**:
```java
@Repository
public interface ClusterRepository extends JpaRepository<Cluster, UUID> {
    
    // Method name-based queries (database-agnostic)
    List<Cluster> findByNameContainingIgnoreCase(String name);
    
    List<Cluster> findByOwnerIdAndStatusIn(UUID ownerId, List<ClusterStatus> statuses);
    
    // Custom JPQL query (database-agnostic)
    @Query("SELECT c FROM Cluster c WHERE c.ownerId = :userId " +
           "OR EXISTS (SELECT m FROM ClusterMember m WHERE m.clusterId = c.id " +
           "AND m.userId = :userId)")
    List<Cluster> findAccessibleByUser(@Param("userId") UUID userId);
    
    // Pagination support
    Page<Cluster> findByOwnerIdOrderByCreatedAtDesc(
        UUID ownerId, 
        Pageable pageable
    );
}
```

**Best Practices**:
- Extend `JpaRepository<Entity, ID>` for basic CRUD operations
- Use method name queries for simple conditions
- Use `@Query` with JPQL for complex queries (database-agnostic)
- Reserve native queries for PostgreSQL-specific features (mark as database-specific)
- Always use `Pageable` for list endpoints to support pagination
- Use `@Param` for query parameters for clarity

### 4.2 Entity Design

**Entity Class Pattern**:
```java
@Entity
@Table(name = "clusters")
public class Cluster {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClusterStatus status;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Domain> domains = new ArrayList<>();
    
    // Constructors, getters, setters
    // equals(), hashCode() based on business key (id)
}
```

**Best Practices**:
- Use UUID for primary keys (better for distributed systems)
- Use `@Column` to specify constraints and column names
- Use `@Enumerated(EnumType.STRING)` for enums (readable in database)
- Use `@CreatedDate` and `@LastModifiedDate` for audit fields
- Use `@OneToMany`, `@ManyToOne` with proper cascade settings
- Implement `equals()` and `hashCode()` based on business key
- Keep entities focused on persistence, business logic in services

### 4.3 Row-Level Security (RLS) Implementation

**PostgreSQL RLS Setup**:
```sql
-- Enable RLS on table
ALTER TABLE clusters ENABLE ROW LEVEL SECURITY;

-- Policy: Users can see clusters they own or are members of
CREATE POLICY cluster_access_policy
ON clusters
FOR ALL
USING (
    owner_id = current_setting('app.user_id')::uuid
    OR EXISTS (
        SELECT 1 FROM cluster_members
        WHERE cluster_id = clusters.id
        AND user_id = current_setting('app.user_id')::uuid
    )
);
```

**Spring Integration with RLS**:
```java
@Component
public class TenantContext {
    private static final ThreadLocal<UUID> currentUserId = new ThreadLocal<>();
    
    public static void setUserId(UUID userId) {
        currentUserId.set(userId);
    }
    
    public static UUID getUserId() {
        return currentUserId.get();
    }
    
    public static void clear() {
        currentUserId.remove();
    }
}

@Component
public class RlsFilter implements Filter {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        try {
            UUID userId = extractUserIdFromJwt(request);
            TenantContext.setUserId(userId);
            
            // Set PostgreSQL session variable for RLS
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute(String.format(
                    "SET LOCAL app.user_id = '%s'", userId
                ));
            }
            
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
```

**RLS Best Practices**:
1. **Shared Schema Pattern**: Use `tenant_id` or `user_id` column with RLS policies
2. **Index RLS Columns**: Always index columns used in RLS conditions (B-tree for scalars, GIN for arrays)
3. **Test Performance**: Use `EXPLAIN ANALYZE` to verify index usage
4. **Set Session Variables**: Pass user context via PostgreSQL session variables
5. **Defense in Depth**: RLS as database-level security, Spring Security as application-level
6. **Supabase Integration**: Use `auth.uid()` in policies when using Supabase Auth
7. **Admin Bypass**: Create separate policies for admin roles

**Multi-Tenant Pattern Summary**:

| Approach | Implementation | Use Case |
|----------|----------------|----------|
| Database-per-tenant | Separate databases | Complete isolation, regulatory compliance |
| Schema-per-tenant | Separate schemas in one DB | Moderate isolation, easier management |
| Shared Schema + RLS | One schema with tenant_id + RLS | Most scalable, cost-effective (recommended) |

## 5. Testing Strategy

### 5.1 Testing Pyramid

**Test Distribution**:
- **70% Unit Tests**: Service layer, business logic
- **25% Integration Tests**: Repository, REST endpoints, database
- **5% UI Tests**: Critical user workflows

### 5.2 Unit Testing with JUnit 5

**Service Layer Test Example**:
```java
@ExtendWith(MockitoExtension.class)
class ClusterServiceTest {
    
    @Mock
    private ClusterRepository clusterRepository;
    
    @Mock
    private Validator validator;
    
    @Mock
    private ApplicationEventPublisher eventPublisher;
    
    @InjectMocks
    private ClusterService clusterService;
    
    @Test
    void createCluster_validInput_createsCluster() {
        // Arrange
        ClusterForm form = new ClusterForm("Test Cluster", "Description");
        Cluster savedCluster = new Cluster();
        savedCluster.setId(UUID.randomUUID());
        savedCluster.setName("Test Cluster");
        
        when(validator.validate(form)).thenReturn(Collections.emptySet());
        when(clusterRepository.saveAndFlush(any())).thenReturn(savedCluster);
        
        // Act
        ClusterId result = clusterService.createCluster(form);
        
        // Assert
        assertNotNull(result);
        verify(clusterRepository).saveAndFlush(any(Cluster.class));
        verify(eventPublisher).publishEvent(any(ClusterCreatedEvent.class));
    }
    
    @Test
    void createCluster_invalidInput_throwsException() {
        // Arrange
        ClusterForm form = new ClusterForm("", "Description");
        ConstraintViolation<ClusterForm> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<ClusterForm>> violations = Set.of(violation);
        
        when(validator.validate(form)).thenReturn(violations);
        
        // Act & Assert
        assertThrows(ConstraintViolationException.class, 
            () -> clusterService.createCluster(form));
    }
}
```

**Unit Test Best Practices**:
- Use `@ExtendWith(MockitoExtension.class)` for Mockito integration
- Mock external dependencies (`@Mock`)
- Use `@InjectMocks` for the class under test
- Follow Arrange-Act-Assert (AAA) pattern
- One assertion per test (or related assertions)
- Descriptive test names: `methodName_scenario_expectedResult`
- Test both success and failure paths

### 5.3 Integration Testing

**Repository Integration Test**:
```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClusterRepositoryTest {
    
    @Autowired
    private ClusterRepository clusterRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void findByNameContainingIgnoreCase_existingName_returnsCluster() {
        // Arrange
        Cluster cluster = new Cluster();
        cluster.setName("Test Cluster");
        cluster.setOwnerId(UUID.randomUUID());
        cluster.setStatus(ClusterStatus.ACTIVE);
        entityManager.persist(cluster);
        entityManager.flush();
        
        // Act
        List<Cluster> results = clusterRepository
            .findByNameContainingIgnoreCase("test");
        
        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Test Cluster");
    }
}
```

**Integration Test Best Practices**:
- Use `@DataJpaTest` for repository tests (lightweight, in-memory DB by default)
- Use `@SpringBootTest` for full application context tests
- Use Testcontainers for real PostgreSQL testing
- Use `@AutoConfigureTestDatabase(replace=NONE)` for real database
- Clean data between tests with `@Transactional` or manual cleanup
- Test database constraints and relationships

### 5.4 Vaadin UI Testing

**TestBench Example**:
```java
@SpringBootTest
class ClusterListViewTest extends SpringUIUnitTest {
    
    @Test
    void createCluster_validInput_showsNotification() {
        // Navigate to cluster list view
        final ClusterListView view = navigate(ClusterListView.class);
        
        // Fill form
        test(view.nameField).setValue("New Cluster");
        test(view.descriptionField).setValue("Test Description");
        
        // Click create button
        test(view.createButton).click();
        
        // Verify notification
        Notification notification = $(Notification.class).first();
        assertEquals("Cluster created successfully", 
            test(notification).getText());
    }
}
```

**UI Test Best Practices**:
- Use Page Object Pattern for complex views
- Test critical user workflows only (5% of tests)
- Use Vaadin TestBench for component testing
- Run UI tests in CI with headless browsers
- Keep UI tests independent (no shared state)
- Use descriptive selectors and IDs for components

## 6. Internationalization (i18n)

### 6.1 ResourceBundle Pattern

**Properties Files**:
```properties
# messages_en.properties
clusters.list.title=Clusters
clusters.list.create=Create Cluster
clusters.list.search=Search clusters...

# messages_ru.properties
clusters.list.title=Кластеры
clusters.list.create=Создать кластер
clusters.list.search=Поиск кластеров...
```

**Vaadin I18NProvider Implementation**:
```java
@Component
public class TranslationProvider implements I18NProvider {
    
    private static final List<Locale> LOCALES = List.of(
        Locale.ENGLISH,
        new Locale("ru")
    );
    
    @Override
    public List<Locale> getProvidedLocales() {
        return LOCALES;
    }
    
    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        try {
            return MessageFormat.format(bundle.getString(key), params);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }
}
```

**Usage in Views**:
```java
@Route("clusters")
public class ClusterListView extends VerticalLayout {
    
    public ClusterListView() {
        H1 title = new H1(getTranslation("clusters.list.title"));
        Button createButton = new Button(getTranslation("clusters.list.create"));
        add(title, createButton);
    }
}
```

**i18n Best Practices**:
- Use namespace keys: `feature.view.element`
- Provide translations for all supported locales
- Use `MessageFormat` for parameterized messages
- Fallback to key display when translation missing (with markers like `!key!`)
- Keep translations in sync with Constitution Principle II (bilingual docs)

## 7. Performance Optimization

### 7.1 Database Performance

**Best Practices**:
1. **Indexing Strategy**:
   - Index foreign keys
   - Index columns used in WHERE, ORDER BY, JOIN
   - Index RLS policy columns (critical!)
   - Use GIN indexes for array columns
   - Monitor slow queries with `pg_stat_statements`

2. **Query Optimization**:
   - Use `@Query` with JOIN FETCH for eager loading
   - Use pagination for large result sets
   - Avoid N+1 queries (use JOIN FETCH or EntityGraph)
   - Use projections for large entities when only few fields needed
   ```java
   @Query("SELECT new pro.universo.dto.ClusterDto(c.id, c.name) FROM Cluster c")
   List<ClusterDto> findAllSummaries();
   ```

3. **Connection Pooling**:
   - Use HikariCP (Spring Boot default)
   - Configure pool size based on load
   ```yaml
   spring.datasource.hikari:
     maximum-pool-size: 20
     minimum-idle: 5
     connection-timeout: 30000
     idle-timeout: 600000
     max-lifetime: 1800000
   ```

### 7.2 Vaadin Performance

**Best Practices**:
1. **Lazy Loading**: Use `Grid.setItems(CallbackDataProvider)` for large datasets
2. **Virtual Scrolling**: Enable for long lists
3. **Production Mode**: Always enable for production builds
4. **Frontend Caching**: Use Vaadin's built-in caching
5. **Minimize Server Round-Trips**: Batch updates when possible

## 8. Supabase Integration

### 8.1 Authentication Flow

**Complete Flow**:
1. User authenticates via Supabase Auth (email/password, OAuth, magic link)
2. Supabase returns JWT access token and refresh token
3. Frontend sends JWT in `Authorization: Bearer <token>` header
4. Spring Security validates JWT signature using Supabase public key
5. Extract user ID from JWT claims
6. Set PostgreSQL session variable for RLS
7. All queries automatically filtered by RLS policies

### 8.2 Database Connection

**Configuration**:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://db.xxxxxx.supabase.co:5432/postgres
    username: postgres
    password: ${SUPABASE_DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
```

**Best Practices**:
- Connect directly to PostgreSQL (not REST API) for Spring Data JPA
- Use Supabase REST API for authentication operations only
- Store credentials in environment variables
- Use connection pooling (HikariCP)
- Enable SSL for production: `?ssl=true&sslmode=require`

## 9. Code Quality and Standards

### 9.1 Code Style

**Guidelines**:
- Follow Google Java Style Guide or similar standard
- Use meaningful variable and method names
- Keep methods short (< 50 lines ideally)
- Prefer composition over inheritance
- Use final for immutable variables
- Document public APIs with Javadoc

### 9.2 Static Analysis

**Recommended Tools**:
1. **Checkstyle**: Code style enforcement
2. **SpotBugs**: Bug detection
3. **JaCoCo**: Code coverage (target: 70%+ for service layer)
4. **SonarQube**: Comprehensive quality analysis

**Maven Configuration**:
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## 10. DevOps and Deployment

### 10.1 Build Process

**Production Build**:
```bash
mvn clean package -Pproduction
```

**Build Profile Configuration**:
- Development: Fast builds, Vaadin dev mode, source maps
- Production: Minification, transpilation, optimized bundle

### 10.2 Environment Variables

**Required Variables**:
```bash
DATABASE_URL=jdbc:postgresql://host:5432/database
DATABASE_USERNAME=username
DATABASE_PASSWORD=password
JWT_SECRET=your-supabase-jwt-secret
SUPABASE_URL=https://xxxxx.supabase.co
VAADIN_PRODUCTION_MODE=true
```

**Best Practices**:
- Never commit secrets to version control
- Use `.env` files locally (add to `.gitignore`)
- Use secrets management in production (AWS Secrets Manager, Azure Key Vault)
- Validate required variables at startup

## 11. Migration from React Reference

### 11.1 Pattern Translation Guide

| React Pattern | Java/Vaadin Equivalent | Notes |
|--------------|------------------------|-------|
| `useState()` hook | Component field + `getUI().access()` | Vaadin components are stateful |
| `useEffect()` hook | `addAttachListener()` | Server-side lifecycle |
| `<Button onClick={...}>` | `button.addClickListener(e -> {...})` | Lambda expressions |
| `<TextField value={x} onChange={...}>` | `binder.bind(field, Getter, Setter)` | Vaadin Binder for two-way binding |
| React Router `<Route>` | `@Route` annotation | Type-safe navigation |
| Context API | Spring dependency injection | Services injected into views |
| `fetch()` API calls | Direct service method calls | No HTTP needed (server-side) |
| Redux/Zustand state | Spring `@Scope("session")` beans | Session-scoped state |
| React Query cache | Spring Cache abstraction | Server-side caching |
| Material-UI components | Vaadin Lumo components | Similar APIs |

### 11.2 Anti-Patterns to Avoid

**Do NOT Port These from React**:
1. Client-side state management (unnecessary in Vaadin)
2. REST API calls from UI (call services directly)
3. Complex async patterns (Vaadin is synchronous server-side)
4. Client-side routing (use Vaadin `@Route`)
5. Separate frontend build process (Vaadin handles this)

## 12. Security Best Practices

### 12.1 Authentication & Authorization

**Checklist**:
- [ ] Use Spring Security for authentication
- [ ] Validate JWT signatures
- [ ] Store JWT secrets securely (environment variables)
- [ ] Apply method-level security (`@PreAuthorize`)
- [ ] Use role-based access control (RBAC)
- [ ] Implement refresh token rotation
- [ ] Set appropriate JWT expiry times (15-30 minutes)
- [ ] Use HTTPS in production

### 12.2 Database Security

**Checklist**:
- [ ] Enable Row-Level Security (RLS) on all tables
- [ ] Define restrictive RLS policies
- [ ] Index RLS policy columns
- [ ] Use prepared statements (JPA does this automatically)
- [ ] Validate input at service layer
- [ ] Sanitize user input
- [ ] Use database connection encryption (SSL)
- [ ] Limit database user permissions

### 12.3 Application Security

**Checklist**:
- [ ] Enable CSRF protection for state-changing operations
- [ ] Validate all user input
- [ ] Use parameterized queries (never string concatenation)
- [ ] Implement rate limiting
- [ ] Log security events
- [ ] Keep dependencies updated
- [ ] Regular security audits
- [ ] Follow OWASP Top 10 guidelines

## References

### Official Documentation
- [Vaadin Documentation](https://vaadin.com/docs/)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/)
- [Spring Security](https://docs.spring.io/spring-security/)
- [Supabase Documentation](https://supabase.com/docs)

### Community Resources
- [Baeldung Spring Tutorials](https://www.baeldung.com/)
- [Vaadin GitHub Examples](https://github.com/vaadin)
- [Maven Official Guides](https://maven.apache.org/guides/)

### Research Sources
- Multi-module Maven best practices (Baeldung, Java Code Geeks)
- Supabase + Spring Boot integration guides
- Row-Level Security patterns (bytefish, New Sky Security)
- Vaadin TestBench documentation
- Spring Boot multi-tenancy patterns

---

**Document Status**: Active  
**Review Frequency**: Quarterly or after major technology updates  
**Next Review**: 2025-02-17
