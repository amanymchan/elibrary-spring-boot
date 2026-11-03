# Open Shelf eLibrary

A Spring Boot eLibrary application using Spring MVC and Thymeleaf server-rendered views. Browse and search the catalog, borrow and return titles, and open digital previews.

## Requirements

- Java 17 or newer
- Maven 3.6+

## Run locally

```bash
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).

## MVC routes

| Method | Route | Purpose |
| --- | --- | --- |
| `GET` | `/` | Browse, search, filter, and sort the catalog |
| `POST` | `/books/{id}/borrow` | Borrow an available book, then redirect to the catalog |
| `POST` | `/books/{id}/return` | Return a borrowed book, then redirect to the catalog |
| `GET` | `/books/{id}/digital` | Read a digital preview |

Views are Thymeleaf templates under `src/main/resources/templates`; styles are served from `src/main/resources/static`. Search, filtering, and sorting are handled by the MVC controller, while borrowing and returning use standard form POSTs.

## Notes

The catalog is seeded in memory and resets whenever the application restarts. This is a demo foundation, not a production library system: it does not yet include persistent storage, user accounts, authentication, due dates, or licensed full-text digital books. Replace preview text with content you have permission to distribute before deployment.
