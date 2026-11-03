package com.elibrary.book;

public record Book(
        long id,
        String title,
        String author,
        String category,
        String description,
        boolean digital,
        boolean available,
        String digitalContent
) { }
