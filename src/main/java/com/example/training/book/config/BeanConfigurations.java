package com.example.training.book.config;

import com.example.training.book.service.InMemoryBookServiceImplementation;
import com.example.training.book.service.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurations {
    @Bean
    public BookService getBookService(){
        return new InMemoryBookServiceImplementation();
    }
}
