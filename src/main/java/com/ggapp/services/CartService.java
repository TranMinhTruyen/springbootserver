package com.ggapp.services;

import com.ggapp.common.dto.response.CartResponse;
import com.ggapp.common.exception.ApplicationException;

/**
 * @author Tran Minh Truyen
 */
public interface CartService {
	CartResponse createCart(Long customerId, Long productId, long productAmount) throws ApplicationException;
	CartResponse getCartById(Long id) throws ApplicationException;
	CartResponse updateProductAmountInCart(Long customerId, Long productId, long amount) throws ApplicationException;
	CartResponse deleteCart(Long id);
	boolean deleteCartAfterCreateOrder(Long id);
	CartResponse addProductToCart(Long customerId, Long productId, long productAmount) throws ApplicationException;
	CartResponse removeProductFromCart(Long customerId, Long productId);
	boolean isCartExists(Long customerId);
}
