# Микросервис frontend-service
### Порядок запуска модулей
Использовать следующую последовательность команд для корректного запуска:

```bash
cd frontend-service
mvn clean install
mvn spring-boot:run

cd ../image-hosting
mvn clean install
mvn spring-boot:run

cd ../api-gateway
mvn clean install
mvn spring-boot:run
```
Главную страницу открывать по адресу:
```
http://localhost:8090/index
```
## Запуск для микросервиса frontend-service

1. Перейти в терминале к модулю:  ```cd frontend-service```

2. Выполнить очистку проекта по команде:
  ```bash
    mvn clean install
  ```

3. Запустить модуль командой:
  ```bash
        mvn spring-boot:run
  ```

<br>

**Примечание:** если НЕ установлен Maven требуется запустить с командой: ```./mvnw spring-boot:run ```

**В микросервисной архитектуре, особенно с REST, необходимо отдавать html-шаблоны только из frontend-сервиса.**

<br>
4. После запуска сервис доступен по адресу, если настроен application.yml, по адресу: http://localhost:8090/

http://localhost:8090/

Команда для проверки всех портов задействованных в проекте:
 ```bash
  netstat -ano | findstr ":8090" ":8081" ":8082" ":8084"
  ```
Команда остановить запущенный микросервис:
  ```bash
   Stop-Process -Id (Get-NetTCPConnection -LocalPort 8082).OwningProcess -Force
  ```

### Незащищённые маршруты (доступны без авторизации):

#### `auth-service`:

HTTP:GET  URL:`/login` Назначение:Страница входа <br>                
HTTP:GET  URL:`/registration` Назначение:Страница регистрации <br>  
HTTP:POST  URL:`/registration` Назначение:Отправка формы регистрации <br>

#### `image-hosting`:

HTTP:GET  URL:`/` Назначение:Главная страница с изображениями (`index.html`) <br>  
HTTP:GET  URL:`/email-page` Назначение:Форма отправки email <br>     
HTTP:POST  URL:`/sendEmail` Назначение:Отправка email-сообщения <br>

### Защищённые маршруты (требуют авторизации):

HTTP:POST URL:`/upload` Назначение:Загрузка изображений <br>  
HTTP:GET URL:`/images/{id}` Назначение:Просмотр отдельного изображения (если не открыто публично) <br>
HTTP:GET URL:`/profile` Назначение:Профиль пользователя <br>
HTTP:GET URL:`/logout` Назначение:Выход из системы (чаще защищено Keycloak/Security) <br>
HTTP:Все URL:`/api/**` Назначение:Вызовы REST API (например, через gateway) <br>