package com.example.dao.repository.mongo;

import com.example.dao.document.News;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends MongoRepository<News, Integer> {
}
