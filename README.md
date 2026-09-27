# Spring Boot Task Tracker - NTI

A Task Tracker REST API developed as part of the **NTI (National Telecommunication Institute) Enterprise Java training**.

The project demonstrates building a layered Spring Boot application with RESTful APIs, Spring Data JPA, PostgreSQL, validation, exception handling, Spring Profiles, type-safe configuration properties, and Spring Boot Actuator.

---

## Tech Stack

- **Java:** 25
- **Spring Boot:** 4.1.1
- **Spring Web:** RESTful API development
- **Spring Data JPA:** Data access and persistence
- **Hibernate:** ORM
- **Jakarta Validation:** Request validation
- **PostgreSQL:** Relational database
- **Lombok:** Boilerplate reduction
- **Spring Boot Actuator:** Application monitoring and management
- **Maven:** Dependency management and build tool

---

## Project Structure

```text
src
└── main
    ├── java
    │   └── com.abdelrahman.tasktracker
    │       ├── controller
    │       │   └── TaskController
    │       │
    │       ├── dto
    │       │   ├── DevTaskProperties
    │       │   └── ProdTaskProperties
    │       │
    │       ├── exceptions
    │       │   ├── MaxTasksException
    │       │   └── TaskNotFoundException
    │       │
    │       ├── models
    │       │   └── Task
    │       │
    │       ├── profiles
    │       │   ├── DevController
    │       │   └── ProdController
    │       │
    │       ├── repo
    │       │   └── TaskRepo
    │       │
    │       ├── service
    │       │   └── TaskService
    │       │
    │       └── TaskTrackerApplication
    │
    └── resources
        ├── application.properties
        ├── application-dev.properties
        └── application-prod.properties
```

---

## Architecture

The application follows a simple layered architecture:

```text
                 HTTP Request
                      │
                      ▼
              ┌───────────────┐
              │ TaskController│
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │  TaskService  │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │    TaskRepo   │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │ PostgreSQL DB │
              └───────────────┘
```

Configuration for development and production-style environments is separated using Spring Profiles and `@ConfigurationProperties`.

---

## Task Model

The `Task` entity is mapped to the `tasks` table in PostgreSQL.

```java
@Entity
@Table(name = "tasks")
public class Task {

    private Integer id;
    private String title;
    private String description;
    private Boolean completed;
    private LocalDate dueDate;
}
```

### Validation

The task title is required:

```java
@NotBlank
private String title;
```

Request validation is triggered using `@Valid` in the controller.

---

## REST API

Base URL:

```text
http://localhost:8080/api/tasks
```

### Create a Task

```http
POST /api/tasks
```

Example request:

```json
{
  "title": "Study Spring Boot",
  "description": "Review Spring Data JPA and REST APIs",
  "completed": false,
  "dueDate": "2026-10-01"
}
```

### Get Tasks

```http
GET /api/tasks
```

Optional query parameters:

```text
limit
completed
```

Examples:

```http
GET /api/tasks?limit=5
```

```http
GET /api/tasks?completed=false
```

```http
GET /api/tasks?limit=5&completed=false
```

### Get Task by ID

```http
GET /api/tasks/{id}
```

Example:

```http
GET /api/tasks/1
```

### Update a Task

```http
PUT /api/tasks/{id}
```

### Mark a Task as Completed

```http
PATCH /api/tasks/{id}/complete
```

### Delete a Task

```http
DELETE /api/tasks/{id}
```

---

## Exception Handling

The application defines custom exceptions for common business cases:

### `TaskNotFoundException`

Used when a requested task does not exist.

```text
HTTP 404 Not Found
```

### `MaxTasksException`

Used when the configured maximum number of tasks has been reached.

```text
HTTP 409 Conflict
```

---

## Configuration Properties

The project uses Spring Boot's type-safe `@ConfigurationProperties` instead of reading every custom configuration value individually.

### Development Properties

```java
@ConfigurationProperties(prefix = "task")
public record DevTaskProperties(
        String name,
        @DefaultValue("15") int num,
        @DefaultValue("Osama default") String user,
        String description
) {
}
```

