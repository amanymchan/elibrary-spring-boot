# Open Shelf eLibrary

A Spring Boot eLibrary application using Spring MVC, Thymeleaf, and Spring Data JPA.

## Pages

- `/book` — browse all books and borrow available titles.
- `/borrowed-books` — see the guest reader's active loans, due dates, and return books.
- `/digital` — browse digital books and read available preview content.

## Architecture

- MVC controller: `BookController`
- JPA entities: `Book`, `Category`, `User`, `BorrowRecord`
- Spring Data repositories: `BookRepository`, `CategoryRepository`, `UserRepository`, `BorrowRecordRepository`
- Service: `BookService` handles catalog, digital content, and transactional borrowing/returning logic.
- Thymeleaf page templates: `books.html`, `borrowed-books.html`, and `digital.html` in `src/main/resources/templates`.
- Database: H2 in-memory database.

Borrowing creates a `BorrowRecord` for the seeded demo guest user, with a 14-day due date. Returning marks the active record as returned and updates book availability. The demo currently uses a shared guest user; it does not include authentication or per-user accounts.

## Run locally

Requires Java 17+ and Maven 3.6+.

```bash
mvn spring-boot:run
```

Open http://localhost:8080 (redirects to `/book`). The optional H2 console is at http://localhost:8080/h2-console.

The in-memory database resets when the app restarts. Digital book text is demonstration preview content; replace it only with content you have permission to distribute.
