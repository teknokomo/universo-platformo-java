# Core Frontend Package (core-frt)

User interface and frontend infrastructure for Universo Platformo.

## Overview

This package provides the core UI functionality including:
- Vaadin application foundation
- UI components and views
- Frontend routing
- Theme customization
- Client-server communication

## Structure

```
core-frt/
└── base/                   # Base implementation
    ├── src/
    │   ├── main/
    │   │   ├── java/      # Java source code
    │   │   └── resources/ # Configuration and theme files
    │   └── test/          # Test code
    └── pom.xml            # Maven configuration
```

## Dependencies

- Vaadin Spring Boot Starter
- Core Server Package (core-srv-base)
- Vaadin TestBench (for testing)

## Configuration

See `src/main/resources/application.properties` for configuration options.

Key settings:
- `vaadin.productionMode`: Enable production optimizations
- `vaadin.frontend.hotdeploy`: Enable hot deployment in development

## Running

```bash
cd packages/core-frt/base
mvn spring-boot:run
```

Access the application at `http://localhost:8080`

## Development

Vaadin will automatically compile frontend resources on first access.
In development mode, changes to Java code will be hot-reloaded.

## Testing

```bash
cd packages/core-frt/base
mvn test
```

## Building for Production

```bash
cd packages/core-frt/base
mvn clean package -Pproduction
```

This will create an optimized production build with minified frontend resources.
