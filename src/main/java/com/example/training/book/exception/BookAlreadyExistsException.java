package com.example.training.book.exception;

public class BookAlreadyExistsException extends BookException{

        public BookAlreadyExistsException() {
            super();
        }

        public BookAlreadyExistsException (final String message) {
            super(message);
        }

        public BookAlreadyExistsException (final String message, final Throwable cause) {
            super(message, cause);
        }

    }

