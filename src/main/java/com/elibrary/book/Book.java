package com.elibrary.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String category;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean digital;

    @Column(nullable = false)
    private boolean available;

    @Column(length = 4000)
    private String digitalContent;

    protected Book() {
    }

    public Book(String title, String author, String category, String description,
                boolean digital, boolean available, String digitalContent) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.digital = digital;
        this.available = available;
        this.digitalContent = digitalContent;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getDigitalContent() {
        return digitalContent;
    }

    public void setDigitalContent(String digitalContent) {
        this.digitalContent = digitalContent;
    }
}
