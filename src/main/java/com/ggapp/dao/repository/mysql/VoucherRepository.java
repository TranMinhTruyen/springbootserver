package com.ggapp.dao.repository.mysql;

import com.ggapp.dao.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Tran Minh Truyen on 26/11/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE = 4
 */
@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query(value = "select v from Voucher v " +
            "left join v.productVoucherList pvc " +
            "left join pvc.product p " +
            "where v.code = :code " +
            "and p.id = :productId and v.isDeleted = false and p.isDeleted = false")
    Optional<Voucher> findByCodeAndProductId(@Param(value = "code") String code, @Param(value = "productId") int productId);

    @Query(value = "select v from Voucher v " +
            "left join v.productVoucherList pvc " +
            "left join pvc.product p " +
            "where v.code = :code " +
            "and v.isDeleted = false and p.isDeleted = false")
    Optional<Voucher> findByCode(@Param(value = "code") String code);

    @Query(value = "select v from Voucher v " +
            "left join v.productVoucherList pvc " +
            "left join pvc.product p " +
            "where v.code in :listCode " +
            "and v.isDeleted = false and p.isDeleted = false")
    Optional<List<Voucher>> findByListCode(@Param(value = "listCode") List<String> listCode);
}
