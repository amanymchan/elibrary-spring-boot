package com.elibrary.book;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Iterable<Book> listBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable long id) {
        return bookService.findById(id);
    }

    @PostMapping("/{id}/borrow")
    public Book borrowBook(@PathVariable long id) {
        return bookService.borrow(id);
    }

    @PostMapping("/{id}/return")
    public Book returnBook(@PathVariable long id) {
        return bookService.returnBook(id);
    }

    @GetMapping("/{id}/digital")
    public Map<String, String> readDigitalContent(@PathVariable long id) {
        return Map.of("content", bookService.digitalContent(id));
    }
}
