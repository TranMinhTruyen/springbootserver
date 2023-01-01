package com.ggapp.dao.repository.mongo;

import com.ggapp.dao.document.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends MongoRepository<Session, Integer> {
}
