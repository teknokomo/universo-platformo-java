# Пакет Core Frontend (core-frt)

Пользовательский интерфейс и инфраструктура фронтенда для Universo Platformo.

## Обзор

Данный пакет предоставляет базовый функционал UI включая:
- Основа приложения Vaadin
- UI компоненты и представления
- Маршрутизация фронтенда
- Кастомизация темы
- Коммуникация клиент-сервер

## Структура

```
core-frt/
└── base/                   # Базовая реализация
    ├── src/
    │   ├── main/
    │   │   ├── java/      # Исходный код Java
    │   │   └── resources/ # Конфигурация и файлы темы
    │   └── test/          # Код тестов
    └── pom.xml            # Конфигурация Maven
```

## Зависимости

- Vaadin Spring Boot Starter
- Пакет Core Server (core-srv-base)
- Vaadin TestBench (для тестирования)

## Конфигурация

См. `src/main/resources/application.properties` для опций конфигурации.

Ключевые настройки:
- `vaadin.productionMode`: Включение продакшен-оптимизаций
- `vaadin.frontend.hotdeploy`: Включение горячего развёртывания в разработке

## Запуск

```bash
cd packages/core-frt/base
mvn spring-boot:run
```

Доступ к приложению по адресу `http://localhost:8080`

## Разработка

Vaadin автоматически скомпилирует фронтенд-ресурсы при первом доступе.
В режиме разработки изменения в коде Java будут перезагружаться горячо.

## Тестирование

```bash
cd packages/core-frt/base
mvn test
```

## Сборка для продакшена

```bash
cd packages/core-frt/base
mvn clean package -Pproduction
```

Это создаст оптимизированную продакшен-сборку с минифицированными фронтенд-ресурсами.
