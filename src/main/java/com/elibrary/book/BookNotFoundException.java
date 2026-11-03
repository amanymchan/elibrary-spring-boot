package com.elibrary.book;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends EntityNotFoundException {
    public BookNotFoundException(long id) {
        super("Book " + id + " was not found.");
    }
}
