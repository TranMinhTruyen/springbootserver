package com.example.dao.document;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Document(collection = "Cart")
@Data
public class Cart {

	private int id;

	@Version
	private int version;

	@Field(value = "productList")
	private List<ListProduct> productList;

	@Field(value = "totalPrice")
	private BigDecimal totalPrice;

	@Field(name = "CreatedDate")
	private LocalDateTime createdDate;

	@Field(name = "CreatedBy")
	private String createdBy;

	@Field(name = "UpdateDate")
	private LocalDateTime updateDate;

	@Field(name = "UpdateBy")
	private String updateBy;

	@Field(name = "DeleteDate")
	private LocalDateTime deleteDate;

	@Field(name = "DeleteBy")
	private String deleteBy;

	@Override
	public String toString() {
		return "Cart{" +
				"userId=" + id +
				", productList=" + productList +
				", totalPrice=" + totalPrice +
				'}';
	}
}
