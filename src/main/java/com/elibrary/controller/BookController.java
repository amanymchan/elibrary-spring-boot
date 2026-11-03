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
    public String books(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("count", books.size());
        return "books";
    }

    @GetMapping("/borrowed-books")
    public String borrowedBooks(Model model) {
        model.addAttribute("borrowRecords", bookService.findBorrowedBooks());
        return "borrowed-books";
    }

    @GetMapping("/digital")
    public String digitalBooks(Model model) {
        model.addAttribute("books", bookService.findDigitalBooks());
        return "digital";
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
