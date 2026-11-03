package com.elibrary.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

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

    public Book(String title, String author, Category category, String description,
                boolean digital, boolean available, String digitalContent) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.digital = digital;
        this.available = available;
        this.digitalContent = digitalContent;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isDigital() { return digital; }
    public void setDigital(boolean digital) { this.digital = digital; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String getDigitalContent() { return digitalContent; }
    public void setDigitalContent(String digitalContent) { this.digitalContent = digitalContent; }
}
