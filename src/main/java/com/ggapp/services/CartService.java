package com.ggapp.services;

import com.ggapp.common.dto.response.CartResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;

/**
 * @author Tran Minh Truyen
 */
public interface CartService {
	CartResponse createCartAndAddProductToCart(CustomUserDetail customUserDetail, int productId, int storeId, int productAmount) throws ApplicationException;
	CartResponse getCartOwner(CustomUserDetail customUserDetail) throws ApplicationException;
	CartResponse updateProductAmountInCart(CustomUserDetail customUserDetail, int productId, int storeId, int amount) throws ApplicationException;
	CartResponse deleteCart(CustomUserDetail customUserDetail, int storeId) throws ApplicationException;
	boolean deleteCartAfterCreateOrder(int id);
	CartResponse removeProductFromCart(CustomUserDetail customUserDetail, int productId, int storeId) throws ApplicationException;
	CartResponse removeSingleProductFromCart(CustomUserDetail customUserDetail, int productId) throws ApplicationException;
}
