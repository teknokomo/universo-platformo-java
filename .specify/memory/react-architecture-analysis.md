# Universo Platformo React - Architectural Analysis

**Date**: 2025-11-17  
**Purpose**: Deep analysis of React repository architecture for Java implementation reference  
**Source**: https://github.com/teknokomo/universo-platformo-react

## Executive Summary

This document provides a comprehensive analysis of the Universo Platformo React repository architecture, identifying key patterns, features, and best practices that should be considered for the Java/Vaadin/Spring implementation.

### Key Findings

1. **Feature-Complete Monorepo**: 32+ packages implementing modular architecture
2. **Three-Tier Entity Pattern**: Consistently used across features (Primary/Secondary/Tertiary)
3. **Advanced RLS Integration**: Row-Level Security pattern with JWT context propagation
4. **Comprehensive i18n Architecture**: Centralized namespace registration system
5. **UPDL Node System**: Core abstraction layer for multi-platform export
6. **Template-First Export**: Reusable export templates for multiple technologies

## Repository Structure Analysis

### Package Organization

The React repository uses a sophisticated PNPM workspace monorepo with 32+ packages:

#### Core Infrastructure Packages
- `universo-api-client` - Centralized API client
- `universo-i18n` - Internationalization namespace registry
- `universo-types` - Shared TypeScript types
- `universo-utils` - Common utilities
- `universo-template-mui` - Reusable MUI components

#### Feature Packages (Frontend/Backend Pairs)

**Three-Tier Pattern Features**:
1. **Clusters** (`clusters-frt` / `clusters-srv`)
   - Primary: Clusters
   - Secondary: Domains
   - Tertiary: Resources
   - Pattern: Organization → Division → Assets

2. **Metaverses** (`metaverses-frt` / `metaverses-srv`)
   - Primary: Metaverses
   - Secondary: Sections
   - Tertiary: Entities
   - Pattern: World → Area → Objects

3. **Uniks** (`uniks-frt` / `uniks-srv`)
   - Primary: Uniks (Workflows)
   - Secondary: Spaces
   - Tertiary: Agents
   - Pattern: Project → Workspace → Executors

4. **Projects** (`projects-frt` / `projects-srv`)
   - Primary: Projects
   - Secondary: Versions
   - Tertiary: Assets
   - Pattern: Container → Release → Resources

**Specialized Features**:
- **Auth** (`auth-frt` / `auth-srv`) - Authentication with Passport.js + Supabase
- **Profile** (`profile-frt` / `profile-srv`) - User profile management
- **Publish** (`publish-frt` / `publish-srv`) - Multi-platform export system
- **Analytics** (`analytics-frt`) - Analytics and tracking
- **Space Builder** (`space-builder-frt` / `space-builder-srv`) - Visual editor
- **Multiplayer Colyseus** (`multiplayer-colyseus-srv`) - Real-time multiplayer

**UPDL System**:
- **UPDL** (`updl`) - Universal Platform Description Language
  - Core abstraction nodes: Space, Entity, Component, Event, Action, Data, Universo
  - Multi-platform export capability

**Legacy Components** (scheduled for removal):
- `flowise-*` packages - Legacy Flowise integration being phased out

### Technology Stack

#### Frontend
- **Framework**: React 18.3.1
- **UI Library**: Material-UI (MUI) v6.5.0
- **Routing**: React Router v6.28.0
- **State Management**: TanStack Query v5.62.13
- **Forms**: React Hook Form v7.54.2 + Zod v3.25.76
- **i18n**: i18next 23.16.8 + react-i18next
- **Build Tool**: Vite v5.4.19
- **Testing**: Vitest v2.1.8 + happy-dom

#### Backend
- **Runtime**: Node.js >=18.15.0
- **Framework**: Express
- **Database**: PostgreSQL (via Supabase)
- **ORM**: TypeORM v0.3.20
- **Authentication**: Passport.js with JWT
- **Validation**: Zod
- **Rate Limiting**: express-rate-limit + Redis

#### Monorepo Management
- **Package Manager**: PNPM v9+ with workspace catalog
- **Build System**: Turbo for parallel builds
- **TypeScript**: v5.8.3 (unified across monorepo)

## Key Architectural Patterns

### 1. Three-Tier Entity Relationship Pattern (CRITICAL)

**Pattern**: Primary → Secondary → Tertiary entities with hierarchical relationships

