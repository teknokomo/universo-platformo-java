# Feature Implementation Roadmap

**Last Updated**: 2025-11-17  
**Based On**: React repository analysis (https://github.com/teknokomo/universo-platformo-react)  
**Purpose**: Priority-ordered roadmap for porting React features to Java implementation

## Feature Analysis Summary

### Implemented in React (32+ packages)

#### Core Infrastructure ✅
- [x] Authentication (Passport.js + Supabase JWT)
- [x] User Profiles and Workspaces
- [x] RLS-based multi-tenancy
- [x] Centralized i18n system
- [x] API client abstraction
- [x] Shared type definitions
- [x] Reusable UI components (MUI templates)

#### Primary Features ✅
1. **Clusters** - Organization management (3-tier: Clusters/Domains/Resources)
2. **Metaverses** - Virtual world creation (3-tier: Metaverses/Sections/Entities)
3. **Uniks** - Workflow/project management (3-tier: Uniks/Spaces/Agents)
4. **Projects** - Version control (3-tier: Projects/Versions/Assets)

#### Specialized Features ✅
- [x] UPDL Node System - Universal scene description language
- [x] Space Builder - Visual 3D editor
- [x] Publish System - Multi-platform export (AR.js, PlayCanvas, etc.)
- [x] Analytics - Usage tracking and metrics
- [x] Multiplayer - Colyseus real-time infrastructure

### Status in Java Repository

#### Implemented ✅
- [x] Repository structure (monorepo with Maven)
- [x] Constitution and governance documents
- [x] Pattern translation guides
- [x] Core package structure (core-frt, core-srv)
- [x] Basic Vaadin application skeleton
- [x] Basic Spring Boot backend skeleton

#### Not Yet Implemented ❌
- [ ] All primary features (Clusters, Metaverses, Uniks, Projects)
- [ ] Authentication with Supabase
- [ ] RLS integration
- [ ] Member management pattern
- [ ] Universal pagination pattern
- [ ] UPDL system
- [ ] Export/publish functionality
- [ ] Space Builder
- [ ] Analytics
- [ ] Multiplayer infrastructure

## Implementation Priorities

### Phase 1: Foundation (Current - Months 1-3)

**Goal**: Establish core infrastructure and first working feature

#### 1.1 Authentication & Authorization (P0 - Critical)
**React Reference**: `packages/auth-frt`, `packages/auth-srv`

**Scope**:
- [ ] Spring Security configuration with JWT
- [ ] Supabase JWT validation
- [ ] User session management
- [ ] Login/logout UI (Vaadin)
- [ ] Password reset flow
- [ ] Protected routes

**Deliverables**:
- Working authentication system
- User profile storage
- Session management
- Bilingual UI (EN/RU)

**Estimated Effort**: 2-3 weeks

#### 1.2 First Feature: Clusters (P0 - Critical)
**React Reference**: `packages/clusters-frt`, `packages/clusters-srv`

**Why First**: 
- Demonstrates three-tier pattern
- Includes member management
- Reference implementation for other features
- Relatively simple business logic

**Scope**:
- [ ] Three entities (Cluster, Domain, Resource)
- [ ] Member management with roles (owner/admin/member)
- [ ] CRUD operations for all tiers
- [ ] List views with pagination, search, sort
- [ ] Detail views with tabs
- [ ] RLS integration (optional for Phase 1)
- [ ] Bilingual UI

**Deliverables**:
- Complete Clusters feature package
- Reusable pagination pattern
- Reusable member management components
- Integration and unit tests (70%+ coverage)
- Documentation (EN/RU)

**Estimated Effort**: 4-6 weeks

#### 1.3 Profile Management (P1 - High)
**React Reference**: `packages/profile-frt`, `packages/profile-srv`

**Scope**:
- [ ] User profile CRUD
- [ ] Avatar upload
- [ ] Workspace management
- [ ] Preferences and settings
- [ ] Activity history

**Deliverables**:
- Profile management UI
- Workspace switcher
- User preferences storage

**Estimated Effort**: 1-2 weeks

**Phase 1 Total Estimated Effort**: 7-11 weeks

### Phase 2: Core Features (Months 4-6)

**Goal**: Implement remaining primary features using Clusters pattern

#### 2.1 Metaverses (P1 - High)
**React Reference**: `packages/metaverses-frt`, `packages/metaverses-srv`

**Pattern**: Three-tier (Metaverses/Sections/Entities)

**Scope**:
- [ ] Metaverse creation and management
- [ ] Section organization
- [ ] Entity placement (3D objects)
- [ ] Basic scene configuration
- [ ] Member management (reuse from Clusters)

**Deliverables**:
- Complete Metaverses feature
- Scene structure foundation
- Integration with future UPDL system

**Estimated Effort**: 3-4 weeks

#### 2.2 Uniks (Workflows) (P1 - High)
**React Reference**: `packages/uniks-frt`, `packages/uniks-srv`

**Pattern**: Three-tier (Uniks/Spaces/Agents) with Canvas sub-feature

**Scope**:
- [ ] Workflow/project creation
- [ ] Space organization (workspaces)
- [ ] Agent management
- [ ] Canvas framework (for future visual programming)
- [ ] Member collaboration

**Deliverables**:
- Complete Uniks feature
- Foundation for visual programming
- Workflow execution framework

**Estimated Effort**: 4-5 weeks

#### 2.3 Projects (Version Control) (P1 - High)
**React Reference**: `packages/projects-frt`, `packages/projects-srv`

**Pattern**: Three-tier (Projects/Versions/Assets)

**Scope**:
- [ ] Project creation and management
- [ ] Version tracking and history
- [ ] Asset upload and storage
- [ ] Branch/merge concepts (simplified Git)
- [ ] Collaboration and permissions

**Deliverables**:
- Complete Projects feature
- Version control system
- Asset management system

**Estimated Effort**: 3-4 weeks

#### 2.4 RLS Integration (P1 - High)
**React Reference**: `packages/auth-srv` (RLS utilities)

**Scope**:
- [ ] Spring Security filter for JWT context
- [ ] JPA transaction callbacks for PostgreSQL session vars
- [ ] RLS policies in database migrations
- [ ] Integration with all existing features
- [ ] Performance testing

**Deliverables**:
- Database-enforced security
- RLS pattern documentation
- Migration guide for existing data

**Estimated Effort**: 2-3 weeks

**Phase 2 Total Estimated Effort**: 12-16 weeks

### Phase 3: Specialized Features (Months 7-12)

**Goal**: Advanced functionality and unique platform features

#### 3.1 UPDL Schema and Validation (P2 - Medium)
**React Reference**: `packages/updl`

**Scope**:
- [ ] UPDL JSON schema definition
- [ ] Schema validation service
- [ ] Node type definitions (Space, Entity, Component, etc.)
- [ ] UPDL document storage and versioning
- [ ] REST API for UPDL operations

**Deliverables**:
- UPDL schema validator
- UPDL storage service
- API for UPDL manipulation

**Estimated Effort**: 3-4 weeks

#### 3.2 Space Builder Foundation (P2 - Medium)
**React Reference**: `packages/space-builder-frt`, `packages/space-builder-srv`

**Scope**:
- [ ] 3D scene editor UI (Vaadin + Java 3D library)
- [ ] Object placement and manipulation
- [ ] Property inspector
- [ ] Scene hierarchy tree
- [ ] UPDL export from editor

**Deliverables**:
- Basic 3D visual editor
- Scene editing capabilities
- UPDL integration

**Estimated Effort**: 6-8 weeks

#### 3.3 Export System (P2 - Medium)
**React Reference**: `packages/publish-frt`, `packages/publish-srv`

**Scope**:
- [ ] Template engine for exports
- [ ] AR.js exporter (web-based AR)
- [ ] Basic HTML/CSS export
- [ ] Export preview functionality
- [ ] Publication URL structure

**Deliverables**:
- Working export system
- At least 2 export targets
- Template management

**Estimated Effort**: 4-6 weeks

#### 3.4 Analytics (P2 - Medium)
**React Reference**: `packages/analytics-frt`

**Scope**:
- [ ] Event tracking infrastructure
- [ ] Usage analytics dashboard
- [ ] User behavior tracking
- [ ] Performance metrics
- [ ] Export analytics reports

**Deliverables**:
- Analytics collection system
- Dashboard UI
- Reporting tools

**Estimated Effort**: 2-3 weeks

#### 3.5 Real-Time Collaboration (P3 - Low)
**React Reference**: `packages/multiplayer-colyseus-srv`

**Scope**:
- [ ] WebSocket infrastructure (Spring WebSocket)
- [ ] Real-time synchronization
- [ ] Presence indicators
- [ ] Collaborative editing
- [ ] Conflict resolution

**Deliverables**:
- WebSocket server
- Real-time sync system
- Collaborative features

**Estimated Effort**: 6-8 weeks

**Phase 3 Total Estimated Effort**: 21-29 weeks

## Feature Comparison Matrix

| Feature | React Status | Java Priority | Complexity | Dependencies |
|---------|--------------|---------------|------------|--------------|
| **Authentication** | ✅ Complete | P0 Critical | Medium | None |
| **Clusters** | ✅ Complete | P0 Critical | Medium | Auth |
| **Profile** | ✅ Complete | P1 High | Low | Auth |
| **Metaverses** | ✅ Complete | P1 High | High | Auth, Clusters pattern |
| **Uniks** | ✅ Complete | P1 High | High | Auth, Clusters pattern |
| **Projects** | ✅ Complete | P1 High | Medium | Auth, Clusters pattern |
| **RLS Integration** | ✅ Complete | P1 High | High | Auth, All features |
| **UPDL System** | ✅ Complete | P2 Medium | High | Projects |
| **Space Builder** | ✅ Complete | P2 Medium | Very High | UPDL, Metaverses |
| **Export System** | ✅ Complete | P2 Medium | High | UPDL, Space Builder |
| **Analytics** | ✅ Complete | P2 Medium | Low | All features |
| **Multiplayer** | ✅ Complete | P3 Low | Very High | All features |

## Decision Criteria

### Priority Levels

**P0 (Critical)**: Required for MVP, blocks other features
- Authentication (needed by all features)
- Clusters (reference implementation)

**P1 (High)**: Core platform features, needed for full functionality
- Profile (user management)
- Metaverses, Uniks, Projects (primary features)
- RLS (security requirement)

**P2 (Medium)**: Advanced features, differentiators
- UPDL (unique platform capability)
- Space Builder (visual editing)
- Export System (multi-platform support)
- Analytics (insights and optimization)

**P3 (Low)**: Nice-to-have, can be deferred
- Real-time collaboration (complex, can use async collaboration initially)

### Feature Selection Criteria

1. **Dependency Tree**: Features required by other features get higher priority
2. **Pattern Demonstration**: Features that establish reusable patterns (like Clusters) are critical
3. **Complexity vs Value**: Balance implementation effort with user value
4. **React Completeness**: Prioritize features fully implemented in React reference
5. **Platform Uniqueness**: UPDL and export systems are unique differentiators

## Implementation Approach

### For Each Feature

1. **Analysis Phase**
   - Study React implementation thoroughly
   - Identify entity relationships and patterns
   - Document business logic and rules
   - Create specification document

2. **Design Phase**
   - Design Java/JPA entities
   - Design Spring REST API
   - Design Vaadin UI components
   - Create data model diagram
   - Write API contracts (OpenAPI)

3. **Implementation Phase**
   - Backend: Entities → Repositories → Services → Controllers
   - Frontend: Views → Components → Forms → Validation
   - Testing: Unit → Integration → UI tests
   - Documentation: README (EN/RU) → API docs

4. **Validation Phase**
   - Test against requirements
   - Code review
   - Security review
   - Performance testing
   - Documentation review

### Code Reuse Strategy

- **Patterns**: Establish in Clusters, reuse in all features
- **Components**: Generic list, form, modal components
- **Services**: Generic pagination, search, member management
- **Security**: RLS filter, permission evaluators
- **Testing**: Test utilities and mocks

## Monitoring Strategy

### React Repository Tracking

**Monthly Reviews**:
- Check for new features or packages
- Analyze architectural changes
- Review system pattern updates
- Identify bugs fixed (may affect Java version)

**Quarterly Deep Dives**:
- Compare feature completeness
- Evaluate new patterns
- Update translation guide
- Update this roadmap

**Triggers for Immediate Review**:
- Major version releases in React repo
- Security advisories
- Breaking architectural changes
- New constitutional principles

### Success Metrics

**Phase 1 Success Criteria**:
- Authentication fully functional
- Clusters feature complete with 70%+ test coverage
- Pattern documentation complete
- First production deployment

**Phase 2 Success Criteria**:
- All primary features (Clusters, Metaverses, Uniks, Projects) complete
- RLS fully integrated
- Test coverage 70%+ across all features
- Documentation complete (EN/RU)

**Phase 3 Success Criteria**:
- UPDL system operational
- At least 2 export targets working
- Space Builder MVP complete
- Real-time collaboration (optional stretch goal)

## Risk Assessment

### High Risk Items

1. **RLS Integration Complexity** (Phase 2)
   - **Risk**: Java's connection pooling makes per-request session vars difficult
   - **Mitigation**: Use dedicated connection per request, thorough testing, consider Supabase PostgREST alternative

2. **3D Editor Complexity** (Phase 3)
   - **Risk**: Vaadin + Java 3D libraries may be immature
   - **Mitigation**: Research Java 3D options early, consider web-based editor alternative

3. **UPDL Export Quality** (Phase 3)
   - **Risk**: Maintaining parity with React's multiple exporters
   - **Mitigation**: Focus on 1-2 high-value targets initially, leverage existing libraries

4. **Real-Time Collaboration** (Phase 3)
   - **Risk**: Very complex, potential performance issues
   - **Mitigation**: Use proven Spring WebSocket + STOMP, consider deferring to Phase 4

### Medium Risk Items

1. **Vaadin UI Complexity**
   - **Risk**: Less familiar than React for team
   - **Mitigation**: Training, early prototypes, leverage Vaadin documentation

2. **Pattern Translation Accuracy**
   - **Risk**: Java patterns may not map 1:1 with React
   - **Mitigation**: Iterate based on actual implementation, document deviations

3. **Test Coverage Goals**
   - **Risk**: 70% may be challenging with Vaadin UI tests
   - **Mitigation**: Focus unit tests on services, use TestBench strategically

## Conclusion

This roadmap provides a structured approach to porting Universo Platformo features from React to Java, prioritizing:

1. **Foundation First**: Authentication and reference implementation (Clusters)
2. **Core Features**: Primary three-tier features (Metaverses, Uniks, Projects)
3. **Specialization**: Unique platform features (UPDL, Space Builder, Export)
4. **Maturity**: Advanced features (Analytics, Real-time collaboration)

Estimated timeline: **12-18 months** for full feature parity with React implementation, with MVP (Phase 1) achievable in **3 months**.

Regular monitoring of the React repository ensures alignment and allows incorporation of improvements and new features as they become available in the reference implementation.
