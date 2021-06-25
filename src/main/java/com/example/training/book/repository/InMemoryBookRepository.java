package com.example.training.book.repository;

import com.example.training.book.model.Book;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class InMemoryBookRepository implements BookRepository {

    List<Book> bookList = new ArrayList<Book>() ;

    public InMemoryBookRepository() {
        this.bookList .add(new Book("book 1", "ref001"));
        this.bookList .add(new Book("book 2", "ref002"));
        this.bookList .add(new Book("book 3", "ref003"));
    }

    @Override
    public Collection<Book> fetchAllBooks() {

        return bookList;
    }

    @Override
    public Optional<Book> getBookByReference(String reference) {
        return bookList
                .stream()
                .filter(book -> book.getReference().equals(reference))
                .findFirst();
    }

    @Override
    public Optional<Book> addBook(Book book) {
        bookList.add(book);
        return getBookByReference(book.getReference());
    }

    @Override
    public Optional<Book> updateBook(Book book) {

        int indexSearch = bookList.indexOf(book);
        bookList.set(indexSearch, book);

        return Optional.empty();
    }

    @Override
    public Either<Exception, Book> deleteBook(Book book) {

        try {
            int indexSearch = bookList.indexOf(book);
            Book updated = bookList.remove(indexSearch);
            return Either.right(updated);
        } catch (Exception e) {
            return Either.left(e);
        }

    }
}
