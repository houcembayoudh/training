package com.example.training.book.endpoint.rest;

import com.example.training.book.service.BookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookEndPoint {

private final BookService bookService;

  public BookEndPoint(BookService bookService) {
    this.bookService = bookService;
  }


}
