package com.example.training.book.dtao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class BookDtao {
    @Id
    private String reference;

    private String bookName;

    public BookDtao(com.example.training.book.model.Book book){
        this.reference=book.getReference();
        this.bookName=book.getBookName();
    }

    public com.example.training.book.model.Book toModel(){
        return new com.example.training.book.model.Book(
                this.bookName,
                this.reference
        );
    }
}
