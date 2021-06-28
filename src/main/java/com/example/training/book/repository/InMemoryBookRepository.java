package com.example.training.book.repository;

import com.example.training.book.exception.BookNotFoundException;
import com.example.training.book.model.Book;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class InMemoryBookRepository implements BookRepository {

    List<Book> bookList = new ArrayList<>() ;

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
    public Either<BookNotFoundException, Book> updateBook(Book book) {

            int indexSearch = bookList.indexOf(book);
            if (indexSearch != -1) {
                bookList.set(indexSearch, book);
                return Either.right(book);
            }else{
                return Either.left(new BookNotFoundException("Book not found"));
            }

    }

    @Override
    public Either<BookNotFoundException, Book> deleteBook(Book book) {

        int indexSearch = bookList.indexOf(book);
        if (indexSearch != -1) {
            Book removed = bookList.remove(indexSearch);
            return Either.right(removed);
        }else{
            return Either.left(new BookNotFoundException("Book not found"));
        }

    }





}

