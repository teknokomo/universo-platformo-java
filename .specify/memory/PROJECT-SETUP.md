# Project Setup Guide / Руководство по настройке проекта

## English Version

### Initial Setup Completed ✅

The Universo Platformo Java project has been successfully initialized with the following structure:

#### ✅ Core Components
- **Root Maven POM**: Multi-module monorepo configuration
- **Package Structure**: Organized under `packages/` directory
- **Core Server Package** (`core-srv`): Backend services with Spring Boot
- **Core Frontend Package** (`core-frt`): UI components with Vaadin

#### ✅ Project Structure
```
universo-platformo-java/
├── packages/
│   ├── core-srv/              # Backend services
│   │   ├── base/              # Base implementation
│   │   │   ├── src/main/java # Spring Boot application
│   │   │   └── pom.xml        # Package configuration
│   │   ├── README.md          # English documentation
│   │   ├── README-RU.md       # Russian documentation
│   │   └── pom.xml            # Module aggregator
│   └── core-frt/              # Frontend UI
│       ├── base/              # Base implementation
│       │   ├── src/main/java # Vaadin application
│       │   └── pom.xml        # Package configuration
│       ├── README.md          # English documentation
│       ├── README-RU.md       # Russian documentation
│       └── pom.xml            # Module aggregator
├── specs/                     # Feature specifications
├── .specify/                  # Specification tooling
├── README.md                  # Main documentation (English)
├── README-RU.md               # Main documentation (Russian)
└── pom.xml                    # Root Maven configuration
```

#### ✅ Technology Stack Configured
- **Java 17** (LTS)
- **Spring Boot 3.2.0**
- **Vaadin 24.3.0**
- **PostgreSQL Driver** (for Supabase)
- **JUnit 5** (testing)

#### ✅ Build and Test Status
```bash
# Build successful ✓
mvn clean compile

# Tests passing ✓
mvn test
```

### Next Steps

1. **Configure Database Connection**
   ```bash
   export DATABASE_URL=jdbc:postgresql://your-supabase-host:5432/your-database
   export DATABASE_USERNAME=your-username
   export DATABASE_PASSWORD=your-password
   export JWT_SECRET=your-supabase-jwt-secret
   ```

2. **Run the Applications**
   ```bash
   # Backend
   cd packages/core-srv/base
   mvn spring-boot:run
   
   # Frontend (in another terminal)
   cd packages/core-frt/base
   mvn spring-boot:run
   ```

3. **Create First Feature**
   - Follow constitution principles from `.specify/memory/constitution.md`
   - Create GitHub Issue following `.github/instructions/github-issues.md`
   - Use specification templates from `.specify/templates/`
   - Suggested first feature: "Clusters" (Clusters/Domains/Resources)

### Constitution Compliance ✅

All principles from `.specify/memory/constitution.md` are satisfied:

| Principle | Status | Implementation |
|-----------|--------|----------------|
| I. Monorepo Package Architecture | ✅ | `packages/` with `-srv`/`-frt` naming |
| II. Bilingual Documentation | ✅ | README.md + README-RU.md (identical structure) |
| III. Database Abstraction | ✅ | JPA with PostgreSQL driver, abstraction ready |
| IV. GitHub Workflow Compliance | ✅ | Instructions in `.github/instructions/` |
| V. Technology Stack Integrity | ✅ | Java 17, Vaadin, Spring Boot configured |
| VI. Specification-Driven Development | ✅ | Templates in `.specify/templates/` |

### Documentation Bilingual Verification

All documentation files have been created with matching line counts:

```
README.md       172 lines ✓
README-RU.md    172 lines ✓

core-srv/README.md    63 lines ✓
core-srv/README-RU.md 63 lines ✓

core-frt/README.md    69 lines ✓
core-frt/README-RU.md 69 lines ✓
```

---

## Русская версия

### Начальная настройка завершена ✅

Проект Universo Platformo Java успешно инициализирован со следующей структурой:

