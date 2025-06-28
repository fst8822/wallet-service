# Wallet Service

REST API сервис для управления цифровыми кошельками с поддержкой операций депозита и снятия средств.

## Описание

Приложение предоставляет REST API для работы с кошельками:
- Выполнение операций пополнения (DEPOSIT) и снятия (WITHDRAW) средств
- Получение текущего баланса кошелька
- Обеспечение консистентности данных при высокой нагрузке (до 1000 RPS на один кошелек)

## Технологический стек

- **Java**: 8-17
- **Spring Boot**: 3.x
- **PostgreSQL**: База данных для хранения информации о кошельках
- **Liquibase**: Управление миграциями базы данных
- **Docker & Docker Compose**: Контейнеризация и оркестрация
- **JUnit**: Тестирование

## API Endpoints

### Выполнение операции с кошельком
```http
POST /api/v1/wallet
Content-Type: application/json

{
    "walletId": "f31d64fe-2c85-47bb-a007-9b44f36a5c7c",
    "operationType": "DEPOSIT",
    "amount": 1000
}
```

**Параметры:**
- `walletId` (UUID) - Уникальный идентификатор кошелька
- `operationType` (String) - Тип операции: `DEPOSIT` или `WITHDRAW`
- `amount` (Number) - Сумма операции

**Возможные ответы:**
- `200 OK` - Операция выполнена успешно
- `400 Bad Request` - Некорректные входные данные
- `404 Not Found` - Кошелек не найден
- `400 InsufficientFundsException` - Недостаточно средств для снятия

### Получение баланса кошелька
```http
GET /api/v1/wallets/{walletId}
```

**Параметры:**
- `walletId` (UUID) - Идентификатор кошелька

**Ответ:**
```json
{
    "walletId": "9e07b2d2-799b-4b1e-8920-35f64d570cbe",
    "balance": 5000
}
```

## Быстрый старт

### Предварительные требования
- Docker
- Docker Compose

### Запуск приложения

1. Клонировать репозиторий:
```bash
git clone <https://github.com/fst8822/wallet-service.git>
cd wallet-service
```

2. Запустить приложение с помощью Docker Compose:
```bash
docker-compose up -d
```

Приложение будет доступно по адресу: `http://localhost:8080`

### Остановка приложения
```bash
docker-compose down
```

## Конфигурация

Приложение поддерживает настройку параметров через переменные окружения без пересборки контейнеров.

### Переменные окружения приложения

#### Сервер
- `APP_PORT` - Порт приложения (по умолчанию: 8080)
- `TOMCAT_MAX_THREADS` - Максимальное количество потоков Tomcat (по умолчанию: 400)

#### Логирование
- `LOGGING_LEVEL_APP` - Уровень логирования для com.das.wallet (по умолчанию: DEBUG)
- `LOGGING_LEVEL_SPRING` - Уровень логирования для Spring (по умолчанию: INFO)
- `logging_level_hibernate_SQL` - Уровень логирования SQL запросов Hibernate (по умолчанию: INFO)

#### База данных
- `DB_HOST` - Хост базы данных (по умолчанию: localhost)
- `DB_PORT` - Порт базы данных (по умолчанию: 5432)
- `DB_NAME` - Имя базы данных (по умолчанию: wallet)
- `DB_USERNAME` - Пользователь БД (по умолчанию: postgres)
- `DB_PASSWORD` - Пароль БД (по умолчанию: root)

#### Пул соединений HikariCP
- `HIKARI_MAX_POOL_SIZE` - Максимальный размер пула соединений (по умолчанию: 20)
- `HIKARI_MIN_IDLE` - Минимальное количество неактивных соединений (по умолчанию: 5)
- `HIKARI_CONNECTION_TIMEOUT` - Таймаут соединения в миллисекундах (по умолчанию: 30000)

#### JPA/Hibernate
- `JPA_DDL_AUTO` - Стратегия DDL для Hibernate (по умолчанию: none)
- `SHOW_SQL` - Отображение SQL запросов в логах (по умолчанию: true)

#### Liquibase
- `SPRING_LIQUIBASE_ENABLED` - Включение Liquibase (по умолчанию: true)
- `SPRING_LIQUIBASE_CHANGE_LOG` - Путь к основному changelog файлу

### Переменные окружения PostgreSQL
- `POSTGRES_DB` - Имя базы данных
- `POSTGRES_USER` - Пользователь
- `POSTGRES_PASSWORD` - Пароль
- `POSTGRES_INITDB_ARGS` - Дополнительные параметры инициализации

### Пример настройки в `docker-compose.yml`:
```yaml
services:
  wallet-service:
    environment:
      - APP_PORT=8081
      - DB_HOST=postgres
      - DB_NAME=wallet_prod
      - LOGGING_LEVEL_APP=INFO
      - TOMCAT_MAX_THREADS=200
      - HIKARI_MAX_POOL_SIZE=30
  
  postgres:
    environment:
      - POSTGRES_DB=wallet_prod
      - POSTGRES_USER=wallet_user
      - POSTGRES_PASSWORD=secure_password
```

## База данных

Управление схемой базы данных осуществляется с помощью Liquibase. Миграции применяются автоматически при запуске приложения.

### Структура таблиц
- `wallets` - Основная таблица с информацией о кошельках и их балансах

## Обработка конкурентности

Приложение обеспечивает корректную работу в конкурентной среде:
- Использование пессимистических блокировок для критических секций
- Транзакционность операций
- Обработка состояний гонки при одновременных операциях над одним кошельком
- Retry механизмы для обработки временных блокировок

## Обработка ошибок

Приложение возвращает структурированные ответы для различных типов ошибок:

### Кошелек не найден (404)
```json
{
    "error": "WALLET_NOT_FOUND",
    "message": "Wallet with id 550e8400-e29b-41d4-a716-446655440000 not found"
}
```

### Недостаточно средств (422)
```json
{
    "error": "INSUFFICIENT_FUNDS",
    "message": "Insufficient funds for withdrawal. Current balance: 100, requested: 500"
}
```

### Некорректные данные (400)
```json
{
    "error": "INVALID_REQUEST",
    "message": "Invalid operation type. Must be DEPOSIT or WITHDRAW"
}
```

## Тестирование

Проект включает в себя комплексные тесты:

### Запуск тестов
```bash
./mvnw test
```

### Покрытие тестами
- Unit тесты для бизнес-логики
- Integration тесты для проверки работы с базой данных

## Мониторинг и логирование

- Структурированное логирование всех операций

### Требования для разработки
- Java 21
- Maven 3.6+
- Docker & Docker Compose

### Сборка проекта
```bash
./mvnw clean package
```

### Запуск в режиме разработки
```bash
./mvnw spring-boot:run
```

## Лицензия

[Укажите лицензию проекта]

## Контакты

[Контактная информация разработчика]
