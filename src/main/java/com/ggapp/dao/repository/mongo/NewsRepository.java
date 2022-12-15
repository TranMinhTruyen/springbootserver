package com.ggapp.dao.repository.mongo;

import com.ggapp.dao.document.News;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends MongoRepository<News, Long> {
}
