package com.ggapp.dao.repository.mysql.modal;

import com.ggapp.dao.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Tran Minh Truyen on 27/11/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
@Data
@AllArgsConstructor
public class VoucherAndListProductModal {
    private String code;
    private String discountType;
    private String discountValue;
    private List<Product> listProduct;
}
