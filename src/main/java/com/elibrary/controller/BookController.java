package com.elibrary.controller;

import com.elibrary.exception.BookNotFoundException;
import com.elibrary.exception.BookUnavailableException;
import com.elibrary.model.Book;
import com.elibrary.service.BookService;
import java.util.List;
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
    public String home() {
        return "redirect:/book";
    }

    @GetMapping("/book")
    public String books(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "filter", defaultValue = "all") String filter,
            @RequestParam(value = "sort", defaultValue = "title") String sort,
            Model model) {
        List<Book> books = bookService.filterAndSort(query, filter, sort);
        model.addAttribute("books", books);
        model.addAttribute("count", books.size());
        model.addAttribute("query", query == null ? "" : query);
        model.addAttribute("filter", filter);
        model.addAttribute("sort", sort);
        return "books";
    }

    @GetMapping("/borrowed-books")
    public String borrowedBooks(Model model) {
        model.addAttribute("borrowRecords", bookService.findBorrowedBooks());
        return "borrowed-books";
    }

    @GetMapping("/digital")
    public String digitalBooks(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "filter", defaultValue = "digital") String filter,
            @RequestParam(value = "sort", defaultValue = "title") String sort,
            Model model) {
        List<Book> books = bookService.filterAndSort(query, filter, sort);
        model.addAttribute("books", books);
        model.addAttribute("count", books.size());
        model.addAttribute("query", query == null ? "" : query);
        model.addAttribute("filter", filter);
        model.addAttribute("sort", sort);
        return "digital";
    }

    @GetMapping("/books/{id}/digital")
    public String digitalPreview(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Book book = bookService.findById(id);
            if (!book.isDigital()) {
                throw new BookUnavailableException("Digital content is not available for this title.");
            }
            model.addAttribute("book", book);
            model.addAttribute("content", bookService.digitalContent(id));
            return "reader";
        } catch (BookNotFoundException | BookUnavailableException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/digital";
        }
    }

    @PostMapping("/books/{id}/borrow")
    public String borrow(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.borrow(id);
            redirectAttributes.addFlashAttribute("notice", "Book borrowed — happy reading!");
        } catch (BookUnavailableException | BookNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/book";
    }

    @PostMapping("/books/{id}/return")
    public String returnBook(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.returnBook(id);
            redirectAttributes.addFlashAttribute("notice", "Book returned. Thanks for sharing!");
        } catch (BookUnavailableException | BookNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/borrowed-books";
    }
}
