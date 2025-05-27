# Текстовый сканер для анализа отчетов

Микросервисная архитектура для анализа текстовых отчетов с функциями:
- Подсчет статистики: количество абзацев, слов, символов
- Сравнение файлов на схожесть
- Визуализация данных (облака слов)

## Архитектура
- **API Gateway** - маршрутизация запросов (nginx)
- **File Storing Service** - хранение и выдача файлов
- **File Analysis Service** - анализ файлов, хранение и выдача результатов

## Запуск проекта
```bash
docker-compose up -d
```

## API
- Документация API доступна по адресу: http://localhost/api/docs

## Разработка и тестирование

### Запуск тестов
Для запуска тестов с проверкой покрытия (минимум 65%):

```bash
# File Storing Service
cd file-storing-service
mvn clean test jacoco:report

# File Analysis Service
cd file-analysis-service
mvn clean test jacoco:report
```

### Отчеты о покрытии
После запуска тестов отчеты о покрытии кода будут доступны:
- File Storing Service: `file-storing-service/target/site/jacoco/index.html`
- File Analysis Service: `file-analysis-service/target/site/jacoco/index.html`

### Коллекция Postman
Для тестирования API доступна коллекция Postman в директории `postman/`