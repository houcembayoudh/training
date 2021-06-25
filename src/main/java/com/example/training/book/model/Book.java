package com.example.training.book.model;


public class Book {
    final String BookName;
    final String Summary;


    public Book(String bookName, String summary) {
        BookName = bookName;
        Summary = summary;
    }

    public String getBookName() {
        return BookName;
    }

    public String getSummary() {
        return Summary;
    }

    @Override
    public String toString() {
        return "Book{" +
                "BookName='" + BookName + '\'' +
                ", Summary='" + Summary + '\'' +
                '}';
    }
}
