package com.example.training.book.endpoint.rest;

import com.example.training.book.model.Book;
import com.example.training.book.repository.BookRepository;
import com.example.training.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.List;


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

        final var result = bookService.addBook(book);
        if(result.savingException().isPresent()){
            return ResponseEntity.badRequest().header("error",
                    result.savingException().get().getMessage()).build();
        }
        return ResponseEntity.ok(result.book());
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
    @DeleteMapping("/deleteBook/{reference}")
    public ResponseEntity<Book> deleteBookByReference(
            @RequestParam(required = true, name = "reference")
                    String reference) {
        return
                bookService
                .deleteBookByReference(reference)
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

    @PostMapping("/saveListOfBooks")
    public ResponseEntity<BookRepository.SavingBooksRecord> saveAllBooks
            (@RequestBody Collection<Book> books){
    return ResponseEntity
            .ok(bookService
                    .saveAll(books));
    }

}
