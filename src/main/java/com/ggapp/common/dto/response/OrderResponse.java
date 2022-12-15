package com.ggapp.common.dto.response;

import com.ggapp.dao.document.ListProduct;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Data
public class OrderResponse{
	private long id;
	private int version;
	private long customerId;
	private String customerName;
	private long employeeId;
	private String employeeName;
	private Date createDate;
	private List<ListProduct> listProducts;
	private BigDecimal totalPrice;
	private String status;
	private String address;
}
