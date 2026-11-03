package com.elibrary.book;

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
        seedData();
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<Book> filterAndSort(String query, String filter, String sort) {
        String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        String normalizedFilter = switch (filter) {
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

    @Transactional
    public Book borrow(long id) {
        Book book = findById(id);
        if (!book.isAvailable()) {
            throw new BookUnavailableException("This book is already borrowed.");
        }
        User user = userRepository.findByEmail(DEMO_USER_EMAIL)
                .orElseThrow(() -> new IllegalStateException("Demo library user is missing."));
        var today = java.time.LocalDate.now();
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
        record.markReturned(java.time.LocalDate.now());
        borrowRecordRepository.save(record);
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
                || book.getCategory().getName().toLowerCase(Locale.ROOT).contains(query)
                || (book.getDescription() != null
                    && book.getDescription().toLowerCase(Locale.ROOT).contains(query));
    }

    private void seedData() {
        User demoUser = userRepository.findByEmail(DEMO_USER_EMAIL)
                .orElseGet(() -> userRepository.save(new User("Guest Reader", DEMO_USER_EMAIL)));

        if (bookRepository.count() > 0) {
            return;
        }

        Category fiction = categoryRepository.save(new Category("Fiction", "Fiction and literary classics."));
        Category science = categoryRepository.save(new Category("Science", "Science and the natural world."));
        Category fantasy = categoryRepository.save(new Category("Fantasy", "Fantasy and adventure."));
        Category technology = categoryRepository.save(new Category("Technology", "Computing and software."));

        bookRepository.saveAll(List.of(
                new Book("The Secret Garden", "Frances Hodgson Burnett", fiction,
                        "A classic story of friendship and a hidden garden.", true, true,
                        "The Secret Garden — Digital preview\n\nMary Lennox discovers a locked garden and, with new friends, brings it back to life.\n\nThis demonstration reader contains a short preview. Add licensed content before production use."),
                new Book("Pride and Prejudice", "Jane Austen", fiction,
                        "A witty novel of manners, family, and first impressions.", true, true,
                        "Pride and Prejudice — Digital preview\n\nIt is a truth universally acknowledged, that a single person in possession of a good fortune, must be in want of a partner.\n\nThis demonstration reader contains a short preview. Add licensed content before production use."),
                new Book("A Brief History of Time", "Stephen Hawking", science,
                        "An accessible introduction to cosmology and the universe.", false, true, null),
                new Book("The Hobbit", "J. R. R. Tolkien", fantasy,
                        "A reluctant hobbit joins an unexpected adventure.", true, true,
                        "The Hobbit — Digital preview\n\nIn a hole in the ground there lived a hobbit.\n\nThis demonstration reader contains a short preview. Add licensed content before production use."),
                new Book("Clean Code", "Robert C. Martin", technology,
                        "Principles and practices for writing readable software.", false, true, null)
        ));
    }
}
