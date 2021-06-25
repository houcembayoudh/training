package com.example.training.book.service;

import com.example.training.book.model.Book;
import com.example.training.book.repository.InMemoryBookRepository;
import io.vavr.control.Either;

import java.util.Collection;
import java.util.Optional;


public class BookService {

    private final InMemoryBookRepository inMemoryBookRepository;

    public BookService(InMemoryBookRepository inMemoryBookRepository) {
        this.inMemoryBookRepository = inMemoryBookRepository;
    }


    public Collection<Book> fetchAllBooks() {
        return inMemoryBookRepository.fetchAllBooks();
    }


    public Optional<Book> getBookByReference(String reference) {
        return inMemoryBookRepository.getBookByReference(reference);
    }


    public Optional<Book> addBook(Book book) {
        return inMemoryBookRepository.addBook(book);
    }


    public Optional<Book> updateBook(Book book) {
        return inMemoryBookRepository.updateBook(book);
    }


    public Either<Exception, Book> deleteBook(Book book) {
        return inMemoryBookRepository.deleteBook(book);
    }
}
