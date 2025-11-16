# Пакет Core Server (core-srv)

Серверные сервисы и инфраструктура для Universo Platformo.

## Обзор

Данный пакет предоставляет базовый функционал бэкенда включая:
- Основа приложения Spring Boot
- Слой доступа к данным и репозитории
- Сервисы бизнес-логики
- REST API эндпоинты
- Безопасность и аутентификация

## Структура

```
core-srv/
└── base/                   # Базовая реализация
    ├── src/
    │   ├── main/
    │   │   ├── java/      # Исходный код Java
    │   │   └── resources/ # Файлы конфигурации
    │   └── test/          # Код тестов
    └── pom.xml            # Конфигурация Maven
```

## Зависимости

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- PostgreSQL Driver

## Конфигурация

См. `src/main/resources/application.properties` для опций конфигурации.

Ключевые переменные окружения:
- `DATABASE_URL`: URL подключения к базе данных PostgreSQL
- `DATABASE_USERNAME`: Имя пользователя базы данных
- `DATABASE_PASSWORD`: Пароль базы данных
- `JWT_SECRET`: Секретный ключ для валидации JWT токенов

## Запуск

```bash
cd packages/core-srv/base
mvn spring-boot:run
```

## Тестирование

```bash
cd packages/core-srv/base
mvn test
```

## Сборка

```bash
cd packages/core-srv/base
mvn clean package
```
