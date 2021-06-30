package com.example.training.book.db;

import com.example.training.book.dtao.DBBookRepositoryDtao;
import com.example.training.book.endpoint.rest.BookEndPoint;
import com.example.training.book.model.Book;
import com.example.training.book.repository.DBBookRepository;
import com.example.training.book.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

@SpringBootTest
public class DbBookDtaoEndPointTestUnit {

    BookEndPoint bookEndPoint;
    BookService bookService;


    public DbBookDtaoEndPointTestUnit(@Autowired DBBookRepositoryDtao dbBookRepositoryDtao) {
        var bookRepository = new DBBookRepository(dbBookRepositoryDtao);
        bookService = new BookService(bookRepository);
        bookEndPoint = new BookEndPoint(bookService);
    }

    @BeforeEach
    public void init(){
        bookService.removeAll();
    }


    @Test
    public void addBook_insert_book_expect_200_same_book() {
        Book book = new Book("Omar", "Sportage");
        final var result =
                bookEndPoint
                        .addBook(book)
                        ;
        Assertions
                .assertEquals(
                        ResponseEntity.ok(book)
                        , result);
    }

    @Test
    public void addBook_insert_invalid_book_expect_invalid_book_exception() {
        Book book = new Book("", "null");
        final var result =
                bookEndPoint
                        .addBook(book);
        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        HttpStatus.BAD_REQUEST,
                        result.getStatusCode()
                ),
                () -> Assertions.assertTrue(
                        result.getHeaders().containsKey("error")
                )
        );
    }
    @Test
    public void addBook_insert_invalid_book_reference_expect_invalid_book_exception() {
        Book book = new Book("aa", "");
        final var result =
                bookEndPoint
                        .addBook(book);
        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        HttpStatus.BAD_REQUEST,
                        result.getStatusCode()
                ),
                () -> Assertions.assertTrue(
                        result.getHeaders().containsKey("error")
                )
        );
    }


    @Test
    public void fetchAllBooks_insert_book_expect_200_list_of_book() {
        Book book = new Book("Omar", "Sportage");
                bookEndPoint
                        .addBook(book);
        final var result =
                bookEndPoint.fetchAllBooks();
        Assertions
                .assertEquals(
                        ResponseEntity.ok(List.of(book))
                        , result);
    }


    @Test
    public void getBookByReference_insert_book_expect_200_fetch_same_book() {
        Book book = new Book("Omar", "Sportage");
        bookEndPoint
                .addBook(book);

        final var result =
                bookEndPoint.getBookByReference(book.getReference());
        Assertions
                .assertEquals(
                        ResponseEntity.ok(book)
                        , result);
    }

    @Test
    public void getBookByReference_insert_invalid_reference_expect_book_not_found_exception() {
        Book book = new Book("Omar", "Sportage");
        bookEndPoint
                .addBook(book);

        final var result =
                bookEndPoint.getBookByReference("book.getReference()");
        Assertions
                .assertEquals(
                        404
                        , result.getStatusCode().value());
    }

    @Test
    public void updateBook_insert_book_expect_200_updated_book() {
        Book book = new Book("Omar", "Sportage");
        bookEndPoint
                .addBook(book);
        Book updateBook = new  Book("Omar007", "Sportage");
        final var result =
                bookEndPoint.updateBook(updateBook);
        Assertions
                .assertEquals(
                        ResponseEntity.ok(updateBook)
                        , result);
    }

    @Test
    public void updateBook_insert_invalid_book_expect_book_not_found_exception() {
        Book book = new Book("Omar", "Sportage");
        bookEndPoint
                .addBook(book);
        Book updateBook = new  Book("Omar007", "Sportage11");
        final var result =
                bookEndPoint.updateBook(updateBook);
        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        HttpStatus.BAD_REQUEST,
                        result.getStatusCode()
                ),
                () -> Assertions.assertTrue(
                        result.getHeaders().containsKey("error")
                ));
    }

    @Test
    public void deleteByReference_insert_book_expect_200() {
        Book book = new Book("Omar", "Sportage");
        Book book2 = new Book("Omar22", "Sportage2");
        bookEndPoint
                .addBook(book);
        bookEndPoint
                .addBook(book2);
        bookEndPoint.deleteBookByReference("Sportage2");
        final var result =
                bookEndPoint.fetchAllBooks().getBody().size();
        Assertions
                .assertEquals(
                        1
                        , result);
    }

    @Test
    public void deleteByReference_insert_wrong_reference_expect_not_found_exception() {
        Book book = new Book("Omar", "Sportage");
        Book book2 = new Book("Omar22", "Sportage2");
        bookEndPoint
                .addBook(book);
        bookEndPoint
                .addBook(book2);

        final var result =
                bookEndPoint.deleteBookByReference("Sportage3");
        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        HttpStatus.BAD_REQUEST,
                        result.getStatusCode()
                ),
                () -> Assertions.assertTrue(
                        result.getHeaders().containsKey("error")
                )
        );
    }

    @Test
    public void saveAll_insert_collection_of_valid_books_expect_all_saved() {
        Collection<Book> books =List.of(new Book("Omar", "Bayoudh")
        ,new Book("Omar", "Ben Romdhan")
        ,new Book("Omar", "Octavia")
        ,new Book("Omar", "Sportage"));
         final var result = bookEndPoint.saveAllBooks(books).getBody();
          Assertions
                .assertAll(
                        () -> Assertions
                                        .assertEquals(4,
                                                result.getSavedBooks().size()),
                                    () -> Assertions
                                            .assertEquals(0,
                                                    result.getUnsavedBooks().size())
                );
    }


    @Test
    public void saveAll_insert_collection_of_valid_books_with_one_duplicate_expect_3_saved_and_1_unsaved() {
        Collection<Book> books =List.of(new Book("Omar", "Bayoudh")
                ,new Book("Omar", "Ben Romdhan")
                ,new Book("Omar", "Octavia")
                ,new Book("Omar", "Bayoudh"));
        final var result = bookEndPoint.saveAllBooks(books).getBody();
        Assertions
                .assertAll(
                        () -> Assertions
                                .assertEquals(3,
                                        result.getSavedBooks().size()),
                        () -> Assertions
                                .assertEquals(1,
                                        result.getUnsavedBooks().size())
                );
    }

    @Test
    public void saveAll_insert_collection_of_valid_books_with_one_invalid_book_expect_3_saved_and_1_unsaved() {
        Collection<Book> books =List.of(new Book("Omar", "Bayoudh")
                ,new Book("Omar", "Ben Romdhan")
                ,new Book("Omar", "Octavia")
                ,new Book("Omar", ""));
        final var result = bookEndPoint.saveAllBooks(books).getBody();
        Assertions
                .assertAll(
                        () -> Assertions
                                .assertEquals(3,
                                        result.getSavedBooks().size()),
                        () -> Assertions
                                .assertEquals(1,
                                        result.getUnsavedBooks().size())
                );
    }
}
