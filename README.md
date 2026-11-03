# Open Shelf eLibrary

A Spring Boot eLibrary application built with Spring MVC, Thymeleaf, and JPA. The app includes a server-rendered catalog, search/filter/sort controls, borrow/return actions, and digital content previews.

## Architecture

- Controller: `BookController` in `com.elibrary.book`
- Data model: `Book` entity mapped to the `books` table via JPA annotations
- Service layer: `BookService`
- Repository: `BookRepository` extending `JpaRepository`
- Views: Thymeleaf templates in `src/main/resources/templates`
- Database: H2 in-memory database configured in `application.properties`

## Requirements

- Java 17+
- Maven 3.6+

## Run locally

```bash
mvn spring-boot:run
```

Open: http://localhost:8080

Open the H2 console at: http://localhost:8080/h2-console

## Notes

The application uses an in-memory H2 database, so the catalog resets when the app restarts. This is a demo foundation and does not yet include authentication, user accounts, due dates, or an external digital library service.
