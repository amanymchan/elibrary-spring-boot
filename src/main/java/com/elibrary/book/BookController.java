package com.elibrary.book;

import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String catalog(@RequestParam(defaultValue = "") String q,
                          @RequestParam(defaultValue = "all") String filter,
                          @RequestParam(defaultValue = "title") String sort,
                          Model model) {
        String normalizedFilter = switch (filter) {
            case "available" -> "available";
            case "digital" -> "digital";
            default -> "all";
        };
        String normalizedSort = switch (sort) {
            case "author" -> "author";
            default -> "title";
        };

        List<Book> books = bookService.filterAndSort(q, normalizedFilter, normalizedSort);

        model.addAttribute("books", books);
        model.addAttribute("query", q);
        model.addAttribute("filter", normalizedFilter);
        model.addAttribute("sort", normalizedSort);
        model.addAttribute("count", books.size());
        return "catalog";
    }

    @PostMapping("/books/{id}/borrow")
    public String borrow(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.borrow(id);
            redirectAttributes.addFlashAttribute("notice", "Book borrowed — happy reading!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/books/{id}/return")
    public String returnBook(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.returnBook(id);
            redirectAttributes.addFlashAttribute("notice", "Book returned. Thanks for sharing!");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/books/{id}/digital")
    public String readDigital(@PathVariable long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("content", bookService.digitalContent(id));
        return "reader";
    }
}
