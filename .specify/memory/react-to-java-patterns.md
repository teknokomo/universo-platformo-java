# React to Java/Vaadin Pattern Translation Guide

This guide documents common patterns from the Universo Platformo React implementation and their equivalent implementations in Java/Vaadin/Spring.

## Package Structure Translation

### React (PNPM Workspace)
```
packages/
├── clusters-frt/
│   └── base/
│       ├── src/
│       │   ├── api/
│       │   ├── hooks/
│       │   ├── pages/
│       │   ├── components/
│       │   └── i18n/
│       ├── package.json
│       └── tsconfig.json
```

### Java (Maven Multi-Module)
```
packages/
├── clusters-frt/
│   └── base/
│       ├── src/
│       │   ├── main/java/pro/universo/clusters/ui/
│       │   │   ├── views/
│       │   │   ├── components/
│       │   │   └── layouts/
│       │   └── main/resources/
│       │       ├── messages_en.properties
│       │       └── messages_ru.properties
│       └── pom.xml
```

## Dependency Management

### React (PNPM Workspace Catalog)
```yaml
# pnpm-workspace.yaml
catalog:
  typescript: ^5.8.3
  react: ^18.3.1
  '@mui/material': ^6.5.0
```

### Java (Maven BOM)
```xml
<!-- Parent pom.xml -->
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

## UI Component Patterns

### React (Material-UI)
```typescript
import { Button, TextField, DataGrid } from '@mui/material';

function ClusterList() {
  const [name, setName] = useState('');
  
  return (
    <>
      <TextField
        label="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
      />
      <Button variant="contained" onClick={handleSubmit}>
        Create
      </Button>
    </>
  );
}
```

### Java (Vaadin)
```java
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.grid.Grid;

public class ClusterListView extends VerticalLayout {
    private final TextField nameField = new TextField("Name");
    
    public ClusterListView() {
        nameField.setRequired(true);
        
        Button createButton = new Button("Create", e -> handleSubmit());
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        add(nameField, createButton);
    }
}
```

## Data Fetching

### React (React Query)
```typescript
import { useQuery, useMutation } from '@tanstack/react-query';

function useClusters() {
  return useQuery({
    queryKey: ['clusters'],
    queryFn: () => api.getClusters()
  });
}

function useCreateCluster() {
  return useMutation({
    mutationFn: (data) => api.createCluster(data),
    onSuccess: () => {
      queryClient.invalidateQueries(['clusters']);
    }
  });
}
```

### Java (Vaadin DataProvider + Spring Service)
```java
@Service
public class ClusterService {
    
    @Autowired
    private ClusterRepository repository;
    
    public Page<Cluster> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @Transactional
    public Cluster create(CreateClusterRequest request) {
        Cluster cluster = new Cluster();
        cluster.setName(request.getName());
        return repository.save(cluster);
    }
}

// In Vaadin view
grid.setItems(query -> {
    Pageable pageable = PageRequest.of(
        query.getPage(), 
        query.getPageSize()
    );
    return clusterService.findAll(pageable).getContent().stream();
});
```

## Form Validation

### React (Zod + React Hook Form)
```typescript
import { z } from 'zod';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';

const schema = z.object({
  name: z.string().min(1, 'Name is required'),
  description: z.string().optional()
});

function ClusterForm() {
  const { register, handleSubmit, formState: { errors } } = useForm({
    resolver: zodResolver(schema)
  });
  
  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <TextField
        {...register('name')}
        error={!!errors.name}
        helperText={errors.name?.message}
      />
    </form>
  );
}
```

### Java (Bean Validation + Vaadin Binder)
```java
@Entity
public class Cluster {
    @NotBlank(message = "{cluster.name.required}")
    @Size(max = 255)
    private String name;
    
    @Size(max = 5000)
    private String description;
}

// In Vaadin form
Binder<Cluster> binder = new Binder<>(Cluster.class);
binder.forField(nameField)
    .asRequired(getTranslation("cluster.name.required"))
    .withValidator(name -> name.length() <= 255, 
                   getTranslation("cluster.name.maxlength"))
    .bind(Cluster::getName, Cluster::setName);

