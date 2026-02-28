# Universo Platformo Java

Реализация Universo Platformo / Universo MMOOMM / Universo Kiberplano на базе Vaadin / Spring и связанного стека на Java.

## Обзор

Universo Platformo Java — это комплексная реализация полностековой платформы с использованием корпоративных технологий Java. Данный проект следует концептуальной архитектуре [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react), адаптируя паттерны к лучшим практикам экосистемы Java/Vaadin/Spring.

## Технологический стек

- **Язык**: Java 17+ (LTS)
- **Фреймворк фронтенда**: Vaadin 24.x (Flow) — серверные UI-компоненты, отображаемые в браузере
- **Фреймворк бэкенда**: Spring Boot 3.x с Spring Framework 6.x
- **Аутентификация**: Supabase Auth REST API (вызывается исключительно из серверных сервисов)
- **База данных**: Supabase (на базе PostgreSQL) с абстрагированным доступом к данным
- **Инструмент сборки**: Maven (мультимодульный монорепозиторий)
- **Тестирование**: JUnit 5, Spring Test, Mockito, Vaadin TestBench
- **UI тема**: Тема Vaadin Lumo с пользовательскими стилями

## Структура проекта

**⚠️ ОБЯЗАТЕЛЬНОЕ ТРЕБОВАНИЕ К АРХИТЕКТУРЕ ⚠️**

ВЕСЬ функционал в этом проекте ДОЛЖЕН быть реализован как модульные пакеты в директории `packages/`. Создание функционала вне этой структуры нарушает конституцию проекта (Принцип I — БЕЗУСЛОВНЫЙ) и будет отклонено при code review.

Эта модульная архитектура основана на проверенном паттерне из [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react), который успешно реализует 32+ модульных пакета. Модульная структура ОБЯЗАТЕЛЬНА, потому что отдельные пакеты в будущем будут извлечены в отдельные репозитории по мере развития платформы.

**ЗАПРЕТ**: Код функционала НЕ ДОЛЖЕН создаваться вне директории `packages/`. Исключение составляют только общие инфраструктурные файлы (конфигурация сборки, корневая документация, CI/CD).

Проект использует архитектуру монорепозитория с пакетами, организованными в директории `packages/`:

```
universo-platformo-java/
├── packages/
│   ├── core-srv/          # Базовые серверные сервисы
│   │   └── base/          # Базовая реализация (Spring Boot, JPA, Security)
│   ├── core-frt/          # Базовый UI — точка входа приложения Spring Boot
│   │   └── base/          # Оболочка приложения Vaadin, зависит от start-* пакетов
│   ├── start-srv/         # Серверные сервисы стартовой страницы
│   │   └── base/          # HTTP-клиент Supabase Auth, DTO, конфигурация
│   └── start-frt/         # Представления фронтенда стартовой страницы
│       └── base/          # Страница гостя, онбординг, страница входа
├── .specify/              # Инструменты и шаблоны для спецификаций
└── pom.xml                # Корневая конфигурация Maven
```

### Соглашение об именовании пакетов

- Суффикс `-srv`: Пакеты бэкенда/сервера (ОБЯЗАТЕЛЬНО для всего функционала бэкенда)
- Суффикс `-frt`: Пакеты фронтенда/UI (ОБЯЗАТЕЛЬНО для всего функционала фронтенда)
- Директория `base/`: Базовая реализация (ОБЯЗАТЕЛЬНА в каждом пакете, поддерживает будущие реализации)

### Архитектура: Фронтенд — Бэкенд — Supabase

Фронтенд никогда не обращается к Supabase напрямую. Все вызовы Supabase API проходят через бэкенд:

```
Браузер ── (Vaadin WebSocket/HTTP) ──► Представления Vaadin (start-frt)
                                              │
                                              ▼
                                     SupabaseAuthService (start-frt)
                                     [управление сессией]
                                              │
                                              ▼
                                     SupabaseAuthClient (start-srv)
                                     [HTTP-клиент с таймаутами]
                                              │
                                              ▼
                                       Supabase REST API
```

`SupabaseAuthClient` (`start-srv`) — единственный компонент, которому разрешено обращаться к Supabase.
`SupabaseAuthService` (`start-frt`) управляет сессией Vaadin; к Supabase напрямую не обращается.

## Начало работы

### Предварительные требования

- Java 17 или новее (рекомендуется LTS версия)
- Maven 3.9.x или новее
- Проект Supabase (для аутентификации) или база данных PostgreSQL

### Установка

1. Клонируйте репозиторий:
```bash
git clone https://github.com/teknokomo/universo-platformo-java.git
cd universo-platformo-java
```

2. Соберите проект:
```bash
mvn clean install
```

3. Настройте переменные окружения (см. раздел «Конфигурация» ниже)

4. Запустите приложение:
```bash
cd packages/core-frt/base
mvn spring-boot:run
```

Приложение будет доступно по адресу `http://localhost:8080`

## Конфигурация

### Аутентификация Supabase

Установите следующие переменные окружения для включения аутентификации через Supabase:

```bash
export SUPABASE_URL=https://your-project-id.supabase.co
export SUPABASE_ANON_KEY=your-anon-public-key
export SUPABASE_JWT_SECRET=your-jwt-secret
```

