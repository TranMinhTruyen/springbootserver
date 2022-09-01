package com.example.common.dto.response;

import com.example.common.dto.request.CartRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Tran Minh Truyen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CartResponse extends CartRequest {
	private long totalAmount;
	private BigDecimal totalPrice;
}
