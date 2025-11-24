# Data Model: Initial Universo Platformo Java Project Setup

**Feature**: 001-initial-project-setup  
**Date**: 2025-11-17  
**Phase**: Phase 1 - Data Model & Entity Design

## Overview

This feature is a foundational setup and does not introduce runtime entities. However, it establishes the **standard entity relationship patterns** that future features must follow. This document defines those patterns and provides example entity structures.

## Standard Entity Relationship Patterns

### Pattern 1: Three-Tier Hierarchy (Primary → Secondary → Tertiary)

This is the **default pattern** for most Universo Platformo features. It follows a hierarchical relationship structure:

```
┌─────────────┐
│  PRIMARY    │  (e.g., Cluster)
│  Entity     │  • Top-level organizational unit
└─────────────┘  • Owned by user or organization
      │          • Has metadata, timestamps
      │ 1:N
      ▼
┌─────────────┐
│ SECONDARY   │  (e.g., Domain)
│  Entity     │  • Belongs to one Primary entity
└─────────────┘  • Groups Tertiary entities
      │          • Has metadata, timestamps
      │ 1:N
      ▼
┌─────────────┐
│  TERTIARY   │  (e.g., Resource)
│  Entity     │  • Belongs to one Secondary entity
└─────────────┘  • Actual content/data items
                 • Has metadata, timestamps
```

**Characteristics**:
- **One-to-Many relationships** down the hierarchy
- Each tier has **complete CRUD operations**
- Each tier has **dedicated UI views** (list, detail, create, edit, delete)
- Each tier has **separate repository/service layers**
- **Cascading deletes**: Deleting Primary deletes all Secondary; deleting Secondary deletes all Tertiary
- **Soft deletes** recommended for Primary and Secondary (mark as deleted, preserve data)

### Example Implementation: Clusters Feature

**Primary Entity**: `Cluster`
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
    private UUID ownerId;  // User who owns this cluster
    
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;  // Soft delete
    
    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Domain> domains = new ArrayList<>();
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

**Secondary Entity**: `Domain`
```java
@Entity
@Table(name = "domains")
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id", nullable = false)
    private Cluster cluster;
    
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;  // Soft delete
    
    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resource> resources = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

**Tertiary Entity**: `Resource`
```java
@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ResourceType type;  // e.g., API, DATABASE, SERVICE
    
    @Column(columnDefinition = "TEXT")
    private String url;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;
    
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;
    
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> configuration;  // Resource-specific config
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

enum ResourceType {
    API, DATABASE, SERVICE, EXTERNAL_TOOL, OTHER
}
```

### Pattern 2: Extended Hierarchy (4+ Tiers)

Some features require deeper nesting. Example: **Uniks Feature**

```
Primary (Unik) → Secondary (Section) → Tertiary (Component) → Quaternary (Element)
```

**When to use**: Features with naturally deep hierarchies (file systems, complex taxonomies, nested organizations)

**Guidelines**:
- Keep hierarchy depth ≤ 5 levels (performance and UX concerns)
- Each level must have clear semantic meaning
- Consider flattening if hierarchy feels artificial

### Pattern 3: Simplified Hierarchy (1-2 Tiers)

Some features need fewer tiers. Examples:
- **User Profiles**: Single entity with no children
- **Settings**: Single entity per user
- **Notifications**: Flat list of notifications

**When to use**: Simple CRUD features with no natural hierarchy

**Guidelines**:
- Must be explicitly justified in feature specification
- Still follow repository/service layer patterns
- Still have complete CRUD operations

### Pattern 4: Many-to-Many Relationships

When entities need many-to-many relationships, use **junction tables**:

```java
@Entity
@Table(name = "resource_tags")
public class ResourceTag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Unique constraint on (resource_id, tag_id)
}

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    
    @Column(length = 7)
    private String color;  // Hex color code
}
```

**Guidelines**:
- Explicit junction table entity (don't use `@ManyToMany` directly for better control)
- Unique constraint on foreign key pair
- Junction table can have additional metadata (created_at, created_by, etc.)

## Common Entity Attributes

All entities SHOULD include these standard attributes:

```java
// Primary key (UUID recommended for distributed systems)
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;

// Audit timestamps
@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt;

@Column(name = "updated_at")
private LocalDateTime updatedAt;

// Soft delete support (optional but recommended for important entities)
@Column(name = "deleted_at")
private LocalDateTime deletedAt;

// Flexible metadata (for entity-specific properties without schema changes)
@Type(JsonBinaryType.class)
@Column(columnDefinition = "jsonb")
private Map<String, Object> metadata;

// Lifecycle hooks
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
}

@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();
}
```

## Validation Rules

Standard validation annotations for entities:

```java
// Name fields: required, max length 255
@NotBlank(message = "{entity.name.required}")
@Size(max = 255, message = "{entity.name.maxlength}")
private String name;

// Description fields: optional, max length 5000
@Size(max = 5000, message = "{entity.description.maxlength}")
private String description;

// Email fields
@Email(message = "{entity.email.invalid}")
@NotBlank(message = "{entity.email.required}")
private String email;

// URL fields
@URL(message = "{entity.url.invalid}")
private String url;

// Enum fields
@NotNull(message = "{entity.type.required}")
@Enumerated(EnumType.STRING)
private EntityType type;
```

## Repository Pattern

Each entity MUST have a repository interface:

```java
@Repository
public interface ClusterRepository extends JpaRepository<Cluster, UUID> {
    
    // Query methods by method naming convention
    List<Cluster> findByOwnerId(UUID ownerId);
    
    Optional<Cluster> findByIdAndOwnerId(UUID id, UUID ownerId);
    
    List<Cluster> findByOwnerIdAndDeletedAtIsNull(UUID ownerId);
    
