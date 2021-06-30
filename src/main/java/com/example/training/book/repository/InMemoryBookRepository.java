package com.example.training.book.repository;

import com.example.training.book.exception.*;
import com.example.training.book.model.Book;
import io.vavr.control.Either;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class InMemoryBookRepository implements BookRepository {

    List<Book> bookList = new ArrayList<>();

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
    public final BookSavingRecord addBook(final Book book) {
        var containExceptions = checkBookValidity(book);
        if (containExceptions.isPresent()) {
            return
                    new BookSavingRecord(
                            book,
                            containExceptions.get()
                    );
        }
        bookList.add(book);
        return
                new BookSavingRecord(
                        book
                );
    }

    @Override
    public Either<BookNotFoundException, Book> updateBook(Book book) {
       final var bookExist = bookList.stream()
                .anyMatch(
                        it -> it
                                .getReference()
                                .equals(book.getReference()));
       if (!bookExist){
           return  Either.left(new BookNotFoundException(("Book doesn not exist")));
       }
        bookList = bookList.stream()
               .filter(Predicate.not(it -> it.getReference()
               .equals(book.getReference())))
               .collect(Collectors.toList());
        bookList.add(book);

        return Either.right(book);
    }

    @Override
    public Either<BookNotFoundException, Book> deleteBookByReference(String bookReference) {


     final var searchedBook =  bookList.stream()
              .filter(it -> it.getReference().equals(bookReference))
              .findFirst();

     searchedBook
             .ifPresent(
                     it -> bookList = bookList.stream()
                     .filter( Predicate.not(book -> book.equals(it)))
                     .collect(Collectors.toList())
             );
        return searchedBook
                .<Either<BookNotFoundException, Book>>map(Either::right)
                .orElseGet(
                        ()
                        ->
                        Either.left(new BookNotFoundException("Book not found"))
                );

    }



    @Override
    public SavingBooksRecord saveAll(final Collection<Book> books) {
        final var savingResults =
                books
                        .stream()
                        .map(this::addBook)// never parallelize this stream, it is not pure
                        .collect(
                                Collectors.partitioningBy(
                                        bookSavingRecord -> bookSavingRecord.savingException().isEmpty()
                                )
                        );

        final var savedBooks =
                savingResults.get(true)
                        .stream()
                        .map(BookSavingRecord::book)
                        .collect(Collectors.toUnmodifiableList());

        final var notSavedBooks =
                savingResults.get(false)
                        .stream()
                        .filter(it -> it.savingException().isPresent())
                        .map(it ->
                                new SavingBooksRecord.UnsavedBook(
                                        it.book(),
                                        it.savingException()
                                                .get()
                                )
                        )
                        .collect(Collectors.toUnmodifiableList());

        return
                new SavingBooksRecord(
                        savedBooks,
                        notSavedBooks
                );


    }

    @Override
    public void removeAll() {
    bookList = new ArrayList<>();
    }



    private final boolean invalidBookName(String bookName) {
        return Objects.isNull(bookName) || bookName.isEmpty();
    }

    private final boolean invalidBookReference(String bookReference) {
        return Objects.isNull(bookReference) || bookReference.isEmpty();
    }

    private final boolean bookAlreadyExists(Book book) {
        return getBookByReference(book.getReference()).isPresent();
    }

    private Optional<? extends BookException> checkBookValidity(Book book) {
        //validName
        if (invalidBookName(book.getBookName())) {
            return Optional.ofNullable(
                    new BookNameIsNotValidException
                            ("Book Name is Either null or empty"));
        }
        //validReference
        if (invalidBookReference(book.getReference())) {
            return Optional.ofNullable(
                    new BookReferenceIsNotValidException
                            ("Book Reference is Either null or empty"));
        }
        //bookExists
        if (bookAlreadyExists(book)) {
            return Optional.ofNullable(
                    new BookAlreadyExistsException
                            ("Book Already exists"));
        }
        return Optional.empty();
    }
}



