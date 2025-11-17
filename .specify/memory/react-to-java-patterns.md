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

## Reference Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Vaadin Documentation](https://vaadin.com/docs/latest)
- [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react)