**Implementation in React**:
```typescript
// Primary Entity: Cluster
interface Cluster {
  id: string
  name: string
  description?: string
  created_by: string
  created_at: Date
  updated_at: Date
}

// Secondary Entity: Domain
interface Domain {
  id: string
  name: string
  cluster_id: string  // Foreign key to primary
  created_at: Date
}

// Tertiary Entity: Resource
interface Resource {
  id: string
  name: string
  type: string
  domain_id: string  // Foreign key to secondary
  cluster_id: string // Direct reference to primary for queries
}

// Many-to-Many Join Tables
interface ClusterUser {
  cluster_id: string
  user_id: string
  role: 'owner' | 'admin' | 'member'
}
```

**Variants Across Features**:

| Feature | Primary | Secondary | Tertiary | Special Notes |
|---------|---------|-----------|----------|---------------|
| Clusters | Clusters | Domains | Resources | Standard three-tier |
| Metaverses | Metaverses | Sections | Entities | Standard three-tier |
| Uniks | Uniks | Spaces | Agents | Spaces can contain Canvases |
| Projects | Projects | Versions | Assets | Version-based hierarchy |

**UI Pattern**:
- List view with search, pagination, sorting
- Detail view with tabs for secondary entities
- Create/Edit modals with form validation
- Member management for collaborative features

**Java Translation**:
```java
@Entity
@Table(name = "clusters")
public class Cluster {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 5000)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL)
    private Set<Domain> domains = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "cluster_users",
        joinColumns = @JoinColumn(name = "cluster_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ClusterUser> members = new HashSet<>();
}
```

### 2. RLS Integration Pattern (CRITICAL)

**Purpose**: Row-Level Security with JWT context propagation for multi-tenant data isolation

**React Implementation**:
- JWT tokens from Supabase contain user claims
- Express middleware extracts JWT and sets PostgreSQL session variables
- TypeORM queries automatically filtered by RLS policies
- Dedicated QueryRunner per request for isolation

**Key Components**:
1. **Middleware**: `ensureAuthWithRls` - Validates JWT, creates QueryRunner, applies RLS context
2. **Context Utility**: `applyRlsContext` - Sets PostgreSQL session variables from JWT claims
3. **Database Policies**: PostgreSQL RLS policies use `current_setting()` to filter rows

**Java Translation Pattern**:
```java
@Component
public class RlsSecurityFilter extends OncePerRequestFilter {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwt = (JwtAuthenticationToken) auth.getPrincipal();
            
            try (Connection conn = dataSource.getConnection()) {
                // Set PostgreSQL session variables for RLS
                String userId = jwt.getToken().getClaim("sub");
                String email = jwt.getToken().getClaim("email");
                
                conn.prepareStatement(
                    "SELECT set_config('request.jwt.claims', ?::json, true)"
                ).executeUpdate();
                
                // Store connection in request attribute for repository access
                request.setAttribute("rlsConnection", conn);
                
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
```

**Benefits**:
- Database-enforced security (defense in depth)
- Automatic row filtering (no manual WHERE clauses)
- Zero-trust architecture (application code cannot bypass)
- Centralized policy management in migrations

### 3. Centralized i18n Architecture Pattern

**Pattern**: Single namespace registry with lazy loading

**React Implementation**:
```typescript
// Registry in @universo/i18n
import i18next from 'i18next'

const registeredNamespaces = new Set<string>()

export function registerNamespace(
    name: string, 
    resources: { en: any, ru: any }
) {
    if (registeredNamespaces.has(name)) return
    
    i18next.addResourceBundle('en', name, resources.en)
    i18next.addResourceBundle('ru', name, resources.ru)
    registeredNamespaces.add(name)
}

// Usage in feature packages
import { registerNamespace } from '@universo/i18n'
import clustersEn from './locales/en.json'
import clustersRu from './locales/ru.json'

registerNamespace('clusters', { en: clustersEn, ru: clustersRu })

// In components
const { t } = useTranslation('clusters')
t('list.title') // "Clusters" or "Кластеры"
```

**Java Translation**:
```java
// ResourceBundle approach (standard Java)
@Component
public class I18nConfig {
    
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = 
            new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
            "classpath:messages/clusters",
            "classpath:messages/metaverses",
            "classpath:messages/common"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}

// In Vaadin views
public class ClusterListView extends VerticalLayout {
    public ClusterListView() {
        H1 title = new H1(getTranslation("clusters.list.title"));
    }
}
```

### 4. Universal List Pattern (CRITICAL)

**Pattern**: Reusable paginated list with search, sort, and filtering

