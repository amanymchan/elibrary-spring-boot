package com.elibrary.book;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final Map<Long, Book> books = new ConcurrentHashMap<>();

    public BookService() {
        add(new Book(1, "The Secret Garden", "Frances Hodgson Burnett", "Fiction",
                "A classic story of friendship and a hidden garden.", true, true,
                "The Secret Garden — Digital preview\n\nMary Lennox discovers a locked garden and, with new friends, brings it back to life.\n\nThis demonstration reader contains a short preview. Add licensed content before production use."));
        add(new Book(2, "Pride and Prejudice", "Jane Austen", "Classic",
                "A witty novel of manners, family, and first impressions.", true, true,
                "Pride and Prejudice — Digital preview\n\nIt is a truth universally acknowledged, that a single person in possession of a good fortune, must be in want of a partner.\n\nThis demonstration reader contains a short preview. Add licensed content before production use."));
        add(new Book(3, "A Brief History of Time", "Stephen Hawking", "Science",
                "An accessible introduction to cosmology and the universe.", false, true, null));
        add(new Book(4, "The Hobbit", "J. R. R. Tolkien", "Fantasy",
                "A reluctant hobbit joins an unexpected adventure.", true, true,
                "The Hobbit — Digital preview\n\nIn a hole in the ground there lived a hobbit.\n\nThis demonstration reader contains a short preview. Add licensed content before production use."));
        add(new Book(5, "Clean Code", "Robert C. Martin", "Technology",
                "Principles and practices for writing readable software.", false, true, null));
    }

    private void add(Book book) {
        books.put(book.id(), book);
    }

    public List<Book> findAll() {
        return books.values().stream().sorted((a, b) -> Long.compare(a.id(), b.id())).toList();
    }

    public Book findById(long id) {
        Book book = books.get(id);
        if (book == null) throw new BookNotFoundException(id);
        return book;
    }

    public synchronized Book borrow(long id) {
        Book book = findById(id);
        if (!book.available()) throw new BookUnavailableException("This book is already borrowed.");
        Book updated = new Book(book.id(), book.title(), book.author(), book.category(), book.description(),
                book.digital(), false, book.digitalContent());
        books.put(id, updated);
        return updated;
    }

    public synchronized Book returnBook(long id) {
        Book book = findById(id);
        if (book.available()) throw new BookUnavailableException("This book is not currently borrowed.");
        Book updated = new Book(book.id(), book.title(), book.author(), book.category(), book.description(),
                book.digital(), true, book.digitalContent());
        books.put(id, updated);
        return updated;
    }

    public String digitalContent(long id) {
        Book book = findById(id);
        if (!book.digital() || book.digitalContent() == null) {
            throw new BookUnavailableException("Digital content is not available for this title.");
        }
        return book.digitalContent();
    }
}
