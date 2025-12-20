package com.example.readify.Models;

import java.time.LocalDate;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private String publisher;
    private String isbn;
    private int totalCopies;
    private String status;
    private LocalDate dateAdded;

    public Book(String bookId, String title, String author, String category,
                String publisher, String isbn, int totalCopies,
                String status, LocalDate dateAdded) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.status = status;
        this.dateAdded = dateAdded;
    }

    public String getBookId() {
        return bookId;
    }
}
