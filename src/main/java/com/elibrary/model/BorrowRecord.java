package com.elibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {
    public enum Status { BORROWED, RETURNED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate borrowedOn;

    @Column(nullable = false)
    private LocalDate dueOn;

    private LocalDate returnedOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    protected BorrowRecord() { }

    public BorrowRecord(Book book, User user, LocalDate borrowedOn, LocalDate dueOn) {
        this.book = book;
        this.user = user;
        this.borrowedOn = borrowedOn;
        this.dueOn = dueOn;
        this.status = Status.BORROWED;
    }

    public void markReturned(LocalDate date) {
        this.returnedOn = date;
        this.status = Status.RETURNED;
    }

    public Long getId() { return id; }
    public Book getBook() { return book; }
    public User getUser() { return user; }
    public LocalDate getBorrowedOn() { return borrowedOn; }
    public LocalDate getDueOn() { return dueOn; }
    public LocalDate getReturnedOn() { return returnedOn; }
    public Status getStatus() { return status; }
}
