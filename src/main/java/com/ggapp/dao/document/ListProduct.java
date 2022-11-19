package com.ggapp.dao.document;

import com.ggapp.common.dto.response.ProductImageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@Document
@Data
@AllArgsConstructor
public class ListProduct {

	@Field(value = "productId")
	private int id;

	@Field(value = "productName")
	private String productName;

	@Field(value = "productPrice")
	private BigDecimal price;

	@Field(value = "Image")
	private List<ProductImageResponse> productImage;

	@Field(value = "productPriceAfterDiscount")
	private BigDecimal priceAfterDiscount;

	@Field(value = "Discount")
	private float discount;

	@Field(value = "productAmount")
	private long productAmount;
}
