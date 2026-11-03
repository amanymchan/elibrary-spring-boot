package com.elibrary.config;

import com.elibrary.model.Book;
import com.elibrary.model.Category;
import com.elibrary.model.User;
import com.elibrary.repository.BookRepository;
import com.elibrary.repository.CategoryRepository;
import com.elibrary.repository.UserRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Loads a small, repeatable demo catalog for a fresh database. */
@Component
public class DemoDataInitializer implements CommandLineRunner {
    private static final String DEMO_USER_EMAIL = "guest@openshelf.local";

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public DemoDataInitializer(BookRepository bookRepository, CategoryRepository categoryRepository,
                               UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        userRepository.findByEmail(DEMO_USER_EMAIL)
                .orElseGet(() -> userRepository.save(new User("Guest Reader", DEMO_USER_EMAIL)));

        if (bookRepository.count() > 0) {
            return;
        }

        Category fiction = categoryRepository.save(new Category("Fiction", "Stories and literary classics"));
        Category fantasy = categoryRepository.save(new Category("Fantasy", "Imaginative worlds and adventures"));
        Category science = categoryRepository.save(new Category("Science", "Ideas that explain our world"));
        Category technology = categoryRepository.save(new Category("Technology", "Computing and modern technology"));

        bookRepository.saveAll(List.of(
                new Book("Pride and Prejudice", "Jane Austen", fiction,
                        "A sparkling comedy of manners about first impressions, family, and love.", true, true,
                        "It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.\n\nThis public-domain opening is provided as a short demonstration preview."),
                new Book("The Secret Garden", "Frances Hodgson Burnett", fiction,
                        "A lonely child discovers a locked garden and the healing power of friendship.", true, true,
                        "When Mary Lennox was sent to Misselthwaite Manor to live with her uncle everybody said she was the most disagreeable-looking child ever seen.\n\nThis public-domain opening is provided as a short demonstration preview."),
                new Book("The Hobbit", "J. R. R. Tolkien", fantasy,
                        "Bilbo Baggins is swept into an unexpected journey beyond the Shire.", false, true, null),
                new Book("A Brief History of Time", "Stephen Hawking", science,
                        "A popular introduction to the big questions of cosmology and time.", true, true,
                        "This catalog entry demonstrates digital availability. A licensed reading sample would appear here."),
                new Book("The Pragmatic Programmer", "David Thomas and Andrew Hunt", technology,
                        "Practical lessons for building software with care and adaptability.", false, true, null),
                new Book("Frankenstein", "Mary Shelley", fiction,
                        "Mary Shelley’s classic novel explores ambition, responsibility, and humanity.", true, true,
                        "You will rejoice to hear that no disaster has accompanied the commencement of an enterprise which you have regarded with such evil forebodings.\n\nThis public-domain opening is provided as a short demonstration preview.")
        ));
    }
}
