package com.example.common.dto.response;

import com.example.dao.document.ListProduct;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Data
public class OrderResponse{
	private int id;
	private int customerId;
	private String customerName;
	private int employeeId;
	private String employeeName;
	private Date createDate;
	private List<ListProduct> listProducts;
	private BigDecimal totalPrice;
	private String status;
	private String address;
}
