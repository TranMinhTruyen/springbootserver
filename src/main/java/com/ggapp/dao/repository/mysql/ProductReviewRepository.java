package com.ggapp.dao.repository.mysql;

import com.ggapp.dao.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Tran Minh Truyen
 */
@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {
    Optional<List<ProductReview>> findProductReviewByUserNameContainingIgnoreCase(String userName);

    @Query(value = "select pr from ProductReview pr where pr.product.id = :productId")
    Optional<List<ProductReview>> findByProductId(@Param(value = "productId") Integer productId);
}
