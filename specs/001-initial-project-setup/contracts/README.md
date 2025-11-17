# API Contracts: Standard REST Endpoints

This directory contains example API contracts for Universo Platformo Java features. These contracts demonstrate the standard REST API patterns that all features should follow.

## Contract Files

- `clusters-api.yaml` - Example OpenAPI specification for the Clusters feature (standard 3-tier pattern)
- `README.md` - This file

## API Design Principles

### RESTful Conventions

All Universo Platformo APIs follow REST principles:

1. **Resource-based URLs**: Use nouns, not verbs (`/api/clusters`, not `/api/getClusters`)
2. **HTTP methods**: GET (read), POST (create), PUT (update), DELETE (delete)
3. **Status codes**: 200 (success), 201 (created), 204 (no content), 400 (bad request), 404 (not found), 500 (server error)
4. **JSON payloads**: Request and response bodies use JSON format
5. **Pagination**: Use query parameters `page` and `size` for list endpoints
6. **Filtering**: Use query parameters for filtering (e.g., `?status=active`)
7. **Sorting**: Use query parameter `sort` (e.g., `?sort=createdAt,desc`)

### URL Structure

```
/api/{feature}/{resource}
/api/{feature}/{resource}/{id}
/api/{feature}/{resource}/{id}/{sub-resource}
/api/{feature}/{resource}/{id}/{sub-resource}/{sub-id}
```

Examples:
- `/api/clusters` - List all clusters
- `/api/clusters/{clusterId}` - Get specific cluster
- `/api/clusters/{clusterId}/domains` - List domains in cluster
- `/api/clusters/{clusterId}/domains/{domainId}` - Get specific domain
- `/api/clusters/{clusterId}/domains/{domainId}/resources` - List resources in domain

### Authentication

All API endpoints (except public endpoints) require authentication:

```
Authorization: Bearer {jwt_token}
```

JWT token obtained from Supabase Auth and validated by Spring Security.

### Error Response Format

Standard error response:

```json
{
  "timestamp": "2025-11-17T06:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Cluster not found: 123e4567-e89b-12d3-a456-426614174000",
  "path": "/api/clusters/123e4567-e89b-12d3-a456-426614174000",
  "errorCode": "RESOURCE_NOT_FOUND"
}
```

Validation error response:

```json
{
  "timestamp": "2025-11-17T06:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for object='clusterRequest'. Error count: 2",
  "path": "/api/clusters",
  "errorCode": "VALIDATION_ERROR",
  "fieldErrors": [
    {
      "field": "name",
      "message": "Name is required",
      "rejectedValue": null
    },
    {
      "field": "description",
      "message": "Description exceeds maximum length of 5000 characters",
      "rejectedValue": "Very long description..."
    }
  ]
}
```

### Pagination Response Format

List endpoints return paginated responses:

```json
{
  "content": [
    { "id": "...", "name": "...", ... },
    { "id": "...", "name": "...", ... }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": { "sorted": true, "orders": [...] }
  },
  "totalPages": 5,
  "totalElements": 100,
  "last": false,
  "first": true,
  "numberOfElements": 20,
  "size": 20,
  "number": 0,
  "empty": false
}
```

### Internationalization

API responses can be localized based on `Accept-Language` header:

```
Accept-Language: en-US
Accept-Language: ru-RU
```

Error messages and validation messages will be returned in the requested language.

## Contract Generation

For each feature, generate an OpenAPI 3.0 specification following the pattern in `clusters-api.yaml`.

OpenAPI spec should include:
- All endpoints (CRUD operations for each tier)
- Request/response schemas with validation rules
- Authentication requirements
- Error responses
- Example requests and responses

## Validation with OpenAPI Generator

Contracts can be validated and used to generate client SDKs:

```bash
# Validate OpenAPI spec
openapi-generator validate -i contracts/clusters-api.yaml

# Generate Java client
openapi-generator generate -i contracts/clusters-api.yaml \
  -g java \
  -o generated/java-client

# Generate TypeScript client (for future JavaScript clients)
openapi-generator generate -i contracts/clusters-api.yaml \
  -g typescript-fetch \
  -o generated/ts-client
```

## See Also

- OpenAPI Specification: https://swagger.io/specification/
- Spring Boot OpenAPI integration: https://springdoc.org/
- API Design Best Practices: https://docs.microsoft.com/en-us/azure/architecture/best-practices/api-design
