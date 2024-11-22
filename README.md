# Разработка Системы Управления Задачами

## Для развертывания проекта необходимо:
* скачать репозиторий
* mvn clean package
* mvn install
* docker-compose build
* docker-compose up -d

## Реализовано следующее: 
* Авторизация / Регистрация новых пользователей
* Ролевая система администратора и пользователей
* Создание задачи / удаление - POST /task/create \ DELETE /delete/{taskId}
* Обновление основной информации у задачи - PUT /update/{taskId}
* Назначение исполнителя задачи - PUT /update-executor/{taskId}
* Создание комменатрия для определенной задачи - POST /comment/create/{taskId}
* Удаление комментария - DELETE /comment/delete-comment/{taskId}
  
## Зависимости

 * Spring Boot;
 * Spring Web;
 * Spring Security;
 * JJWT
 * PostgreSQL
 * Lombok
 * JUnit
  
