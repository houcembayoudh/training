package com.example.training.book.endpoint.rest;

import com.example.training.book.repository.BookRepository;
import com.example.training.book.service.BookService;
import com.example.training.book.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;


@RestController
@RequestMapping("/api")
public class BookEndPoint {

    private final BookService bookService;

    public BookEndPoint(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("books")
    public ResponseEntity <Collection<Book>> fetchAllBooks() {
        return ResponseEntity.ok(bookService.fetchAllBooks());
    }

    @GetMapping("/books/{reference}")
    public ResponseEntity<Book> getBookByReference(@PathVariable(required = true, name = "reference") String reference) {
        return
                bookService
                        .getBookByReference(reference)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()) ;
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {

        final var result = bookService.addBook(book);
        if(result.savingException().isPresent()){
            return ResponseEntity.badRequest().header("error",
                    result.savingException().get().getMessage()).build();
        }
        return ResponseEntity.ok(result.book());
    }

    @PutMapping("/books")
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
    @DeleteMapping("/books/{reference}")
    public ResponseEntity<Book> deleteBookByReference(
            @PathVariable(required = true, name = "reference")
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

    @PostMapping("/books/all")
    public ResponseEntity<BookRepository.SavingBooksRecord> saveAllBooks
            (@RequestBody Collection<Book> books){
    return ResponseEntity
            .ok(bookService
                    .saveAll(books));
//    TODO(create a method that converts SavingBooksRecord to John doe)
    }

}
