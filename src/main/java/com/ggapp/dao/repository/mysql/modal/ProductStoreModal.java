package com.ggapp.dao.repository.mysql.modal;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductStoreModal {
    private int version;
    private String productName;
    private String storeCode;
    private BigDecimal price;
    private String brandName;
    private String categoryName;
    private long unitInStock;
    private boolean isNew;
}
