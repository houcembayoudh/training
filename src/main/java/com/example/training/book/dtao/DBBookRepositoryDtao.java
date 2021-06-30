package com.example.training.book.dtao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBBookRepositoryDtao extends MongoRepository<BookDtao, String> {


}
