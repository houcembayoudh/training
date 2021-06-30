package com.example.training.book.service;

import com.example.training.book.exception.BookNotFoundException;
import com.example.training.book.repository.BookRepository;
import com.example.training.book.model.Book;
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


    public BookRepository.BookSavingRecord addBook(Book book) {
        return bookRepository.addBook(book);
    }


    public Either<BookNotFoundException, Book> updateBook(Book book) {
        return bookRepository.updateBook(book);
    }


    public BookRepository.SavingBooksRecord saveAll(Collection<Book> bookList) {
        return bookRepository.saveAll(bookList);
    }

    public Either<BookNotFoundException, Book> deleteBookByReference(String bookReference) {
        return bookRepository.deleteBookByReference(bookReference);
    }


    public void removeAll() {
        bookRepository.removeAll();
    }


}
