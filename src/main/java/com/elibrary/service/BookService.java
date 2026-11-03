package com.elibrary.service;

import com.elibrary.exception.BookNotFoundException;
import com.elibrary.exception.BookUnavailableException;
import com.elibrary.model.Book;
import com.elibrary.model.BorrowRecord;
import com.elibrary.model.User;
import com.elibrary.repository.BookRepository;
import com.elibrary.repository.BorrowRecordRepository;
import com.elibrary.repository.CategoryRepository;
import com.elibrary.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    private static final String DEMO_USER_EMAIL = "guest@openshelf.local";

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository,
                       UserRepository userRepository, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> findDigitalBooks() {
        return bookRepository.findAll().stream().filter(Book::isDigital)
                .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle())).toList();
    }

    @Transactional(readOnly = true)
    public List<BorrowRecord> findBorrowedBooks() {
        User user = findDemoUser();
        return borrowRecordRepository.findByUserIdAndStatusOrderByBorrowedOnDesc(
                user.getId(), BorrowRecord.Status.BORROWED);
    }

    @Transactional
    public Book borrow(long id) {
        Book book = findById(id);
        if (!book.isAvailable()) {
            throw new BookUnavailableException("This book is already borrowed.");
        }
        User user = findDemoUser();
        LocalDate today = LocalDate.now();
        borrowRecordRepository.save(new BorrowRecord(book, user, today, today.plusDays(14)));
        book.setAvailable(false);
        return bookRepository.save(book);
    }

    @Transactional
    public Book returnBook(long id) {
        Book book = findById(id);
        if (book.isAvailable()) {
            throw new BookUnavailableException("This book is not currently borrowed.");
        }
        BorrowRecord record = borrowRecordRepository
                .findFirstByBookIdAndStatusOrderByBorrowedOnDesc(id, BorrowRecord.Status.BORROWED)
                .orElseThrow(() -> new BookUnavailableException("No active borrow record exists for this book."));
        record.markReturned(LocalDate.now());
        borrowRecordRepository.save(record);
        book.setAvailable(true);
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public String digitalContent(long id) {
        Book book = findById(id);
        if (!book.isDigital() || book.getDigitalContent() == null || book.getDigitalContent().isBlank()) {
            throw new BookUnavailableException("Digital content is not available for this title.");
        }
        return book.getDigitalContent();
    }

    @Transactional(readOnly = true)
    public Book findById(long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    private User findDemoUser() {
        return userRepository.findByEmail(DEMO_USER_EMAIL)
                .orElseThrow(() -> new IllegalStateException("Demo library user is missing."));
    }

    @Transactional(readOnly = true)
    public List<Book> filterAndSort(String query, String filter, String sort) {
        String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        String normalizedFilter = switch (filter == null ? "all" : filter) {
            case "available" -> "available";
            case "digital" -> "digital";
            default -> "all";
        };
        String normalizedSort = "author".equals(sort) ? "author" : "title";
        return bookRepository.findAll().stream()
                .filter(book -> normalizedQuery.isEmpty() || containsText(book, normalizedQuery))
                .filter(book -> normalizedFilter.equals("all")
                        || (normalizedFilter.equals("available") && book.isAvailable())
                        || (normalizedFilter.equals("digital") && book.isDigital()))
                .sorted((a, b) -> normalizedSort.equals("author")
                        ? a.getAuthor().compareToIgnoreCase(b.getAuthor())
                        : a.getTitle().compareToIgnoreCase(b.getTitle()))
                .toList();
    }

    private boolean containsText(Book book, String query) {
        return book.getTitle().toLowerCase(Locale.ROOT).contains(query)
                || book.getAuthor().toLowerCase(Locale.ROOT).contains(query)
                || book.getCategory().getName().toLowerCase(Locale.ROOT).contains(query)
                || (book.getDescription() != null && book.getDescription().toLowerCase(Locale.ROOT).contains(query));
    }
}
