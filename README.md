# Open Shelf eLibrary

A small full-stack eLibrary application built with Spring Boot. It includes a responsive web catalog, book search and filters, borrow/return actions, and a simple digital preview reader.

## Requirements

- Java 17 or newer
- Maven 3.6+ (or use the included Maven wrapper if you add one)

## Run locally

```bash
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080). The frontend is served by Spring Boot from `src/main/resources/static` and uses the REST API on the same origin.

## REST API

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/api/books` | List catalog books and availability |
| `GET` | `/api/books/{id}` | Get a book |
| `POST` | `/api/books/{id}/borrow` | Borrow an available book |
| `POST` | `/api/books/{id}/return` | Return a borrowed book |
| `GET` | `/api/books/{id}/digital` | Read a digital preview |

Borrowing an already-borrowed book, returning an available book, or requesting a digital copy for a print-only book returns HTTP `409 Conflict`. Unknown IDs return `404 Not Found`.

## Notes

The catalog is seeded in memory and resets whenever the application restarts. This is a demo foundation, not a production library system: it does not yet include persistent storage, user accounts, authentication, due dates, or licensed full-text digital books. Replace preview text with content you have permission to distribute before deployment.
