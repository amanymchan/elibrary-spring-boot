# Open Shelf eLibrary

A Spring Boot eLibrary application using Spring MVC, Thymeleaf, and Spring Data JPA.

## Architecture

- MVC controller: `BookController`
- JPA entities: `Book` (`books`), `Category` (`categories`), `User` (`library_users`), `BorrowRecord` (`borrow_records`)
- Relationships: each book belongs to a category; each borrow record references a book and a user; users have borrow history
- Service: `BookService` handles search/filter/sort and transactional borrow/return logic
- Spring Data repositories: `BookRepository`, `CategoryRepository`, `UserRepository`, `BorrowRecordRepository`
- Thymeleaf views: `catalog.html` and `reader.html` in `src/main/resources/templates`
- Database: H2 in-memory database

Borrowing creates a `BorrowRecord` for the seeded demo guest user, with a 14-day due date. Returning marks the active record returned and updates book availability. This demo currently uses a guest user rather than account login or per-user borrowing.

## Run locally

Requires Java 17+ and Maven 3.6+.

```bash
mvn spring-boot:run
```

Open http://localhost:8080. The optional H2 console is at http://localhost:8080/h2-console.

The in-memory database resets when the app restarts. This is a demo foundation and does not provide authentication, production-grade account management, or licensed full-text digital books.
