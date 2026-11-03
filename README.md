# Open Shelf eLibrary

Open Shelf eLibrary is a Spring Boot library application designed to manage a small catalog of books, track borrow activity, and surface a simple browsing experience through Thymeleaf views. The project is organized to separate concerns cleanly across the web, service, persistence, and domain layers.

## Application architecture

This application follows a layered Spring Boot architecture:

- Web layer: controllers handle HTTP requests and render views
- Service layer: business logic lives in `BookService`
- Persistence layer: repositories encapsulate database access with Spring Data JPA
- Domain layer: model classes define the core entities and relationships
- Exception layer: custom runtime exceptions represent domain errors and API-level failure states

This design keeps the application easy to reason about, test, and extend as new features are added.

Project structure:

```text
com.elibrary
├── ElibraryApplication.java
├── controller
│   └── BookController.java
├── model
│   ├── Book.java
│   ├── Category.java
│   ├── User.java
│   └── BorrowRecord.java
├── repository
│   ├── BookRepository.java
│   ├── CategoryRepository.java
│   ├── UserRepository.java
│   └── BorrowRecordRepository.java
├── service
│   └── BookService.java
└── exception
    ├── BookNotFoundException.java
    └── BookUnavailableException.java
```

The application also serves templates and static assets from:

- `src/main/resources/templates`
- `src/main/resources/static`

## Domain modelling

The domain model is centered around the library’s inventory and borrowing flow.

### Book
`Book` is the primary catalog entity and stores:
- title
- author
- category
- description
- digital availability
- availability status
- digital preview content

### Category
`Category` organizes books into logical groups such as Fiction, Science, Fantasy, and Technology.

### User
`User` represents the library member or guest reader interacting with the system.

### BorrowRecord
`BorrowRecord` tracks the lifecycle of a loan:
- who borrowed the book
- which book was borrowed
- borrowed date
- due date
- return date
- current status (`BORROWED` / `RETURNED`)

This allows the application to retain the history of borrowing activity while also determining whether a title is currently available.

## API design

The project uses Spring MVC and Thymeleaf rather than a pure REST API. The main interaction points are web routes:

- `/`
- `/book`
- `/borrowed-books`
- `/digital`

The application supports book actions such as:
- borrow
- return
- digital preview retrieval

Controller responsibilities:
- map URLs to model-driven views
- populate data for the UI
- handle redirects and flash messages
- translate business exceptions into user-visible feedback

Examples:
- `GET /book` → displays available books
- `GET /borrowed-books` → displays active loans
- `GET /digital` → displays digital titles
- `POST /books/{id}/borrow` → marks a book as borrowed
- `POST /books/{id}/return` → marks a book as returned

## Code organization

The project is arranged to make responsibilities explicit:

- `controller`: entry points for incoming requests
- `service`: business logic and rules
- `repository`: database queries and persistence operations
- `model`: domain entities and relationships
- `exception`: application exceptions

This separation reduces coupling and supports easier maintenance as the library becomes larger. For example:
- controllers do not contain business rules
- service methods orchestrate the domain logic
- repositories stay focused on persistence concerns
- exception types centralize failure messages

This is especially important in applications that grow beyond a demo into a full eLibrary platform.

## Error handling

The application handles failures with custom exceptions:

- `BookNotFoundException`
- `BookUnavailableException`

These exceptions are mapped to HTTP-style semantics using Spring annotations, which helps communicate failure clearly.

Examples of handled scenarios:
- requesting a non-existent book ID
- borrowing an already borrowed book
- returning a book that is not currently borrowed
- requesting digital content for a non-digital book

Controller code catches these errors and exposes them through redirect messages, providing a clean user experience without exposing internal implementation details.

## Maintainability

The project is structured to support long-term maintainability through:

- clear package boundaries
- business logic in one service layer
- domain entities grouped by responsibility
- repository interfaces for persistence abstraction
- centralized error handling
- simple, readable MVC flow

Additional maintainability benefits:
- new features can be added without mixing UI, database, and business logic
- validation and rule changes are kept in the service layer
- persistence details are isolated from controllers
- domain entities are easier to evolve as business requirements change

## Run locally

Requirements:
- Java 17+
- Maven 3.6+

```bash
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080
```

The app redirects to `/book` by default.

## Notes

This project is designed as a demo foundation for a Spring Boot library application. It uses in-memory data and focuses on clean architecture, readable structure, and a straightforward borrowing workflow rather than production-scale authentication, payment, or persistent multi-user infrastructure.
