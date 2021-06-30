package com.example.training.book.db;

import com.example.training.book.dtao.DBBookRepositoryDtao;
import com.example.training.book.exception.BookAlreadyExistsException;
import com.example.training.book.exception.BookNameIsNotValidException;
import com.example.training.book.exception.BookNotFoundException;
import com.example.training.book.exception.BookReferenceIsNotValidException;
import com.example.training.book.model.Book;
import com.example.training.book.repository.DBBookRepository;
import com.example.training.book.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
public class DbBookDtaoServiceTestUnit {

     BookService bookService;
    public DbBookDtaoServiceTestUnit(@Autowired DBBookRepositoryDtao dbBookRepositoryDtao) {
      var bookRepository = new DBBookRepository(dbBookRepositoryDtao);
      bookService = new BookService(bookRepository);
    }

    @BeforeEach
    public void init(){
        bookService.removeAll();
    }



    @Test
    public void addBook_insert_new_book_expected_same_book() {
        Book book = new Book("Omar", "Sportage");
        final var result =
                bookService
                        .addBook(book)
                        .book();
        Assertions
                .assertEquals(
                        book
                        , result);
    }

    @Test
    public void addBook_insert_a_book_that_already_exists_expected_book_already_exists_exception() {
        Book book = new Book("Omar", "Sportage");
        bookService.addBook(book);
        final var result =
                bookService
                        .addBook(book)
                        .savingException().get();
        Assert.isInstanceOf(BookAlreadyExistsException.class,result);
    }

    @Test
    public void fetchAllBooks_without_insert_expected_empty_collection(){
        Assertions
                .assertEquals(
                        0,
                        bookService
                                .fetchAllBooks()
                                .size());
    }

    @Test
    public void fetchAllBooks_with_one_insert_expected_1_as_size_of_the_collection_of_books(){
        Book book = new Book("Omar", "Sportage");
        bookService
                .addBook(book);
        Assertions
                .assertEquals(
                        1,
                        bookService
                                .fetchAllBooks()
                                .size());
    }

    @Test
    public void fetchAllBooks_with_insert_list_of_3_books_expected_3_as_size_of_the_collection_of_books(){
        Collection<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        bookService.saveAll(bookList);
        Assertions
                .assertEquals(
                        3,
                        bookService
                                .fetchAllBooks()
                                .size());
    }

    @Test
    public void saveAll_insert_3_books_list_expected_3_are_saved(){
        Collection<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        final var result  = bookService.saveAll(bookList);

        Assertions.assertAll( () ->
                        Assertions
                                .assertEquals(
                                        3,
                                        result.getSavedBooks().size())
                ,
                () ->   Assertions
                        .assertEquals(
                                0,
                                result.getUnsavedBooks().size()) );

    }

    @Test
    public void saveAll_insert_3_books_with_one_diplacated_list_expected_2_are_saved_and_1_exception(){
        Book book = new Book("Omar", "Sportage");
        bookService
                .addBook(book);
        Collection<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        final var result  = bookService.saveAll(bookList);

        Assertions.assertAll( () ->
                        Assertions
                                .assertEquals(
                                        2,
                                        result.getSavedBooks().size())
                ,
                () ->   Assertions
                        .assertEquals(
                                1,
                                result.getUnsavedBooks().size()) );

    }

    @Test
    public void saveAll_insert_4_books_list_expected_3_are_saved_and_1_unsaved(){
        Collection<Book> bookList = List.of(new Book("Omar", "Sportage")
                ,new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        final var result  = bookService.saveAll(bookList);

        Assertions.assertAll( () ->
                        Assertions
                                .assertEquals(
                                        3,
                                        result.getSavedBooks().size())
                ,
                () ->   Assertions
                        .assertEquals(
                                1,
                                result.getUnsavedBooks().size()) );

    }

    @Test
    public void saveAll_insert_4_with_1_diplicated_books_list_expected_exception_BookAlreadyExistsException(){
        Collection<Book> bookList = List.of(new Book("Omar", "Sportage")
                ,new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        final var result  =
                bookService
                        .saveAll(bookList)
                        .getUnsavedBooks()
                        .stream()
                        .findFirst()
                        .get()
                ;

        Assertions.assertAll( () ->
                        Assert.isInstanceOf(BookAlreadyExistsException.class, result.getReason())
                ,
                () ->   Assertions
                        .assertEquals(
                                new Book("Omar", "Sportage"),
                                result.getBook()) );
    }

    @Test
    public void deleteByReference_input_reference_expected_deleted(){
        Collection<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        final var result  = bookService.saveAll(bookList);
        bookService.deleteBookByReference("Sportage");
        Assertions
                .assertEquals(
                        2,
                        bookService.fetchAllBooks().size());

    }
    @Test
    public void deleteByReference_input_reference_expected_book_not_found(){
        Collection<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        bookService.saveAll(bookList);
        final var result  =  bookService.deleteBookByReference("Sportage1");
        Assertions.assertAll( () ->
                        Assert.isInstanceOf(BookNotFoundException.class, result.getLeft())
                ,
                () ->   Assertions
                        .assertEquals(
                                3,
                                bookService.fetchAllBooks().size()));

    }
    @Test
    public void getBookByReference_input_reference_expected_book_exist(){
        Collection<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        bookService.saveAll(bookList);
        final var result = bookService.getBookByReference("Sportage").get();
        Assertions
                .assertEquals(
                        new Book("Omar", "Sportage"),
                        result);
    }

    @Test
    public void getBookByReference_input_reference_expected_book_not_exist(){
        Collection<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        bookService.saveAll(bookList);
        final var result = bookService.getBookByReference("Sportage1") ;
        Assertions
                .assertThrows(
                        NoSuchElementException.class
                        ,
                        () -> result.get());
    }

    @Test
    public void updateBook_input_book_expected_updated_book(){
        List<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        bookService.saveAll(bookList);
        final var result =
                bookService
                        .updateBook(new Book("Omar007", "Sportage"))
                        .get();
        Assertions
                .assertEquals(
                        new Book("Omar007", "Sportage"),
                        result );
    }

    @Test
    public void updateBook_input_book_expected_book_does_not_exist_exception(){
        List<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        bookService.saveAll(bookList);
        final var result =
                bookService
                        .updateBook(new Book("Omar007", "BMW"))
                        .getLeft();
        Assert.isInstanceOf(
                BookNotFoundException.class,
                result
        );}

    @Test
    public void removeAllBooks_input_list_of_book_expected_empty(){
        List<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        bookService.saveAll(bookList);
        bookService.removeAll();
        Assertions
                .assertEquals(
                        0,
                        bookService.fetchAllBooks().size());

    }

    @Test
    public void addBook_insert_a_book_with_invalid_book_name_expected_invalid_book_name_exception() {
        Book book = new Book(null, "Sportage");
        bookService.addBook(book);
        final var result =
                bookService
                        .addBook(book)
                        .savingException().get();
        Assert.isInstanceOf(BookNameIsNotValidException.class,result);
    }

    @Test
    public void addBook_insert_a_book_with_invalid_book_reference_expected_invalid_book_reference_exception() {
        Book book = new Book("Omar", "");
        bookService.addBook(book);
        final var result =
                bookService
                        .addBook(book)
                        .savingException().get();
        Assert.isInstanceOf(BookReferenceIsNotValidException.class,result);
    }

}
