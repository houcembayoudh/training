package com.example.training.book.repository;

import com.example.training.book.dtao.BookDtao;
import com.example.training.book.dtao.DBBookRepositoryDtao;
import com.example.training.book.exception.*;
import com.example.training.book.model.Book;
import io.vavr.control.Either;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DBBookRepository implements BookRepository{

    private final DBBookRepositoryDtao dbBookRepositoryDtao;
    public DBBookRepository(DBBookRepositoryDtao dbBookRepositoryDtao){
        this.dbBookRepositoryDtao = dbBookRepositoryDtao;
    }


    @Override
    public Collection<Book> fetchAllBooks() {

        return dbBookRepositoryDtao
                .findAll()
                .stream()
                .map(BookDtao::toModel)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<Book> getBookByReference(String reference) {
        return dbBookRepositoryDtao
                .findById(reference)
                .map(BookDtao::toModel);
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
        final var savedBook
                = dbBookRepositoryDtao
                .save(new BookDtao(book))
                .toModel();
        return
                new BookSavingRecord(
                        savedBook
                );
    }

    @Override
    public Either<BookNotFoundException, Book> updateBook(Book book) {

        final var bookExist =
                dbBookRepositoryDtao.existsById(book.getReference());

        if (!bookExist){
            return  Either.left(new BookNotFoundException(("Book does not exist")));
        }
        final var updatedBook =
                dbBookRepositoryDtao.save(new BookDtao(book)).toModel();

        return Either.right(updatedBook);
    }

    @Override
    public Either<BookNotFoundException, Book> deleteBookByReference(String bookReference) {

        final var searchedBook =  dbBookRepositoryDtao.findById(bookReference)
                .map(BookDtao::toModel);

        searchedBook
                .ifPresent(
                        it -> dbBookRepositoryDtao.deleteById(it.getReference())
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
                                        bookSavingRecord ->
                                                bookSavingRecord.savingException().isEmpty()
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
        dbBookRepositoryDtao.deleteAll();
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
                            ("BookDtao Name is Either null or empty"));
        }
        //validReference
        if (invalidBookReference(book.getReference())) {
            return Optional.ofNullable(
                    new BookReferenceIsNotValidException
                            ("BookDtao Reference is Either null or empty"));
        }
        //bookExists
        if (bookAlreadyExists(book)) {
            return Optional.ofNullable(
                    new BookAlreadyExistsException
                            ("BookDtao Already exists"));
        }
        return Optional.empty();
    }
}
