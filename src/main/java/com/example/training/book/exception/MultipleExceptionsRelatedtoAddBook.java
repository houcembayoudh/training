package com.example.training.book.exception;

public class MultipleExceptionsRelatedtoAddBook extends RuntimeException{

        public MultipleExceptionsRelatedtoAddBook() {
            super();
        }

        public MultipleExceptionsRelatedtoAddBook(final String message) {
            super(message);
        }

        public MultipleExceptionsRelatedtoAddBook(final String message, final Throwable cause) {
            super(message, cause);
        }

}
