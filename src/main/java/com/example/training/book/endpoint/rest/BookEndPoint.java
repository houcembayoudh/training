package com.example.training.book.endpoint.rest;

import com.example.training.book.model.Book;
import com.example.training.book.service.BookService;
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
  public Collection<Book> fetchAllBooks(){
    return bookService.fetchAllBooks();
  }


 @GetMapping("/getBookByReference")
 @ResponseBody
  public Optional<Book> getBookByReference(@RequestParam String reference){
   return bookService.getBookByReference(reference) ;
  }



}
