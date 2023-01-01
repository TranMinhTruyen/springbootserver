package com.ggapp.dao.document;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Document(collection = "Order")
@Data
public class Order {

	private int id;

	@Version
	private int version;

	@Field(value = "customerId")
	private int customerId;

	@Field(value = "employeeId")
	private int employeeId;

	@Field(value = "createDate")
	private Date createDate;

	@Field(value = "cart")
	private List<ListProduct> listProducts;

	@Field(value = "totalPrice")
	private BigDecimal totalPrice;

	@Field(value = "address")
	private String address;

	@Field(value = "status")
	private String status;

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
}
