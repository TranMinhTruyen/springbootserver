package com.example.dao.repository.mongo;

import com.example.dao.document.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tran Minh Truyen
 */
@Repository
public interface CartRepository extends MongoRepository<Cart, Integer> {
}
