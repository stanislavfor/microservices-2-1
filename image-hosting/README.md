# Микросервис image-hosting
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
http://localhost:8084/index
```
## Запуск для модуля image-hosting

1. Выполнить в корне модуля image-hosting:

    ```bash
    mvn clean install
    ```
   или, например, ``` mvn clean ```
2. Запустить 'ImageHostingApplication' или выбрать команду:
   ```bash
    mvn spring-boot:run
   ```

3. Перейти в браузере по адресу: http://localhost:8090/

Команда для проверки всех портов задействованых в проекте:
```bash
    netstat -ano | findstr ":8090" :8082" ":8084"
 ```
Команда остановить запущенный микросервис:
   ```bash
    Stop-Process -Id (Get-NetTCPConnection -LocalPort 8090).OwningProcess -Force
   ```
##
#### Страница авторизации (login.html):

Это страница, на которой пользователи могут ввести свои логин и пароль для входа в систему.

## Главная страница:
### URL: http://localhost:8090/

##
#### Настройки для файла application.yml

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/images_db
    username: postgres # указать имя User из базы данных
    password: password # указать пароль, записанный для данной базы данных
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

```

#### SQL-запросы для создания базы данных PostgreSQL

Создание базы данных и таблиц в PostgreSQL:

```
-- Создать базу данных
CREATE DATABASE images_db;

-- Переключиться на созданную базу данных
\c images_db;

-- Создать таблицу users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL    
);

-- Создать таблицу item
CREATE TABLE item (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    link VARCHAR(200)
);

```

Добавление значений в Таблицу 'users'
```
INSERT INTO users (login, password) VALUES
('admin', '$2a$12$LbAPCsHn8ZN5MUDqDmIX7e9n1YlDkCxEt0lW3Q2WuW0M1vteo8jvG'),  -- пароль зашифрован
('user', '$2a$12$.dlnBAYq6sOUumn3jtG.AepxdSwGxJ8xA2iAPoCHSH61Vjl.JbIfq')   -- пароль зашифрован


```

#### Thymeleaf шаблон страницы my-app.html

```
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Login</title>
</head>
<body>
    <form th:action="@{/login}" method="post">
        <div>
            <label for="login">Login:</label>
            <input type="text" id="login" name="username"/>
        </div>
        <div>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password"/>
        </div>
        <div>
            <button type="submit">Login</button>
        </div>
    </form>
</body>
</html>

```

#### Хеширование пароля

```
String rawPassword = "admin";
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
boolean matches = encoder.matches(rawPassword, "$2a$12$LbAPCsHn8ZN5MUDqDmIX7e9n1YlDkCxEt0lW3Q2WuW0M1vteo8jvG");
System.out.println(matches); 
// Должно быть true

```

## Список методов и fetch-примеров для image-hosting (порт 8084)
В этом списке REST-запросов к сервису `image-hosting` (порт 8084) даны примеры вызова через JavaScript (`fetch`).
В этом списке указаны стандартные эндпоинты из контроллера `ImageController` приложения:


### 1. Получить все изображения (галерея)

**GET /images**

```js
fetch('http://localhost:8084/images')
  .then(res => res.json())
  .then(console.log);
```

### 2. Получить изображение по ID

**GET /images/{id}**

```js
fetch('http://localhost:8084/images/73')
  .then(res => res.json())
  .then(console.log);
```


### 3. Загрузить новое изображение (multipart/form-data)

**POST /images**
(Загрузка через форму — файл, имя, описание)

```js
const formData = new FormData();
formData.append("name", "Мое изображение");
formData.append("description", "Описание");
formData.append("file", fileInput.files[0]); // fileInput — input type="file"

fetch('http://localhost:8084/images', {
  method: 'POST',
  body: formData
})
  .then(res => res.text())
  .then(console.log);
```
Важно:
> Через fetch можно отправлять файлы только при наличии файла в input, из браузера.
> Или можно также использовать Postman.


### 4. Сохранить новое изображение (JSON, не файл)

**POST /images**
(Если backend поддерживает работу через JSON, а не multipart — стандартный REST):

```js
fetch('http://localhost:8084/images', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ name: "Фото", description: "desc", link: "somefile.jpg" })
})
  .then(res => res.json())
  .then(console.log);
```

### 5. Обновить изображение по ID (JSON)

**PUT /images/{id}**

```js
fetch('http://localhost:8084/images/73', {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ name: "Новое имя", description: "Новое описание", link: "file.jpg" })
})
  .then(res => res.json())
  .then(console.log);
```

### 6. Удалить изображение по ID

**DELETE /images/{id}**

```js
fetch('http://localhost:8084/images/73', { method: 'DELETE' })
  .then(res => console.log('Deleted!', res.status));
```

### 7. Получить изображение по имени файла

**GET /images/by-filename/{filename}**

```js
fetch('http://localhost:8084/images/by-filename/название_файла.jpg')
  .then(res => res.json())
  .then(console.log);
```

### 8. Отправка email-сообщения (если EmailController работает на 8084)

**POST /email**

```js
const formData = new FormData();
formData.append("username", "Имя");
formData.append("email", "email@example.com");
formData.append("message", "Текст сообщения");

fetch('http://localhost:8084/email', {
  method: 'POST',
  body: formData
})
  .then(res => res.text())
  .then(console.log);
```


### 9. Статичные файлы (картинки)

**GET /uploads/images/{filename}**
(Прямой доступ к картинке, если маппинг в Spring на папку `/uploads/images` настроен правильно)

```js
// Просто ссылка <img src="http://localhost:8084/uploads/images/файл.jpg">
```


## Краткая сводка эндпоинтов

| **Метод**     | **URL**                            | **Описание**       
```
   GET       /images                          - Все изображения  
   GET       /images/{id}                     - По id               
   POST      /images (multipart или json)     - Добавить            
   PUT       /images/{id} (json)              - Обновить             
   DELETE    /images/{id}                     - Удалить              
   GET       /images/by-filename/{filename}   - По имени файла      
   GET       /uploads/images/{filename}       - Картинка (статик)  
   POST      /email                           - Отправка сообщения  

```



<br><br>