binder.addStatusChangeListener(event -> {
    saveButton.setEnabled(binder.isValid());
});
```

## Internationalization

### React (i18next)
```typescript
// i18n/locales/en/clusters.json
{
  "list": {
    "title": "Clusters",
    "create": "Create Cluster"
  }
}

// Component
import { useTranslation } from 'react-i18next';

function ClusterList() {
  const { t } = useTranslation('clusters');
  
  return (
    <h1>{t('list.title')}</h1>
  );
}
```

### Java (ResourceBundle)
```properties
# messages_en.properties
clusters.list.title=Clusters
clusters.list.create=Create Cluster

# messages_ru.properties
clusters.list.title=Кластеры
clusters.list.create=Создать кластер
```

```java
// In Vaadin component
public class ClusterListView extends VerticalLayout {
    public ClusterListView() {
        H1 title = new H1(getTranslation("clusters.list.title"));
        Button create = new Button(
            getTranslation("clusters.list.create")
        );
        add(title, create);
    }
}
```

## API Endpoints

### Express (React Backend)
```typescript
router.get('/clusters', async (req, res) => {
  const clusters = await clusterService.findAll();
  res.json({ success: true, data: clusters });
});

router.post('/clusters', async (req, res) => {
  const cluster = await clusterService.create(req.body);
  res.status(201).json({ success: true, data: cluster });
});
```

### Spring (Java Backend)
```java
@RestController
@RequestMapping("/api/clusters")
public class ClusterController {
    
    @Autowired
    private ClusterService service;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClusterResponse>>> list() {
        List<ClusterResponse> clusters = service.findAll();
        return ResponseEntity.ok(
            new ApiResponse<>(true, clusters)
        );
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<ClusterResponse>> create(
            @Valid @RequestBody CreateClusterRequest request) {
        ClusterResponse cluster = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, cluster));
    }
}
```

## Error Handling

### React
```typescript
// API client
try {
  const data = await api.createCluster(payload);
  showNotification('Success', 'success');
} catch (error) {
  if (error.response?.status === 400) {
    showNotification(error.response.data.message, 'error');
  }
}

// Component
import { useSnackbar } from 'notistack';

const { enqueueSnackbar } = useSnackbar();
enqueueSnackbar('Cluster created', { variant: 'success' });
```

### Java
```java
// Global exception handler
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            ValidationException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(ex.getMessage()));
    }
}

// Vaadin notification
try {
    clusterService.create(cluster);
    Notification.show(
        getTranslation("cluster.create.success"),
        3000,
        Notification.Position.TOP_END
    ).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
} catch (ValidationException ex) {
    Notification.show(
        ex.getMessage(),
        5000,
        Notification.Position.TOP_END
    ).addThemeVariants(NotificationVariant.LUMO_ERROR);
}
```

## Routing

### React Router
```typescript
import { Routes, Route, useNavigate, useParams } from 'react-router-dom';

function App() {
  return (
    <Routes>
      <Route path="/clusters" element={<ClusterList />} />
      <Route path="/clusters/:id" element={<ClusterDetail />} />
    </Routes>
  );
}

function ClusterDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  
  return (
    <Button onClick={() => navigate('/clusters')}>
      Back
    </Button>
  );
}
```

### Vaadin Router
```java
@Route(value = "clusters", layout = MainLayout.class)
@PageTitle("Clusters")
public class ClusterListView extends VerticalLayout {
}

@Route(value = "clusters/:id", layout = MainLayout.class)
@PageTitle("Cluster Details")
public class ClusterDetailView extends VerticalLayout 
                             implements BeforeEnterObserver {
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String id = event.getRouteParameters()
            .get("id")
            .orElseThrow();
        loadCluster(id);
    }
    
    private void navigateBack() {
        UI.getCurrent().navigate(ClusterListView.class);
    }
}
```

## Authentication

### React (Supabase JWT)
```typescript
import { useAuth } from './hooks/useAuth';

function ProtectedRoute({ children }) {
  const { user, loading } = useAuth();
  
  if (loading) return <Loading />;
  if (!user) return <Navigate to="/login" />;
  
  return children;
}

// API client
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('access_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

### Java (Spring Security)
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt());
        return http.build();
    }
}

