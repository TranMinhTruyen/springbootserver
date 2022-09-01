package com.example.dao.document;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

	@Field(value = "Address")
	private String address;

	@Field(value = "status")
	private String status;

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
}