**React Implementation**:
```typescript
// Backend: Pagination schema
export const paginationSchema = z.object({
    page: z.number().min(1).default(1),
    limit: z.number().min(1).max(100).default(10),
    search: z.string().optional(),
    sortBy: z.string().optional(),
    sortOrder: z.enum(['asc', 'desc']).default('desc')
})

// Frontend: Generic list hook
function usePaginatedList<T>(config: {
    queryKey: string[]
    endpoint: string
    initialFilters?: PaginationParams
}) {
    return useInfiniteQuery({
        queryKey: config.queryKey,
        queryFn: ({ pageParam }) => 
            api.get(config.endpoint, { params: pageParam }),
        initialPageParam: { page: 1, limit: 10 },
        getNextPageParam: (lastPage) => 
            lastPage.hasMore ? { page: lastPage.page + 1 } : undefined
    })
}
```

**Java Translation**:
```java
// DTO for pagination
@Data
public class PageRequest {
    private int page = 1;
    private int size = 10;
    private String search;
    private String sortBy;
    private String sortOrder = "desc";
}

// Generic service method
@Service
public class GenericListService<T> {
    
    public Page<T> findAll(
            PageRequest pageRequest,
            Specification<T> spec
    ) {
        Pageable pageable = PageRequest.of(
            pageRequest.getPage() - 1,
            pageRequest.getSize(),
            Sort.Direction.fromString(pageRequest.getSortOrder()),
            pageRequest.getSortBy()
        );
        
        return repository.findAll(spec, pageable);
    }
}

// Vaadin Grid with lazy loading
Grid<Cluster> grid = new Grid<>();
grid.setItems(query -> {
    return clusterService.findAll(
        PageRequest.of(query.getPage(), query.getPageSize()),
        buildSpec(filters)
    ).stream();
});
```

### 5. TypeORM Repository Pattern

**Pattern**: Abstracted data access with TypeORM repositories

**React Implementation**:
```typescript
// Entity definition
@Entity('clusters')
export class Cluster {
    @PrimaryGeneratedColumn('uuid')
    id: string
    
    @Column({ type: 'varchar', length: 255 })
    name: string
    
    @ManyToMany(() => Domain)
    @JoinTable({ name: 'cluster_domains' })
    domains: Domain[]
}

// Repository usage in routes
router.get('/clusters/:id', ensureAuthWithRls, async (req, res) => {
    const manager = req.dbContext.manager
    const repo = manager.getRepository(Cluster)
    
    const cluster = await repo.findOne({
        where: { id: req.params.id },
        relations: ['domains', 'members']
    })
    
    res.json({ success: true, data: cluster })
})
```

**Java Translation**:
```java
// Spring Data JPA (similar abstraction)
@Repository
public interface ClusterRepository extends JpaRepository<Cluster, UUID> {
    
    @Query("SELECT c FROM Cluster c " +
           "LEFT JOIN FETCH c.domains " +
           "LEFT JOIN FETCH c.members " +
           "WHERE c.id = :id")
    Optional<Cluster> findByIdWithRelations(@Param("id") UUID id);
    
    Page<Cluster> findByNameContainingIgnoreCase(
        String name, 
        Pageable pageable
    );
}

// Service layer
@Service
public class ClusterService {
    
    @Autowired
    private ClusterRepository repository;
    
    public Cluster findById(UUID id) {
        return repository.findByIdWithRelations(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cluster not found"));
    }
}
```

### 6. UPDL Node System Architecture

**Purpose**: Universal abstraction layer for multi-platform 3D/AR/VR content

**Core Node Types**:
1. **Space** - Root container for scenes
2. **Entity** - 3D objects with transform, materials
3. **Component** - Reusable behaviors (physics, animation)
4. **Event** - User interactions and triggers
5. **Action** - Responses to events
6. **Data** - State management and variables
7. **Universo** - Integration nodes for Universo-specific features

**Export System**:
- Template-first architecture
- Exporters for AR.js, PlayCanvas, Babylon.js, Three.js, A-Frame
- Reusable templates (quiz, MMOOMM)

**Java Translation Considerations**:
- UPDL is language-agnostic (JSON/YAML format)
- Java implementation should focus on:
  - UPDL schema validation
  - Template engine for exports
  - Backend services for UPDL storage/versioning
  - Integration with Java 3D libraries (jMonkeyEngine, LibGDX)

### 7. Authentication Pattern

**React Implementation**:
- Passport.js with custom Supabase strategy
- JWT tokens stored in HTTP-only cookies
- Session-based authentication on backend
- Token refresh mechanism

**Components**:
```typescript
// Backend: Passport strategy
passport.use(new SupabaseStrategy({
    supabaseUrl: process.env.SUPABASE_URL,
    supabaseKey: process.env.SUPABASE_ANON_KEY
}, async (profile, done) => {
    // Find or create user
    return done(null, user)
}))

// Middleware
export const ensureAuth = (req, res, next) => {
    if (!req.isAuthenticated()) {
        return res.status(401).json({ error: 'Unauthorized' })
    }
    next()
}

// Frontend: Auth context
const { user, login, logout } = useAuth()
```

