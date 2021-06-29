package com.example.training.book.exception;

public class BookNameIsNotValidException extends BookException{
    public BookNameIsNotValidException() {
    }

    public BookNameIsNotValidException(String message) {
        super(message);
    }

    public BookNameIsNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

}
