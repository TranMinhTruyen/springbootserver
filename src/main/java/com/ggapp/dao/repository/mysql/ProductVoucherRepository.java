package com.ggapp.dao.repository.mysql;

import com.ggapp.dao.entity.ProductVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVoucherRepository extends JpaRepository<ProductVoucher, Integer> {
}
