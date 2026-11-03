# Open Shelf eLibrary

A Spring Boot eLibrary application using Spring MVC, Thymeleaf, and Spring Data JPA.

## Pages

- `/book` — browse books and borrow available titles.
- `/borrowed-books` — see active loans and return books.
- `/digital` — browse digital titles and read preview content.

## Java package structure

```text
com.elibrary
├── ElibraryApplication.java
├── controller/       # Spring MVC controllers
├── model/            # JPA entities: Book, Category, User, BorrowRecord
├── repository/       # Spring Data JPA repositories
├── service/          # Business logic
└── exception/        # Application exceptions
```

Thymeleaf views are in `src/main/resources/templates`; static assets are in `src/main/resources/static`.

The app uses an H2 in-memory database and seeds sample books/categories plus a guest reader on startup. Borrowing creates a 14-day borrow record, and returning closes the active record. Demo data resets when the app restarts. This is a starter application, not production authentication or licensed full-text content.

## Run locally

Requires Java 17+ and Maven 3.6+.

```bash
mvn spring-boot:run
```

Open http://localhost:8080 (redirects to `/book`). The optional H2 console is at http://localhost:8080/h2-console.