#### ✅ Основные компоненты
- **Корневой Maven POM**: Конфигурация мультимодульного монорепозитория
- **Структура пакетов**: Организована в директории `packages/`
- **Пакет Core Server** (`core-srv`): Серверные сервисы с Spring Boot
- **Пакет Core Frontend** (`core-frt`): UI компоненты с Vaadin

#### ✅ Структура проекта
```
universo-platformo-java/
├── packages/
│   ├── core-srv/              # Серверные сервисы
│   │   ├── base/              # Базовая реализация
│   │   │   ├── src/main/java # Приложение Spring Boot
│   │   │   └── pom.xml        # Конфигурация пакета
│   │   ├── README.md          # Документация на английском
│   │   ├── README-RU.md       # Документация на русском
│   │   └── pom.xml            # Агрегатор модуля
│   └── core-frt/              # Пользовательский интерфейс
│       ├── base/              # Базовая реализация
│       │   ├── src/main/java # Приложение Vaadin
│       │   └── pom.xml        # Конфигурация пакета
│       ├── README.md          # Документация на английском
│       ├── README-RU.md       # Документация на русском
│       └── pom.xml            # Агрегатор модуля
├── specs/                     # Спецификации функционала
├── .specify/                  # Инструменты для спецификаций
├── README.md                  # Основная документация (английский)
├── README-RU.md               # Основная документация (русский)
└── pom.xml                    # Корневая конфигурация Maven
```

#### ✅ Настроен технологический стек
- **Java 17** (LTS)
- **Spring Boot 3.2.0**
- **Vaadin 24.3.0**
- **PostgreSQL Driver** (для Supabase)
- **JUnit 5** (тестирование)

#### ✅ Статус сборки и тестирования
```bash
# Сборка успешна ✓
mvn clean compile

# Тесты проходят ✓
mvn test
```

### Следующие шаги

1. **Настроить подключение к базе данных**
   ```bash
   export DATABASE_URL=jdbc:postgresql://your-supabase-host:5432/your-database
   export DATABASE_USERNAME=your-username
   export DATABASE_PASSWORD=your-password
   export JWT_SECRET=your-supabase-jwt-secret
   ```

2. **Запустить приложения**
   ```bash
   # Бэкенд
   cd packages/core-srv/base
   mvn spring-boot:run
   
   # Фронтенд (в другом терминале)
   cd packages/core-frt/base
   mvn spring-boot:run
   ```

3. **Создать первый функционал**
   - Следовать принципам конституции из `.specify/memory/constitution.md`
   - Создать GitHub Issue следуя `.github/instructions/github-issues.md`
   - Использовать шаблоны спецификаций из `.specify/templates/`
   - Рекомендуемый первый функционал: «Кластеры» (Кластеры/Домены/Ресурсы)

### Соответствие конституции ✅

Все принципы из `.specify/memory/constitution.md` выполнены:

| Принцип | Статус | Реализация |
|---------|--------|------------|
| I. Архитектура пакетов монорепозитория | ✅ | `packages/` с именованием `-srv`/`-frt` |
| II. Двуязычная документация | ✅ | README.md + README-RU.md (идентичная структура) |
| III. Абстракция базы данных | ✅ | JPA с драйвером PostgreSQL, абстракция готова |
| IV. Соответствие GitHub workflow | ✅ | Инструкции в `.github/instructions/` |
| V. Целостность технологического стека | ✅ | Java 17, Vaadin, Spring Boot настроены |
| VI. Разработка на основе спецификаций | ✅ | Шаблоны в `.specify/templates/` |

### Проверка двуязычности документации

Все файлы документации созданы с совпадающим количеством строк:

```
README.md       172 строки ✓
README-RU.md    172 строки ✓

core-srv/README.md    63 строки ✓
core-srv/README-RU.md 63 строки ✓

core-frt/README.md    69 строк ✓
core-frt/README-RU.md 69 строк ✓
```
