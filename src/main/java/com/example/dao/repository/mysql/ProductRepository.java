package com.example.dao.repository.mysql;

import com.example.dao.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
	List<Product> findAllByBrandIdAndDeleteByIsNullAndDeleteDateIsNull(int id);
	List<Product> findAllByCategoryIdAndDeleteByIsNullAndDeleteDateIsNull(int id);
	List<Product> findAllByNameEqualsIgnoreCaseAndDeleteByIsNullAndDeleteDateIsNull(String name);
	List<Product> findAllByDeleteByIsNullAndDeleteDateIsNull();

	@Query("select p from Product p where p.isNew = true and p.deleteBy is null and p.deleteDate is null")
	List<Product> findAllByNewIsTrue();
}