**Java Translation**:
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtConverter())
                )
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            );
        return http.build();
    }
    
    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = supabaseUrl + "/auth/v1/jwks";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
```

### 8. Testing Strategy Pattern

**React Approach**:
- **Unit Tests**: Vitest with happy-dom (4-9x faster than jsdom)
- **Integration Tests**: Testing Library for components
- **E2E Tests**: Cypress for critical workflows

**Key Patterns**:
```typescript
// Fast unit tests with happy-dom
import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'

describe('ClusterList', () => {
    it('renders cluster list', () => {
        render(<ClusterList />)
        expect(screen.getByText('Clusters')).toBeInTheDocument()
    })
})

// Integration test with mocked API
vi.mock('@/api/clusters', () => ({
    useClusterList: () => ({
        data: mockClusters,
        isLoading: false
    })
}))
```

**Java Translation**:
```java
@SpringBootTest
@AutoConfigureMockMvc
public class ClusterControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ClusterService clusterService;
    
    @Test
    @WithMockUser
    void shouldGetClusterList() throws Exception {
        when(clusterService.findAll(any()))
            .thenReturn(Page.of(mockClusters));
        
        mockMvc.perform(get("/api/clusters"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
    }
}

// Vaadin TestBench for UI tests
@SpringBootTest
public class ClusterListViewTest extends TestBenchTestCase {
    
    @Test
    void shouldDisplayClusterList() {
        $(ClusterListView.class).first();
        GridElement grid = $(GridElement.class).first();
        
        assertThat(grid.getRowCount()).isGreaterThan(0);
    }
}
```

## Feature Analysis

### Implemented Features in React Repository

#### Core Infrastructure ✅
- [x] Authentication with Supabase + Passport.js
- [x] User profiles and workspace management
- [x] RLS-based multi-tenancy
- [x] Centralized i18n system
- [x] API client abstraction
- [x] Shared type definitions

#### Primary Features ✅
1. **Clusters** (Organizations) ✅
   - Three-tier: Clusters → Domains → Resources
   - Member management with roles
   - Full CRUD operations

2. **Metaverses** (Virtual Worlds) ✅
   - Three-tier: Metaverses → Sections → Entities
   - 3D scene management
   - Export capabilities

3. **Uniks** (Workflows/Projects) ✅
   - Three-tier: Uniks → Spaces → Agents
   - Visual programming interface
   - Canvas-based editors

4. **Projects** (Version Control) ✅
   - Project → Versions → Assets
   - Version tracking
   - Asset management

#### Specialized Features ✅
- [x] **UPDL Node System** - Universal scene description
- [x] **Space Builder** - Visual 3D editor
- [x] **Publish System** - Multi-platform export
- [x] **Analytics** - Usage tracking
- [x] **Multiplayer** - Colyseus integration

### Features Not Yet in Java Implementation

#### Missing Core Features
- [ ] Clusters feature package
- [ ] Metaverses feature package
- [ ] Uniks feature package
- [ ] Projects feature package
- [ ] Space Builder
- [ ] UPDL system
- [ ] Publish/Export system
- [ ] Analytics integration
- [ ] Multiplayer infrastructure

#### Missing Patterns
- [ ] RLS integration pattern documentation
- [ ] Universal list pattern implementation
- [ ] Three-tier entity relationship guidelines
- [ ] Export template system
- [ ] Member management pattern

## Architectural Recommendations

### Immediate Priorities (Phase 1)

1. **Complete Pattern Documentation**
   - Add RLS integration pattern to constitution
   - Document three-tier entity relationship pattern
   - Create universal list pattern guide
   - Update pattern translation guide with all React patterns

2. **Implement First Feature (Clusters)**
   - Reference implementation of three-tier pattern
   - Member management with roles
   - RLS integration
   - Bilingual UI

3. **Establish Testing Standards**
   - Unit test coverage targets (70%+)
   - Integration test patterns
   - UI test strategy with Vaadin TestBench

### Medium-Term Priorities (Phase 2)

1. **Core Feature Implementation**
   - Metaverses (following Clusters pattern)
   - Uniks (workflow management)
   - Projects (version control)

2. **Specialized Systems**
   - UPDL schema and validation
   - Export template engine
   - Space Builder foundation

3. **Infrastructure Improvements**
   - Centralized logging
   - Performance monitoring
   - CI/CD pipelines

### Long-Term Priorities (Phase 3)

1. **Advanced Features**
   - Visual 3D editor (Space Builder equivalent)
   - Real-time collaboration
   - Multiplayer infrastructure

2. **Platform Maturity**
   - Advanced analytics
   - Admin dashboards
   - API documentation portal

## Pattern Gaps and Solutions

### Gap 1: RLS Pattern Not Documented

**Problem**: Constitution mentions database abstraction but doesn't specify RLS pattern

**Solution**: Add new constitutional principle for RLS integration
- Middleware pattern for JWT context
- QueryRunner per request pattern
- PostgreSQL session variable usage
- Migration-based policy management

### Gap 2: Three-Tier Pattern Not Standardized

**Problem**: Pattern exists in React but not formally documented for Java

**Solution**: Create comprehensive pattern guide
- Entity relationship diagrams
- Java/JPA implementation examples
- UI component patterns (Vaadin)
- Service layer patterns

### Gap 3: Universal List Pattern Missing

**Problem**: Pagination/search/sort not standardized

**Solution**: Implement reusable pattern
- Spring Data Specification pattern
- Vaadin lazy loading DataProvider
- Generic DTO and service methods
- Frontend search/filter components

### Gap 4: Export System Not Planned

**Problem**: UPDL and export templates not in roadmap

**Solution**: Add to Phase 2/3 planning
- UPDL schema validation (JSON Schema)
- Template engine (Thymeleaf or custom)
- Export service architecture
- Integration with Java 3D libraries

### Gap 5: Multiplayer Not Addressed

**Problem**: Real-time collaboration not in constitution

**Solution**: Add to future phases
- WebSocket infrastructure (Spring WebSocket)
- State synchronization patterns
- Conflict resolution strategies
- Horizontal scaling considerations

## Memory Bank Patterns to Adopt

### System Patterns to Document

From `systemPatterns.md`:

1. **Source-Only Package PeerDependencies** → **Java**: Maven dependency scopes
2. **RLS Integration Pattern** → **Java**: Security filter + connection management
3. **i18n Architecture** → **Java**: ResourceBundle + MessageSource
4. **Testing Environment** → **Java**: Spring Test + TestBench configuration
5. **Universal List Pattern** → **Java**: Specification + Pageable pattern
6. **React StrictMode** → **Java**: N/A (development mode configuration)
7. **TypeORM Repository** → **Java**: Spring Data JPA repositories

### Product Context Insights

From `productContext.md`:

1. **UPDL Philosophy**
   - Platform-agnostic content description
   - Multi-engine export capability
   - Template-first approach

2. **Development Phases**
   - Phase 1: Foundation (monorepo, basic features)
   - Phase 2: Expansion (advanced features, multiple technologies)
   - Phase 3: Integration (real-world systems, automation)

3. **Use Cases**
   - Educational AR applications
   - Product demonstrations
   - Architectural visualization
   - Virtual world prototyping
   - Multiplayer interactions

## Implementation Roadmap

### Phase 1: Foundation (Current)
- [x] Repository structure (COMPLETE)
- [x] Basic constitution (COMPLETE)
- [x] Pattern translation guide (COMPLETE)
- [ ] RLS pattern documentation
- [ ] Three-tier pattern guide
- [ ] First feature: Clusters

### Phase 2: Core Features
- [ ] Metaverses implementation
- [ ] Uniks implementation
- [ ] Projects implementation
- [ ] Space Builder foundation
- [ ] Analytics integration

### Phase 3: Advanced Systems
- [ ] UPDL schema and validation
- [ ] Export template engine
- [ ] Real-time collaboration
- [ ] Multiplayer infrastructure

## Conclusion

The Universo Platformo React repository provides a mature, well-architected reference implementation with:

- **32+ packages** demonstrating modular architecture
- **Proven patterns** for three-tier entities, RLS, i18n, pagination
- **Comprehensive features** including UPDL, export system, multiplayer
- **Production-ready** patterns validated in real-world use

The Java implementation should:

1. **Adopt core patterns** (three-tier entities, RLS, universal lists)
2. **Translate idiomatically** (use Java/Spring conventions, not direct ports)
3. **Prioritize features** (Clusters → Metaverses → Uniks → Projects)
4. **Document thoroughly** (patterns, decisions, architecture)
5. **Test comprehensively** (70%+ unit, integration, UI tests)

This analysis provides the foundation for updating the Java project constitution, planning documents, and implementation roadmap.

## References

- React Repository: https://github.com/teknokomo/universo-platformo-react
- Constitution: `.specify/memory/constitution.md`
- Pattern Guide: `.specify/memory/react-to-java-patterns.md`
- Implementation Plans: `specs/001-initial-project-setup/`
