package com.example.training.book.exception;

public final class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
        super();
    }

    public BookNotFoundException(final String message) {
        super(message);
    }

    public BookNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}