// Vaadin view
@Route("clusters")
@PermitAll
public class ClusterListView extends VerticalLayout {
    // Automatically requires authentication
}
```

## Database Access

### TypeORM (React Backend)
```typescript
@Entity()
export class Cluster {
  @PrimaryGeneratedColumn('uuid')
  id: string;
  
  @Column({ type: 'varchar', length: 255 })
  name: string;
  
  @ManyToMany(() => Domain)
  @JoinTable({
    name: 'cluster_domains'
  })
  domains: Domain[];
}

// Usage
const cluster = await clusterRepository.findOne({
  where: { id },
  relations: ['domains']
});
```

### JPA (Java Backend)
```java
@Entity
@Table(name = "clusters")
public class Cluster {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @ManyToMany
    @JoinTable(
        name = "cluster_domains",
        joinColumns = @JoinColumn(name = "cluster_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private Set<Domain> domains = new HashSet<>();
}

// Repository
public interface ClusterRepository 
        extends JpaRepository<Cluster, UUID> {
    
    @Query("SELECT c FROM Cluster c JOIN FETCH c.domains WHERE c.id = :id")
    Optional<Cluster> findByIdWithDomains(@Param("id") UUID id);
}
```

## Anti-Patterns to Avoid

### ❌ Don't Port React-Specific Patterns
- Don't try to implement React hooks in Java
- Don't replicate client-side routing in backend
- Don't use JSON for everything (leverage Java type system)

### ❌ Don't Ignore Java Conventions
- Don't use camelCase for class names
- Don't skip Bean Validation annotations
- Don't bypass Spring's dependency injection

### ✅ Do Adapt Conceptually
- Understand the intent, not just the code
- Use idiomatic Java/Spring/Vaadin patterns
- Leverage compile-time type safety
- Embrace Spring's convention over configuration

## Summary

| Aspect | React | Java/Vaadin |
|--------|-------|-------------|
| **Package Management** | PNPM workspace catalog | Maven BOM |
| **UI Components** | Material-UI | Vaadin components |
| **Data Fetching** | React Query | Spring Data + Vaadin DataProvider |
| **Validation** | Zod + React Hook Form | Bean Validation + Vaadin Binder |
| **i18n** | i18next | ResourceBundle |
| **Routing** | React Router | Vaadin @Route |
| **API** | Express | Spring REST |
| **ORM** | TypeORM | JPA/Hibernate |
| **Auth** | JWT in localStorage | Spring Security OAuth2 |
| **Build** | Turbo + tsdown | Maven profiles |

## Row-Level Security (RLS) Pattern

### React (TypeORM + PostgreSQL + JWT)
```typescript
// Middleware: ensureAuthWithRls
export function createEnsureAuthWithRls(options: {
    getDataSource: () => DataSource
}): RequestHandler {
    return async (req: Request, res: Response, next: NextFunction) => {
        // 1. Validate JWT
        ensureAuth(req, res, async (authErr?: any) => {
            if (authErr) return next(authErr)
            
            // 2. Create dedicated QueryRunner
            const dataSource = options.getDataSource()
            const queryRunner = dataSource.createQueryRunner()
            await queryRunner.connect()
            
            // 3. Apply RLS context (set PostgreSQL session variables)
            const user = (req as any).user
            await applyRlsContext(queryRunner, user.supabaseAccessToken)
            
            // 4. Attach to request
            (req as RequestWithDbContext).dbContext = {
                queryRunner,
                manager: queryRunner.manager
            }
            
            // 5. Cleanup on finish
            res.on('finish', () => queryRunner.release())
            next()
        })
    }
}

// Apply RLS context
async function applyRlsContext(
    queryRunner: QueryRunner, 
    accessToken: string
) {
    const jwtSecret = new TextEncoder().encode(process.env.SUPABASE_JWT_SECRET!)
    const { payload } = await jose.jwtVerify(accessToken, jwtSecret)
    
    await queryRunner.query(
        `SELECT set_config('request.jwt.claims', $1::json, true)`,
        [JSON.stringify(payload)]
    )
}

// Usage in routes
router.get('/clusters/:id', ensureAuthWithRls, async (req, res) => {
    const manager = req.dbContext.manager
    const repo = manager.getRepository(Cluster)
    
    // Automatic row filtering by RLS policies
    const cluster = await repo.findOne({ where: { id: req.params.id } })
    res.json({ success: true, data: cluster })
})
```

### Java (Spring Security + JPA + PostgreSQL)
```java
// Security Filter for RLS context
@Component
public class RlsSecurityFilter extends OncePerRequestFilter {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JwtDecoder jwtDecoder;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        String token = extractToken(request);
        if (token != null) {
            try {
                Jwt jwt = jwtDecoder.decode(token);
                
                // Store JWT claims in request attribute
                Map<String, Object> claims = jwt.getClaims();
                request.setAttribute("jwt.claims", claims);
                
                // For RLS, we'd use a TransactionCallback to set session vars
                // This is more complex in Java - typically done per-query
                
                filterChain.doFilter(request, response);
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
    
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

// Custom EntityManager wrapper for RLS
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RlsAwareEntityManager {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private HttpServletRequest request;
    
    public <T> T executeWithRls(Function<EntityManager, T> operation) {
        Session session = entityManager.unwrap(Session.class);
        
        return session.doReturningWork(connection -> {
            // Set PostgreSQL session variables
            Map<String, Object> claims = 
                (Map<String, Object>) request.getAttribute("jwt.claims");
            
            if (claims != null) {
                String claimsJson = new ObjectMapper().writeValueAsString(claims);
                try (PreparedStatement stmt = connection.prepareStatement(
                        "SELECT set_config('request.jwt.claims', ?::json, true)")) {
                    stmt.setString(1, claimsJson);
                    stmt.execute();
                }
            }
            
            // Execute the operation
            return operation.apply(entityManager);
        });
    }
}

// Usage in service
@Service
public class ClusterService {
    
    @Autowired
    private RlsAwareEntityManager rlsEntityManager;
    
    public Cluster findById(UUID id) {
        return rlsEntityManager.executeWithRls(em -> {
            return em.find(Cluster.class, id);
        });
    }
}

// PostgreSQL RLS Policy (same for both)
CREATE POLICY cluster_isolation_policy ON clusters
    USING (
        created_by = (current_setting('request.jwt.claims')::json->>'sub')
        OR id IN (
            SELECT cluster_id FROM cluster_users 
            WHERE user_id = (current_setting('request.jwt.claims')::json->>'sub')
        )
    );
```

**Note**: RLS in Java is more complex than Node.js because:
- Java's connection pooling makes per-request session variables tricky
- TypeORM's QueryRunner model is simpler than JPA's EntityManager
- Consider using Supabase PostgREST directly for RLS benefits
- Alternative: Application-level filtering with Spring Security expressions

## Universal Pagination Pattern

### React (TanStack Query + Zod)
```typescript
// Backend: Pagination schema
export const paginationSchema = z.object({
    page: z.number().min(1).default(1),
    limit: z.number().min(1).max(100).default(10),
    search: z.string().optional(),
    sortBy: z.string().optional(),
    sortOrder: z.enum(['asc', 'desc']).default('desc')
})

// Frontend: Paginated hook
function usePaginatedList<T>(config: {
    queryKey: any[]
    endpoint: string
    params?: PaginationParams
}) {
    return useInfiniteQuery({
        queryKey: config.queryKey,
        queryFn: async ({ pageParam = 1 }) => {
            const response = await api.get(config.endpoint, {
                params: { ...config.params, page: pageParam }
            })
            return response.data
        },
        initialPageParam: 1,
        getNextPageParam: (lastPage) => 
            lastPage.hasMore ? lastPage.page + 1 : undefined
    })
}

// Component usage
function ClusterList() {
    const { 
        data, 
        fetchNextPage, 
        hasNextPage,
        isLoading 
    } = usePaginatedList<Cluster>({
        queryKey: ['clusters', filters],
        endpoint: '/api/clusters',
        params: filters
    })
    
    return (
        <InfiniteScroll
            dataLength={data?.pages.length || 0}
            next={fetchNextPage}
            hasMore={hasNextPage}
        >
            {data?.pages.map(page => 
                page.items.map(cluster => 
                    <ClusterCard key={cluster.id} cluster={cluster} />
                )
            )}
        </InfiniteScroll>
    )
}
```

### Java (Spring Data + Vaadin)
```java
// DTO for pagination
@Data
public class PageRequest {
    private int page = 1;
    private int size = 10;
    private String search;
    private String sortBy = "createdAt";
    private Sort.Direction sortOrder = Sort.Direction.DESC;
    
    public Pageable toPageable() {
        return PageRequest.of(
            page - 1,  // Spring uses 0-based indexing
            size,
            Sort.by(sortOrder, sortBy)
        );
    }
}

// Generic specification builder
public class GenericSpecification<T> {
    
    public static <T> Specification<T> buildSearchSpec(
            String search, 
            String... fields) {
        return (root, query, cb) -> {
            if (search == null || search.isEmpty()) {
                return cb.conjunction();
            }
            
            List<Predicate> predicates = Arrays.stream(fields)
                .map(field -> cb.like(
                    cb.lower(root.get(field)),
                    "%" + search.toLowerCase() + "%"
                ))
                .collect(Collectors.toList());
            
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}

// Service implementation
@Service
public class ClusterService {
    
    @Autowired
    private ClusterRepository repository;
    
    public Page<ClusterResponse> findAll(PageRequest request) {
        Specification<Cluster> spec = GenericSpecification
            .buildSearchSpec(request.getSearch(), "name", "description");
        
        Page<Cluster> page = repository.findAll(spec, request.toPageable());
        
        return page.map(this::toResponse);
    }
}

// Vaadin Grid with lazy loading
@Route("clusters")
public class ClusterListView extends VerticalLayout {
    
    private final ClusterService service;
    private final Grid<ClusterResponse> grid = new Grid<>();
    
    public ClusterListView(ClusterService service) {
        this.service = service;
        configureGrid();
    }
    
    private void configureGrid() {
        grid.addColumn(ClusterResponse::getName).setHeader("Name");
        grid.addColumn(ClusterResponse::getDescription).setHeader("Description");
        
        // Lazy loading with Spring Data
        grid.setItems(query -> {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPage(query.getPage());
            pageRequest.setSize(query.getPageSize());
            
            // Apply filters from UI
            if (searchField.getValue() != null) {
                pageRequest.setSearch(searchField.getValue());
            }
            
            Page<ClusterResponse> page = service.findAll(pageRequest);
            return page.getContent().stream();
        });
        
        // Enable lazy loading
        grid.setPageSize(10);
        grid.setHeight("600px");
        
        add(grid);
    }
}
```

## Three-Tier Entity Pattern

### React (TypeORM Entities)
```typescript
// Primary: Cluster
@Entity('clusters')
export class Cluster {
    @PrimaryGeneratedColumn('uuid')
    id: string
    
    @Column({ length: 255 })
    name: string
    
    @Column({ length: 5000, nullable: true })
    description?: string
    
    @Column({ name: 'created_by' })
    createdBy: string
    
    @CreateDateColumn({ name: 'created_at' })
    createdAt: Date
    
    @UpdateDateColumn({ name: 'updated_at' })
    updatedAt: Date
    
    @OneToMany(() => Domain, domain => domain.cluster)
    domains: Domain[]
    
    @ManyToMany(() => User, user => user.clusters)
    @JoinTable({ name: 'cluster_users' })
    members: User[]
}

// Secondary: Domain
@Entity('domains')
export class Domain {
    @PrimaryGeneratedColumn('uuid')
    id: string
    
    @Column({ length: 255 })
    name: string
    
    @Column({ name: 'cluster_id' })
    clusterId: string
    
    @ManyToOne(() => Cluster, cluster => cluster.domains)
    @JoinColumn({ name: 'cluster_id' })
    cluster: Cluster
    
    @OneToMany(() => Resource, resource => resource.domain)
    resources: Resource[]
}

// Tertiary: Resource
@Entity('resources')
export class Resource {
    @PrimaryGeneratedColumn('uuid')
    id: string
    
    @Column({ length: 255 })
    name: string
    
    @Column({ length: 100 })
    type: string
    
    @Column({ name: 'domain_id' })
    domainId: string
    
    @Column({ name: 'cluster_id' })
    clusterId: string  // Denormalized for efficient queries
    
    @ManyToOne(() => Domain, domain => domain.resources)
    @JoinColumn({ name: 'domain_id' })
    domain: Domain
}
```

### Java (JPA Entities)
```java
// Primary: Cluster
@Entity
@Table(name = "clusters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cluster {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(length = 5000)
    private String description;
    
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    
    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Domain> domains = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "cluster_users",
        joinColumns = @JoinColumn(name = "cluster_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();
}

// Secondary: Domain
@Entity
@Table(name = "domains")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Domain {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id", nullable = false)
    private Cluster cluster;
    
    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resource> resources = new HashSet<>();
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}

// Tertiary: Resource
@Entity
@Table(name = "resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(nullable = false, length = 100)
    private String type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;
    
    // Denormalized for efficient queries
    @Column(name = "cluster_id", nullable = false)
    private UUID clusterId;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
```

## Member Management Pattern

### React (Role-Based Access)
```typescript
// Member entity with roles
export enum ClusterRole {
    OWNER = 'owner',
    ADMIN = 'admin',
    MEMBER = 'member'
}

@Entity('cluster_users')
export class ClusterUser {
    @PrimaryColumn({ name: 'cluster_id' })
    clusterId: string
    
    @PrimaryColumn({ name: 'user_id' })
    userId: string
    
    @Column({ type: 'enum', enum: ClusterRole })
    role: ClusterRole
    
    @CreateDateColumn({ name: 'joined_at' })
    joinedAt: Date
}

// Member management component
function MemberList({ clusterId }: { clusterId: string }) {
    const { data: members } = useQuery({
        queryKey: ['cluster-members', clusterId],
        queryFn: () => api.get(`/clusters/${clusterId}/members`)
    })
    
    const updateRole = useMutation({
        mutationFn: ({ userId, role }: { userId: string, role: ClusterRole }) =>
            api.patch(`/clusters/${clusterId}/members/${userId}`, { role })
    })
    
    return (
        <List>
            {members?.map(member => (
                <ListItem key={member.userId}>
                    <ListItemText primary={member.userName} />
                    <Select
                        value={member.role}
                        onChange={(e) => updateRole.mutate({
                            userId: member.userId,
                            role: e.target.value as ClusterRole
                        })}
                    >
                        <MenuItem value="owner">Owner</MenuItem>
                        <MenuItem value="admin">Admin</MenuItem>
                        <MenuItem value="member">Member</MenuItem>
                    </Select>
                </ListItem>
            ))}
        </List>
    )
}
```

### Java (Spring Security + JPA)
```java
// Member entity with roles
@Entity
@Table(name = "cluster_users")
@Data
@IdClass(ClusterUserId.class)
public class ClusterUser {
    
    @Id
    @Column(name = "cluster_id")
    private UUID clusterId;
    
    @Id
    @Column(name = "user_id")
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClusterRole role;
    
    @CreatedDate
    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id", insertable = false, updatable = false)
    private Cluster cluster;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}

@Data
public class ClusterUserId implements Serializable {
    private UUID clusterId;
    private String userId;
}

public enum ClusterRole {
    OWNER, ADMIN, MEMBER
}

// Permission evaluator
@Component
public class ClusterPermissionEvaluator implements PermissionEvaluator {
    
    @Autowired
    private ClusterUserRepository clusterUserRepository;
    
    @Override
    public boolean hasPermission(
            Authentication authentication,
            Object targetDomainObject,
            Object permission) {
        
        if (!(targetDomainObject instanceof UUID)) {
            return false;
        }
        
        UUID clusterId = (UUID) targetDomainObject;
        String userId = authentication.getName();
        
        Optional<ClusterUser> member = clusterUserRepository
            .findByClusterIdAndUserId(clusterId, userId);
        
        if (member.isEmpty()) {
            return false;
        }
        
        // Check permission based on role
        String requiredPermission = (String) permission;
        return hasRolePermission(member.get().getRole(), requiredPermission);
    }
    
    private boolean hasRolePermission(ClusterRole role, String permission) {
        return switch (permission) {
            case "DELETE" -> role == ClusterRole.OWNER;
            case "EDIT" -> role == ClusterRole.OWNER || role == ClusterRole.ADMIN;
            case "VIEW" -> true;  // All members can view
            default -> false;
        };
    }
    
    @Override
    public boolean hasPermission(
            Authentication authentication,
            Serializable targetId,
            String targetType,
            Object permission) {
        return hasPermission(authentication, targetId, permission);
    }
}

// Vaadin member management view
@Route("clusters/:id/members")
public class ClusterMembersView extends VerticalLayout {
    
    private final ClusterService clusterService;
    private final Grid<ClusterMemberDto> grid = new Grid<>();
    
    public ClusterMembersView(ClusterService clusterService) {
        this.service = clusterService;
        configureGrid();
    }
    
    private void configureGrid() {
        grid.addColumn(ClusterMemberDto::getUserName)
            .setHeader(getTranslation("members.name"));
        
        grid.addComponentColumn(member -> {
            Select<ClusterRole> roleSelect = new Select<>();
            roleSelect.setItems(ClusterRole.values());
            roleSelect.setValue(member.getRole());
            roleSelect.addValueChangeListener(e -> {
                clusterService.updateMemberRole(
                    member.getClusterId(),
                    member.getUserId(),
                    e.getValue()
                );
            });
            return roleSelect;
        }).setHeader(getTranslation("members.role"));
        
        grid.addComponentColumn(member -> {
            Button removeButton = new Button(
                new Icon(VaadinIcon.TRASH),
                e -> removeMember(member)
            );
            removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return removeButton;
        }).setHeader(getTranslation("members.actions"));
        
        add(grid);
    }
}
```

## Reference Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Vaadin Documentation](https://vaadin.com/docs/latest)
- [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react)
- [React Architecture Analysis](.specify/memory/react-architecture-analysis.md)
- [Java/Vaadin/Spring Best Practices](.specify/memory/java-vaadin-spring-best-practices.md)

## Advanced Pattern: Row-Level Security (RLS) Integration

### React Implementation (Not Explicitly Implemented)
React implementation relies on backend API filtering, but lacks database-level security.

### Java/Spring Implementation with PostgreSQL RLS

**Database Setup**:
```sql
-- Enable RLS on clusters table
ALTER TABLE clusters ENABLE ROW LEVEL SECURITY;

-- Policy: Users can access clusters they own or are members of
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

-- Enable RLS on cluster_members table
ALTER TABLE cluster_members ENABLE ROW LEVEL SECURITY;

-- Policy: Users can see members of clusters they have access to
CREATE POLICY cluster_members_access_policy
ON cluster_members
FOR ALL
USING (
    cluster_id IN (
        SELECT id FROM clusters
        WHERE owner_id = current_setting('app.user_id')::uuid
        OR EXISTS (
            SELECT 1 FROM cluster_members cm
            WHERE cm.cluster_id = clusters.id
            AND cm.user_id = current_setting('app.user_id')::uuid
        )
    )
);

-- Create index for performance (critical!)
CREATE INDEX idx_clusters_owner_id ON clusters(owner_id);
CREATE INDEX idx_cluster_members_user_cluster 
    ON cluster_members(user_id, cluster_id);
```

**Spring Integration**:
```java
// Tenant context holder
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

// RLS Filter - sets PostgreSQL session variable
@Component
@Order(1)
public class RlsFilter implements Filter {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        try {
            // Extract user ID from JWT token
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                UUID userId = extractUserId(auth);
                TenantContext.setUserId(userId);
                
                // Set PostgreSQL session variable for RLS
                try (Connection conn = dataSource.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                         "SELECT set_config('app.user_id', ?, false)")) {
                    stmt.setString(1, userId.toString());
                    stmt.execute();
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to set RLS context", e);
                }
            }
            
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
    
    private UUID extractUserId(Authentication auth) {
        if (auth.getPrincipal() instanceof Jwt jwt) {
            String sub = jwt.getSubject();
            return UUID.fromString(sub);
        }
        throw new IllegalStateException("Authentication principal is not a JWT");
    }
}

// Repository remains simple - RLS handles filtering automatically
@Repository
public interface ClusterRepository extends JpaRepository<Cluster, UUID> {
    
    // No explicit user_id filtering needed - RLS handles it!
    List<Cluster> findAll();
    
    // Pagination also works automatically with RLS
    Page<Cluster> findAll(Pageable pageable);
    
    // Search queries work with RLS
    List<Cluster> findByNameContainingIgnoreCase(String name);
}

// Service layer doesn't need to worry about filtering
@Service
@PreAuthorize("isAuthenticated()")
public class ClusterService {
    
    private final ClusterRepository clusterRepository;
    
    public List<ClusterDto> getAllClusters() {
        // RLS automatically filters to clusters user can access
        return clusterRepository.findAll().stream()
            .map(this::toDto)
            .toList();
    }
    
    @Transactional
    public ClusterId createCluster(ClusterForm form) {
        Cluster cluster = new Cluster();
        cluster.setName(form.getName());
        cluster.setDescription(form.getDescription());
        // Automatically set owner from security context
        cluster.setOwnerId(TenantContext.getUserId());
        
        Cluster saved = clusterRepository.save(cluster);
        return new ClusterId(saved.getId());
    }
}
```

**Performance Considerations**:
```sql
-- Monitor RLS query performance
EXPLAIN ANALYZE
SELECT * FROM clusters
WHERE current_setting('app.user_id')::uuid = owner_id;

-- If sequential scan appears, ensure indexes exist
-- For complex RLS with arrays/lists, use GIN indexes
CREATE INDEX idx_cluster_member_ids 
    ON clusters USING GIN (member_ids);
```

**Best Practices for RLS**:

1. **Always Index RLS Columns**: Critical for performance as data grows
   - B-tree indexes for scalar columns (`owner_id`, `user_id`)
   - GIN indexes for array columns (`member_ids[]`)

2. **Test Performance**: Use `EXPLAIN ANALYZE` to verify index usage
   ```sql
   SET app.user_id = 'some-uuid';
   EXPLAIN ANALYZE SELECT * FROM clusters;
   ```

3. **Defense in Depth**: Use RLS + Spring Security
   - RLS: Database-level security (last line of defense)
   - Spring Security: Application-level security (first line of defense)
   - Both layers together prevent data leaks

4. **Session Variables**: Set once per request, applies to all queries
   - Use filter/interceptor at application startup
   - Clear context after request (ThreadLocal cleanup)

5. **Admin Bypass**: Create separate policies for admin roles
   ```sql
   CREATE POLICY admin_full_access_policy
   ON clusters
   FOR ALL
   TO admin_role
   USING (true);
   ```

6. **Testing**: Verify RLS policies with different users
   ```sql
   SET app.user_id = 'user-1-uuid';
   SELECT * FROM clusters;  -- Should only see user-1's clusters
   
   SET app.user_id = 'user-2-uuid';
   SELECT * FROM clusters;  -- Should only see user-2's clusters
   ```

**Advantages of RLS Pattern**:
- ✅ Security enforced at database level (can't be bypassed)
- ✅ Simpler application code (no explicit filtering)
- ✅ Works with any query automatically
- ✅ Prevents SQL injection risks
- ✅ Supports complex multi-tenant scenarios
- ✅ Perfect for Supabase integration

**Disadvantages to Consider**:
- ⚠️ Must index RLS columns for performance
- ⚠️ Complex policies can impact query performance
- ⚠️ Requires PostgreSQL (not portable to all databases)
- ⚠️ Testing requires database-level setup

## Summary

This pattern translation guide provides comprehensive mappings from React/Express patterns to Java/Vaadin/Spring equivalents, with special attention to:
- Component architecture and state management
- Data fetching and caching strategies
- Routing and navigation patterns
- Member management and RBAC
- Row-Level Security (RLS) implementation
- Performance optimization techniques

For detailed best practices and implementation guidelines, see:
- [Java/Vaadin/Spring Best Practices](.specify/memory/java-vaadin-spring-best-practices.md)
- [React Architecture Analysis](.specify/memory/react-architecture-analysis.md)
