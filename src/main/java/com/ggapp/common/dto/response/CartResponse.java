package com.ggapp.common.dto.response;

import com.ggapp.common.dto.request.CartRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Tran Minh Truyen
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CartResponse extends CartRequest {
	private long productTotalAmount;
	private long amountInCart;
	private BigDecimal totalPrice;
}
