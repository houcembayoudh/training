package com.example.training.book.endpoint.rest;

import com.example.training.book.exception.BookNotFoundException;
import com.example.training.book.model.Book;
import com.example.training.book.service.BookService;
import io.vavr.control.Either;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookEndPoint {

    private final BookService bookService;

    public BookEndPoint(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("fetchAllBooks")
    public ResponseEntity <Collection<Book>> fetchAllBooks() {
        return ResponseEntity.ok(bookService.fetchAllBooks());
    }

    @GetMapping("/getBookByReference")
    public ResponseEntity<Book> getBookByReference(@RequestParam String reference) {
        return
                bookService
                        .getBookByReference(reference)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()) ;
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return
                bookService
                        .addBook(book)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.internalServerError().build());

    }

    @PutMapping("/updateBook")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        return bookService
                .updateBook(book)
                .fold(
                        it -> ResponseEntity
                                .badRequest()
                                .header(
                                        "error",
                                        it.getMessage()
                                )
                                .build(),
                        ResponseEntity::ok
                );
    }

    @DeleteMapping("/deleteBook")
    public ResponseEntity<Book> deleteBook(@RequestBody Book book) {
        return
                bookService
                .deleteBook(book)
                .fold(
                        it -> ResponseEntity
                                .badRequest()
                                .header(
                                        "error",
                                        it.getMessage()
                                )
                                .build() ,
                        ResponseEntity::ok
                ) ;
    }


}