    // Custom JPQL query
    @Query("SELECT c FROM Cluster c WHERE c.ownerId = :ownerId " +
           "AND c.deletedAt IS NULL ORDER BY c.createdAt DESC")
    List<Cluster> findActiveClustersForUser(@Param("ownerId") UUID ownerId);
    
    // Native SQL query (use sparingly, marks database-specific code)
    @Query(value = "SELECT * FROM clusters WHERE metadata->>'priority' = :priority", 
           nativeQuery = true)
    List<Cluster> findByMetadataPriority(@Param("priority") String priority);
}
```

## Service Pattern

Each entity MUST have a service layer:

```java
@Service
@Transactional
public class ClusterService {
    
    private final ClusterRepository clusterRepository;
    private final DomainRepository domainRepository;
    
    public ClusterService(ClusterRepository clusterRepository, 
                         DomainRepository domainRepository) {
        this.clusterRepository = clusterRepository;
        this.domainRepository = domainRepository;
    }
    
    public Cluster create(Cluster cluster) {
        // Business logic, validation
        return clusterRepository.save(cluster);
    }
    
    public Cluster update(UUID id, Cluster updates) {
        Cluster existing = clusterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cluster", id));
        
        // Update fields
        existing.setName(updates.getName());
        existing.setDescription(updates.getDescription());
        // ... other fields
        
        return clusterRepository.save(existing);
    }
    
    public void delete(UUID id) {
        // Soft delete
        Cluster cluster = clusterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cluster", id));
        
        cluster.setDeletedAt(LocalDateTime.now());
        clusterRepository.save(cluster);
    }
    
    public void hardDelete(UUID id) {
        // Hard delete (permanent removal)
        clusterRepository.deleteById(id);
    }
    
    public Page<Cluster> findAll(Pageable pageable) {
        return clusterRepository.findAll(pageable);
    }
    
    public List<Cluster> findByOwner(UUID ownerId) {
        return clusterRepository.findByOwnerIdAndDeletedAtIsNull(ownerId);
    }
}
```

## Database Schema Conventions

### Table Naming
- **Plural lowercase**: `clusters`, `domains`, `resources`
- **Junction tables**: `{entity1}_{entity2}` (e.g., `resource_tags`)

### Column Naming
- **Snake case**: `created_at`, `owner_id`, `deleted_at`
- **Foreign keys**: `{referenced_table}_id` (e.g., `cluster_id`, `domain_id`)

### Indexes
- **Primary key**: Automatic index on `id`
- **Foreign keys**: Index on all foreign key columns
- **Frequent queries**: Index on commonly queried fields (e.g., `owner_id`, `created_at`)
- **Composite indexes**: For multi-column queries (e.g., `owner_id, deleted_at`)

### Constraints
- **NOT NULL**: Required fields
- **UNIQUE**: Natural keys (e.g., email, username within tenant)
- **FOREIGN KEY**: Referential integrity with CASCADE rules
- **CHECK**: Domain validation (e.g., `CHECK (length(name) > 0)`)

## Migration Strategy

Use Flyway for database migrations:

```
src/main/resources/db/migration/
├── V001__create_clusters_table.sql
├── V002__create_domains_table.sql
├── V003__create_resources_table.sql
├── V004__add_metadata_to_clusters.sql
└── V005__create_resource_tags_junction.sql
```

**Migration Guidelines**:
- **Versioned**: `V{number}__{description}.sql`
- **Immutable**: Never modify existing migrations
- **Idempotent**: Use `IF NOT EXISTS` where supported
- **Backward compatible**: Avoid breaking schema changes in production

## Entity Pattern Selection Checklist

When designing a new feature, answer these questions:

1. **How many entity tiers does this feature need?**
   - 1 tier: Simplified pattern (justify in spec)
   - 2 tiers: Simplified pattern (justify in spec)
   - 3 tiers: Standard pattern (default, no justification needed)
   - 4+ tiers: Extended pattern (justify in spec)

2. **What are the relationships?**
   - Hierarchical (parent-child): Use one-to-many
   - Associative (tags, categories): Use many-to-many with junction table
   - Reference (lookup): Use many-to-one

3. **Does it need soft deletes?**
   - User-facing content: Yes
   - System/config data: Maybe
   - Transient data: No

4. **Does it need flexible metadata?**
   - Feature still evolving: Yes (use JSONB column)
   - Stable schema: No (use explicit columns)

## Example Features with Pattern Selection

| Feature | Pattern | Tiers | Justification |
|---------|---------|-------|---------------|
| **Clusters** | Standard 3-tier | Cluster → Domain → Resource | Core organizational structure |
| **Metaverses** | Standard 3-tier | Metaverse → Section → Entity | Similar to Clusters, different domain |
| **Uniks** | Extended 4-tier | Unik → Category → Item → Attribute | Complex nested structure |
| **User Profiles** | Simplified 1-tier | Profile (single entity) | No natural hierarchy |
| **Spaces/Canvases** | Extended + Graph | Space → Canvas → Node (+ Node connections) | Graph structure with hierarchy |
| **Notifications** | Simplified 1-tier | Notification (single entity) | Flat list per user |

## Conclusion

All Universo Platformo Java features must follow one of these documented entity patterns. The standard three-tier hierarchy (Primary → Secondary → Tertiary) is the default and should be used unless explicitly justified in the feature specification.

Each entity must have:
- JPA annotations for persistence
- Standard audit fields (id, created_at, updated_at)
- Validation annotations
- Repository interface (Spring Data JPA)
- Service class with business logic
- Corresponding Vaadin views (list, detail, create, edit)
- Corresponding REST API endpoints (if needed for API access)

Ready to proceed with contract generation.
