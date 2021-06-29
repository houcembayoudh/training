package com.example.training.book.exception;

public class BookReferenceIsNotValidException extends BookException{
    public BookReferenceIsNotValidException() {
    }

    public BookReferenceIsNotValidException(String message) {
        super(message);
    }

    public BookReferenceIsNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
