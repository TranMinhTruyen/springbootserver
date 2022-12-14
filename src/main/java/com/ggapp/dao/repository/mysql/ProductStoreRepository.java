package com.ggapp.dao.repository.mysql;

import com.ggapp.dao.entity.ProductStore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductStoreRepository extends JpaRepository<ProductStore, Long> {

    @Query(value = "select ps " +
            "from ProductStore ps " +
            "where ps.store.id = :storeId " +
            "and ps.product.id = :productId " +
            "and ps.product.isDeleted = false and ps.store.isActive = true")
    Optional<ProductStore> findProductStoreByStoreIdAndProductId(@Param(value = "storeId") Long storeId, @Param(value = "productId") Long productId);
}
