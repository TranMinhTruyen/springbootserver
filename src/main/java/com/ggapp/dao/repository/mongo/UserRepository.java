package com.ggapp.dao.repository.mongo;

import com.ggapp.dao.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Tran Minh Truyen
 */
@Repository
public interface UserRepository extends MongoRepository<User, Integer> {
    Optional<List<User>> findUserByFullNameContaining(String fullName);
    Optional<User> findByEmailEqualsIgnoreCase(String email);
}
