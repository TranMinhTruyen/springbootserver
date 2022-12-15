package com.ggapp.dao.document;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Document(collection = "Cart")
@Data
public class Cart {

	private Long id;

	@Version
	private int version;

	@Field(value = "productList")
	private List<ListProduct> productList;

	@Field(value = "totalPrice")
	private BigDecimal totalPrice;

	@Column(name = "isDeleted")
	private boolean isDeleted;

	@Field(name = "createdDate")
	private LocalDateTime createdDate;

	@Field(name = "createdBy")
	private String createdBy;

	@Field(name = "updateDate")
	private LocalDateTime updateDate;

	@Field(name = "updateBy")
	private String updateBy;

	@Field(name = "deleteDate")
	private LocalDateTime deleteDate;

	@Field(name = "deleteBy")
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