Эти значения находятся в вашем проекте Supabase в разделе **Settings → API**.

### Конфигурация базы данных

Установите следующие переменные окружения для подключения к базе данных PostgreSQL:

```bash
export DATABASE_URL=jdbc:postgresql://your-supabase-host:5432/your-database
export DATABASE_USERNAME=your-username
export DATABASE_PASSWORD=your-password
```

### Конфигурация JWT

```bash
export JWT_SECRET=your-supabase-jwt-secret
```

**Важно**: Никогда не коммитьте учётные данные в репозиторий. Всегда используйте переменные окружения или внешние файлы конфигурации.

## Стартовые страницы

Модуль `start-frt/base` предоставляет две стартовые страницы в зависимости от состояния аутентификации:

- **Страница гостя** (`/`): Лендинг с секцией-герой, отзывами и футером. Содержит кнопки «Войти» и «В будущее», ведущие на `/login`.
- **Страница авторизованного** (`/`): Трёхшаговый мастер онбординга (Приветствие → Выбор интересов → Завершение) после входа. Отображает email пользователя и кнопку выхода.
- **Страница входа** (`/login`): Форма с вкладками для входа и регистрации через Supabase по email и паролю.

## Рекомендации по разработке

### Конституция

Этот проект следует строгим принципам управления, определённым в [`.specify/memory/constitution.md`](.specify/memory/constitution.md):

1. **Архитектура пакетов монорепозитория**: Весь функционал организован в дискретные пакеты
2. **Двуязычная документация**: Вся документация на английском и русском языках (НЕОСПОРИМО)
3. **Абстракция базы данных**: Доступ к данным абстрагирован для поддержки множественных СУБД
4. **Соответствие GitHub workflow**: Разработка с приоритетом Issue (НЕОСПОРИМО)
5. **Целостность технологического стека**: Лучшие практики Java/Vaadin/Spring
6. **Разработка на основе спецификаций**: Подход с приоритетом шаблонов (НЕОСПОРИМО)

### Рабочий процесс

1. Создайте GitHub Issue следуя [`.github/instructions/github-issues.md`](.github/instructions/github-issues.md)
2. Примените метки согласно [`.github/instructions/github-labels.md`](.github/instructions/github-labels.md)
3. Создайте спецификацию используя шаблоны из `.specify/templates/`
4. Выполните реализацию следуя спецификации
5. Создайте Pull Request следуя [`.github/instructions/github-pr.md`](.github/instructions/github-pr.md)

### Документация

Вся документация должна соответствовать требованиям двуязычности согласно [`.github/instructions/i18n-docs.md`](.github/instructions/i18n-docs.md):
- Сначала версия на английском (основной стандарт)
- Версия на русском с идентичной структурой и количеством строк
- Обе версии обновляются атомарно

## Тестирование

Запуск всех тестов:
```bash
mvn test
```

Запуск тестов для конкретного пакета:
```bash
cd packages/start-srv/base
mvn test
```

## Сборка для продакшена

Сборка продакшен-артефактов:
```bash
mvn clean package -Pproduction
```

Это создаст оптимизированные сборки с включённым продакшен-режимом Vaadin.

## Референсная реализация

Данная реализация основана на концепциях из [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react). Следуя той же концептуальной архитектуре, данный проект адаптирует паттерны к соглашениям и лучшим практикам экосистемы Java.

**Ключевые справочные документы**:
- **Валидация лучших практик**: [`.specify/memory/best-practices-validation-2025-11-18.md`](.specify/memory/best-practices-validation-2025-11-18.md)
- **Руководство по Spring Modulith**: [`.specify/memory/spring-modulith-verification-guide.md`](.specify/memory/spring-modulith-verification-guide.md)
- **Лучшие практики Java/Vaadin/Spring**: [`.specify/memory/java-vaadin-spring-best-practices.md`](.specify/memory/java-vaadin-spring-best-practices.md)
- **Руководство по переводу паттернов**: [`.specify/memory/react-to-java-patterns.md`](.specify/memory/react-to-java-patterns.md)
- **Анализ архитектуры**: [`.specify/memory/react-architecture-analysis.md`](.specify/memory/react-architecture-analysis.md)
- **Дорожная карта функций**: [`.specify/memory/feature-implementation-roadmap.md`](.specify/memory/feature-implementation-roadmap.md)
- **Анализ пробелов**: [`.specify/memory/gap-analysis.md`](.specify/memory/gap-analysis.md)

**Примечание**: Реализация на React частично завершена и содержит легаси-код, который рефакторится. Данная реализация на Java сосредоточена на чистых, корпоративных паттернах.

## Участие в разработке

1. Следуйте принципам конституции
2. Создавайте issue перед реализацией
3. Поддерживайте двуязычную документацию
4. Следуйте разработке на основе спецификаций
5. Убедитесь, что все тесты проходят
6. Обновляйте документацию по мере необходимости

## Лицензия

[Будет определено]

## Ссылки

- Документация: [docs.universo.pro](https://docs.universo.pro) (скоро)
- Референсная реализация: [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react)
- Руководство по переводу паттернов: [`.specify/memory/react-to-java-patterns.md`](.specify/memory/react-to-java-patterns.md)
- Конституция проекта: [`.specify/memory/constitution.md`](.specify/memory/constitution.md)
