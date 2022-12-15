package com.ggapp.dao.repository.mysql;

import com.ggapp.dao.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("select i from ProductImage i left join i.product p where p.isDeleted = false and i.isDeleted = false and p.id = :productId")
    Optional<List<ProductImage>> findByProductId(@Param("productId") Long productId);
}
