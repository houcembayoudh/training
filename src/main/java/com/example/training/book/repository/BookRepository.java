package com.example.training.book.repository;

import com.example.training.book.exception.BookNotFoundException;
import com.example.training.book.model.Book;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Optional;

public interface BookRepository {

    public Collection<Book> fetchAllBooks();
    public Optional<Book> getBookByReference(String reference);
    public Optional<Book> addBook(Book book);
    public Either<BookNotFoundException, Book> updateBook(Book book);
    public Either<BookNotFoundException, Book> deleteBook(Book book);

}
