package com.example.training.book.service;

import com.example.training.book.exception.BookNotFoundException;
import com.example.training.book.model.Book;
import com.example.training.book.repository.BookRepository;
import com.example.training.book.repository.InMemoryBookRepository;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Optional;


public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public Collection<Book> fetchAllBooks() {
        return bookRepository.fetchAllBooks();
    }


    public Optional<Book> getBookByReference(String reference) {
        return bookRepository.getBookByReference(reference);
    }


    public Optional<Book> addBook(Book book) {
        return bookRepository.addBook(book);
    }


    public Either<BookNotFoundException, Book> updateBook(Book book) {
        return bookRepository.updateBook(book);
    }


    public Either<BookNotFoundException, Book> deleteBook(Book book) {
        return bookRepository.deleteBook(book);
    }
}
