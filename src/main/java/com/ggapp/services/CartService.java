package com.ggapp.services;

import com.ggapp.common.dto.response.CartResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;

/**
 * @author Tran Minh Truyen
 */
public interface CartService {
	CartResponse createCartAndAddProductToCart(CustomUserDetail customUserDetail, int productId, int storeId, int productAmount) throws ApplicationException;
	CartResponse getCartById(int id) throws ApplicationException;
	CartResponse updateProductAmountInCart(int customerId, int productId, int storeId, int amount) throws ApplicationException;
	CartResponse deleteCart(int id, int storeId) throws ApplicationException;
	boolean deleteCartAfterCreateOrder(int id);
	CartResponse removeProductFromCart(int customerId, int productId, int storeId) throws ApplicationException;
}
