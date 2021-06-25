package com.example.training.book.config;

import com.example.training.book.repository.BookRepository;
import com.example.training.book.repository.InMemoryBookRepository;
import com.example.training.book.service.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurations {

    @Bean
    public InMemoryBookRepository getBookRepository(){

        return new InMemoryBookRepository();
    }
    @Bean
    public BookService getBookService(InMemoryBookRepository bookRepository){
        return new BookService( bookRepository);
    }
}
