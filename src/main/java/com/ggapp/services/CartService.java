package com.ggapp.services;

import com.ggapp.common.dto.response.CartResponse;
import com.ggapp.common.exception.ApplicationException;

/**
 * @author Tran Minh Truyen
 */
public interface CartService {
	CartResponse createCart(int customerId, int productId, int storeId, int productAmount) throws ApplicationException;
	CartResponse getCartById(int id) throws ApplicationException;
	CartResponse updateProductAmountInCart(int customerId, int productId, int storeId, int amount) throws ApplicationException;
	CartResponse deleteCart(int id, int storeId) throws ApplicationException;
	boolean deleteCartAfterCreateOrder(int id);
	CartResponse addProductToCart(int customerId, int productId, int storeId, int productAmount) throws ApplicationException;
	CartResponse removeProductFromCart(int customerId, int productId, int storeId) throws ApplicationException;
	boolean isCartExists(int customerId);
}