Example configuration:

```properties
task.name=Spring Profile
task.num=2000
task.user=Mohamed
task.description=Spring Profile Task Is Started...
```

### Production Properties

A separate `ProdTaskProperties` configuration is used for the production profile.

This keeps environment-specific configuration separated from the application logic.

---

## Spring Profiles

The project demonstrates environment-specific beans using `@Profile`.

### Development Controller

```java
@Profile("dev")
@RestController
public class DevController {
    // ...
}
```

Endpoint:

```http
GET /api/dev/data
```

### Production Controller

```java
@Profile("prod")
@RestController
public class ProdController {
    // ...
}
```

Endpoint:

```http
GET /api/prod/data
```

The project contains separate configuration files:

```text
application.properties
application-dev.properties
application-prod.properties
```

The active profiles can be configured using:

```properties
spring.profiles.active=dev,prod
```

For normal environment separation, the active profile can be changed to the required environment, for example:

```properties
spring.profiles.active=dev
```

or:

```properties
spring.profiles.active=prod
```

---

## Spring Boot Actuator

Spring Boot Actuator is included for application monitoring and management.

Enabled endpoints:

```properties
management.endpoints.web.exposure.include=health,info,metrics
management.info.env.enabled=true
```

### Health

```http
GET /actuator/health
```

### Application Info

```http
GET /actuator/info
```

Configured application information:

```properties
info.app.name=Task Tracker
info.app.version=1.0.0
```

Example response:

```json
{
  "app": {
    "name": "Task Tracker",
    "version": "1.0.0"
  }
}
```

### Metrics

```http
GET /actuator/metrics
```

---

## Database Configuration

The application uses PostgreSQL with Spring Data JPA.

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/demo-tasks
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Replace `YOUR_PASSWORD` with your local PostgreSQL password. Do not commit real database credentials to GitHub.

---

## Application Configuration

The project also includes task-related configuration:

```properties
tasktracker.max-tasks=50
tasktracker.default-page-size=10
```

These values are used by the application to control task limits and default result sizes.

---

## Running the Project

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/Spring-Boot-Task-Tracker-NTI.git
cd Spring-Boot-Task-Tracker-NTI
```

### 2. Configure PostgreSQL

Create a database named:

```text
demo-tasks
```

Then configure your local PostgreSQL credentials in `application.properties`.

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The application runs by default on:

```text
http://localhost:8080
```

---

## Useful Endpoints

| Purpose | Method | Endpoint |
|---|---|---|
| Create task | POST | `/api/tasks` |
| Get tasks | GET | `/api/tasks` |
| Get task | GET | `/api/tasks/{id}` |
| Update task | PUT | `/api/tasks/{id}` |
| Complete task | PATCH | `/api/tasks/{id}/complete` |
| Delete task | DELETE | `/api/tasks/{id}` |
| Dev profile data | GET | `/api/dev/data` |
| Prod profile data | GET | `/api/prod/data` |
| Actuator health | GET | `/actuator/health` |
| Actuator info | GET | `/actuator/info` |
| Actuator metrics | GET | `/actuator/metrics` |

---

## Key Spring Boot Concepts Demonstrated

- REST Controllers with `@RestController`
- Request mapping with `@RequestMapping`
- CRUD endpoints
- `@RequestBody`
- `@PathVariable`
- `@RequestParam`
- Request validation with `@Valid` and `@NotBlank`
- Layered architecture
- Dependency Injection
- Spring Data JPA repositories
- JPA entity mapping
- PostgreSQL integration
- Custom exceptions
- HTTP status handling with `ResponseEntity`
- Spring Profiles
- `@ConfigurationProperties`
- `@DefaultValue`
- `@ConfigurationPropertiesScan`
- Spring Boot Actuator
- Application health, info, and metrics endpoints
- Externalized application configuration

---

## Purpose

This project was developed as part of my **NTI Enterprise Java training** to practice building a Spring Boot REST application and applying core Spring concepts in a practical project.

It serves as a foundation for further improvements such as centralized exception handling, DTOs, richer API responses, pagination, and additional production-oriented features.
