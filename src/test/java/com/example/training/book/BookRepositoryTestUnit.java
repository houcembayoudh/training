package com.example.training.book;

import com.example.training.book.exception.BookAlreadyExistsException;
import com.example.training.book.exception.BookNotFoundException;
import com.example.training.book.model.Book;
import com.example.training.book.repository.BookRepository;
import com.example.training.book.repository.InMemoryBookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;
import java.util.NoSuchElementException;


public class BookRepositoryTestUnit {

    BookRepository bookRepository;

    @BeforeEach
    void init() {
        bookRepository = new InMemoryBookRepository();
    }

    @Test
    public void addBook_insert_new_book_expected_same_book() {
        Book book = new Book("Omar", "Sportage");
        final var result =
                bookRepository
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
        bookRepository.addBook(book);
        final var result =
                bookRepository
                        .addBook(book)
                        .savingException().get();
        Assert.isInstanceOf(BookAlreadyExistsException.class,result);
    }

    @Test
    public void fetchAllBooks_without_insert_expected_empty_collection(){
    Assertions
            .assertEquals(
                    0,
                    bookRepository
                            .fetchAllBooks()
                            .size());
    }

    @Test
    public void fetchAllBooks_with_one_insert_expected_1_as_size_of_the_collection_of_books(){
        Book book = new Book("Omar", "Sportage");
        bookRepository
                .addBook(book);
        Assertions
                .assertEquals(
                        1,
                        bookRepository
                                .fetchAllBooks()
                                .size());
    }

    @Test
    public void fetchAllBooks_with_insert_list_of_3_books_expected_3_as_size_of_the_collection_of_books(){
        List<Book> bookList = List.of(new Book("Omar", "Sportage"),
                                new Book("Houcem", "Octavia"),
                                new Book("Monta","el 11"));
        bookRepository.saveAll(bookList);
      Assertions
                .assertEquals(
                        3,
                        bookRepository
                                .fetchAllBooks()
                                .size());
    }

    @Test
    public void saveAll_insert_3_books_list_expected_3_are_saved(){
        List<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        final var result  = bookRepository.saveAll(bookList);

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
        bookRepository
                .addBook(book);
        List<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        final var result  = bookRepository.saveAll(bookList);

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
        List<Book> bookList = List.of(new Book("Omar", "Sportage")
                ,new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        final var result  = bookRepository.saveAll(bookList);

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
        List<Book> bookList = List.of(new Book("Omar", "Sportage")
                ,new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        final var result  =
                            bookRepository
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
        List<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        final var result  = bookRepository.saveAll(bookList);
        bookRepository.deleteBookByReference("Sportage");
        Assertions
                .assertEquals(
                        2,
                        bookRepository.fetchAllBooks().size());

    }
    @Test
    public void deleteByReference_input_reference_expected_book_not_found(){
        List<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        bookRepository.saveAll(bookList);
        final var result  =  bookRepository.deleteBookByReference("Sportage1");
        Assertions.assertAll( () ->
                        Assert.isInstanceOf(BookNotFoundException.class, result.getLeft())
                ,
                () ->   Assertions
                        .assertEquals(
                                3,
                                bookRepository.fetchAllBooks().size()));

    }
    @Test
    public void getBookByReference_input_reference_expected_book_exist(){
        List<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        bookRepository.saveAll(bookList);
        final var result = bookRepository.getBookByReference("Sportage").get();
        Assertions
                .assertEquals(
                        new Book("Omar", "Sportage"),
                        result);
    }

    @Test
    public void getBookByReference_input_reference_expected_book_not_exist(){
        List<Book> bookList = List.of(new Book("Omar", "Sportage"),
                new Book("Houcem", "Octavia"),
                new Book("Monta","el 11"));
        bookRepository.saveAll(bookList);
        final var result = bookRepository.getBookByReference("Sportage1") ;
        Assertions
                .assertThrows(
                        NoSuchElementException.class
                         ,
                        () -> result.get());
    }



    //getByreference cas book exist
    //getByreference cas book not exist


    //updateBook cas book exist
    //updateBook cas book not exist

    //deleteAll





}
