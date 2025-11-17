# Universo Platformo Java

Реализация Universo Platformo / Universo MMOOMM / Universo Kiberplano на базе Vaadin / Spring и связанного стека на Java.

## Обзор

Universo Platformo Java — это комплексная реализация полностековой платформы с использованием корпоративных технологий Java. Данный проект следует концептуальной архитектуре [Universo Platformo React](https://github.com/teknokomo/universo-platformo-react), адаптируя паттерны к лучшим практикам экосистемы Java/Vaadin/Spring.

## Технологический стек

- **Язык**: Java 17+ (LTS)
- **Фреймворк фронтенда**: Vaadin 24.x (Flow)
- **Фреймворк бэкенда**: Spring Boot 3.x с Spring Framework 6.x
- **База данных**: Supabase (на базе PostgreSQL) с абстрагированным доступом к данным
- **Инструмент сборки**: Maven (мультимодульный монорепозиторий)
- **Тестирование**: JUnit 5, Spring Test, Vaadin TestBench
- **UI тема**: Тема Vaadin Lumo с кастомизациями в стиле Material Design

## Структура проекта

Проект следует архитектуре монорепозитория с пакетами, организованными в директории `packages/`:

```
universo-platformo-java/
├── packages/
│   ├── core-srv/          # Базовые серверные сервисы
│   │   └── base/          # Базовая реализация
│   └── core-frt/          # Базовый пользовательский интерфейс
│       └── base/          # Базовая реализация
├── specs/                 # Спецификации функционала
├── .specify/              # Инструменты и шаблоны для спецификаций
└── pom.xml               # Корневая конфигурация Maven
```

### Соглашение об именовании пакетов

- Суффикс `-srv`: Пакеты бэкенда/сервера
- Суффикс `-frt`: Пакеты фронтенда/UI
- Директория `base/`: Базовая реализация (поддерживает будущие множественные реализации)

## Начало работы

### Предварительные требования

- Java 17 или новее (рекомендуется LTS версия)
- Maven 3.9.x или новее
- База данных PostgreSQL (или аккаунт Supabase)

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
# Запуск серверных сервисов
cd packages/core-srv/base
mvn spring-boot:run

# Запуск приложения фронтенда (в другом терминале)
cd packages/core-frt/base
mvn spring-boot:run
```

Приложение будет доступно по адресу `http://localhost:8080`

## Конфигурация

### Конфигурация базы данных

Установите следующие переменные окружения для подключения к базе данных:

```bash
export DATABASE_URL=jdbc:postgresql://your-supabase-host:5432/your-database
export DATABASE_USERNAME=your-username
export DATABASE_PASSWORD=your-password
```

### Конфигурация аутентификации

Установите JWT-секрет для аутентификации Supabase:

```bash
export JWT_SECRET=your-supabase-jwt-secret
```

**Важно**: Никогда не коммитьте учётные данные в репозиторий. Всегда используйте переменные окружения или внешние файлы конфигурации.

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
cd packages/core-srv/base
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
- **Руководство по переводу паттернов**: [`.specify/memory/react-to-java-patterns.md`](.specify/memory/react-to-java-patterns.md) - Полное соответствие паттернов React/Express эквивалентам Vaadin/Spring с примерами кода
- **Анализ архитектуры**: [`.specify/memory/react-architecture-analysis.md`](.specify/memory/react-architecture-analysis.md) - Глубокий анализ структуры репозитория React, паттернов и 32+ реализованных функций
- **Дорожная карта функций**: [`.specify/memory/feature-implementation-roadmap.md`](.specify/memory/feature-implementation-roadmap.md) - Упорядоченная по приоритетам дорожная карта реализации функций из референсной реализации React
- **Анализ пробелов**: [`.specify/memory/gap-analysis.md`](.specify/memory/gap-analysis.md) - Детальное сравнение статуса реализаций React и Java

**Примечание**: Реализация на React частично завершена и содержит легаси-код, который рефакторится. Данная реализация на Java должна сосредоточиться на чистых, корпоративных паттернах.

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
