package com.example.dao.repository.mongo;

import com.example.dao.document.User;
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
    Optional<List<User>> findUserByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

    @Query(value = "{$and : [{$or : [{account: :#{#account}}, {email: {$regex: :#{#account}, $options: 'i'}}]}, {password: :#{#password}}]}")
    Optional<User> findUsersByAccountEqualsAndPasswordEquals(@Param("account") String account, @Param("password") String password);

    Optional<User> findUserByAccount(String account);

    Optional<User> findByEmailEqualsIgnoreCase(String email);
}
