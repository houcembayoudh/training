package com.example.training.book.repository;

import com.example.training.book.exception.BookException;
import com.example.training.book.exception.BookNotFoundException;
import com.example.training.book.model.Book;
import io.vavr.control.Either;
import lombok.Data;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public interface BookRepository {

    public Collection<Book> fetchAllBooks();

    public Optional<Book> getBookByReference(String reference);

    public BookSavingRecord addBook(Book book);

    public Either<BookNotFoundException, Book> updateBook(Book book);



    public SavingBooksRecord saveAll(Collection<Book> books);

    public Either<BookNotFoundException, Book> deleteBookByReference(String bookReference);

    public void removeAll();

    @Data
    public static final class SavingBooksRecord {
        private final Collection<Book> savedBooks;
        private final Collection<UnsavedBook> unsavedBooks;

        @Data
        public static final class UnsavedBook {
            private final Book book;
            private final BookException reason;
        }
    }

    public static final class BookSavingRecord {
        private final Book book;
        private final BookException savingException;

        public BookSavingRecord(final Book book) {
            this.book = book;
            this.savingException = null;
        }

        public BookSavingRecord(final Book book, final BookException savingException) {
            this.book = book;
            this.savingException = savingException;
        }

        public final Book book() {
            return this.book;
        }

        public final Optional<? extends BookException> savingException() {
            return
                    Objects.nonNull(this.savingException)
                            ? Optional
                            .ofNullable(this.savingException)
                            : Optional.empty();
        }
    }

}
