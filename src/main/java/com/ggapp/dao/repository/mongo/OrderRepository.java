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
public interface OrderRepository extends MongoRepository<Order, Long>{
	List<Order> findOrderByCustomerId(Long customerId);
	Order findOrderById(Long id);
	Optional<Order> findOrderByIdAndCustomerId(Long id, Long customerId);
}
