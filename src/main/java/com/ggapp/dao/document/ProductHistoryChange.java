package com.ggapp.dao.document;

import com.ggapp.dao.entity.Brand;
import com.ggapp.dao.entity.Category;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "ProductHistoryChange")
public class ProductHistoryChange {
    private int id;

    @Field(value = "Version")
    private int version;

    @Field(value = "Name")
    private String name;

    @Field(value = "Price")
    private BigDecimal price;

    @Field(value = "Type")
    private String type;

    @Field(value = "Discount")
    private float discount;

    @Field(value = "UnitInStock")
    private long unitInStock;

    @Field(value = "Brand")
    private Brand brand;

    @Field(value = "Category")
    private Category category;

    @Field(value = "CreatedDate")
    private LocalDateTime createdDate;

    @Field(value = "CreatedBy")
    private String createdBy;

    @Field(value = "UpdateDate")
    private LocalDateTime updateDate;

    @Field(value = "UpdateBy")
    private String updateBy;

    @Field(value = "DeleteDate")
    private LocalDateTime deleteDate;

    @Field(value = "DeleteBy")
    private String deleteBy;

    @Field(value = "IsNew")
    private boolean isNew;
}
