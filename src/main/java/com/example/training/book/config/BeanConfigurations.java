package com.example.training.book.config;

import com.example.training.book.dtao.DBBookRepositoryDtao;
import com.example.training.book.repository.BookRepository;
import com.example.training.book.repository.DBBookRepository;
import com.example.training.book.service.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurations {

   /* @Bean
    public BookRepository getBookRepository(){

        return new InMemoryBookRepository();
    }*/
    @Bean
    public BookRepository getBookRepository(DBBookRepositoryDtao dbBookRepositoryDtao){
        return new DBBookRepository(dbBookRepositoryDtao);
    }
    @Bean
    public BookService getBookService(BookRepository bookRepository){
        return new BookService( bookRepository);
    }
}
