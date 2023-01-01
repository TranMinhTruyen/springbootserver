package com.ggapp.dao.repository.mongo;

import com.ggapp.dao.document.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Tran Minh Truyen
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, Integer>{
	List<Order> findOrderByCustomerId(int customerId);
	Order findOrderById(int id);
	Optional<Order> findOrderByIdAndCustomerId(int id, int customerId);
}
