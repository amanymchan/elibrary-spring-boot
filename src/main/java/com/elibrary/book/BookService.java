package com.elibrary.book;

import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        seedData();
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<Book> filterAndSort(String query, String filter, String sort) {
        String trimmedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        String normalizedFilter = switch (filter) {
            case "available" -> "available";
            case "digital" -> "digital";
            default -> "all";
        };
        String normalizedSort = switch (sort) {
            case "author" -> "author";
            default -> "title";
        };

        return bookRepository.findAll().stream()
                .filter(book -> trimmedQuery.isEmpty()
                        || containsText(book, trimmedQuery))
                .filter(book -> normalizedFilter.equals("all")
                        || (normalizedFilter.equals("available") && book.isAvailable())
                        || (normalizedFilter.equals("digital") && book.isDigital()))
                .sorted((a, b) -> {
                    if (normalizedSort.equals("author")) {
                        return a.getAuthor().compareToIgnoreCase(b.getAuthor());
                    }
                    return a.getTitle().compareToIgnoreCase(b.getTitle());
                })
                .toList();
    }

    @Transactional
    public Book borrow(long id) {
        Book book = findById(id);
        if (!book.isAvailable()) {
            throw new BookUnavailableException("This book is already borrowed.");
        }
        book.setAvailable(false);
        return bookRepository.save(book);
    }

    @Transactional
    public Book returnBook(long id) {
        Book book = findById(id);
        if (book.isAvailable()) {
            throw new BookUnavailableException("This book is not currently borrowed.");
        }
        book.setAvailable(true);
        return bookRepository.save(book);
    }

    public String digitalContent(long id) {
        Book book = findById(id);
        if (!book.isDigital() || book.getDigitalContent() == null) {
            throw new BookUnavailableException("Digital content is not available for this title.");
        }
        return book.getDigitalContent();
    }

    private boolean containsText(Book book, String query) {
        return book.getTitle().toLowerCase(Locale.ROOT).contains(query)
                || book.getAuthor().toLowerCase(Locale.ROOT).contains(query)
                || book.getCategory().toLowerCase(Locale.ROOT).contains(query)
                || book.getDescription().toLowerCase(Locale.ROOT).contains(query);
    }

    private void seedData() {
        if (bookRepository.count() > 0) {
            return;
        }

        bookRepository.saveAll(List.of(
                new Book("The Secret Garden", "Frances Hodgson Burnett", "Fiction",
                        "A classic story of friendship and a hidden garden.", true, true,
                        "The Secret Garden — Digital preview\n\nMary Lennox discovers a locked garden and, with new friends, brings it back to life.\n\nThis demonstration reader contains a short preview. Add licensed content before production use."),
                new Book("Pride and Prejudice", "Jane Austen", "Classic",
                        "A witty novel of manners, family, and first impressions.", true, true,
                        "Pride and Prejudice — Digital preview\n\nIt is a truth universally acknowledged, that a single person in possession of a good fortune, must be in want of a partner.\n\nThis demonstration reader contains a short preview. Add licensed content before production use."),
                new Book("A Brief History of Time", "Stephen Hawking", "Science",
                        "An accessible introduction to cosmology and the universe.", false, true, null),
                new Book("The Hobbit", "J. R. R. Tolkien", "Fantasy",
                        "A reluctant hobbit joins an unexpected adventure.", true, true,
                        "The Hobbit — Digital preview\n\nIn a hole in the ground there lived a hobbit.\n\nThis demonstration reader contains a short preview. Add licensed content before production use."),
                new Book("Clean Code", "Robert C. Martin", "Technology",
                        "Principles and practices for writing readable software.", false, true, null)
        ));
    }
}
