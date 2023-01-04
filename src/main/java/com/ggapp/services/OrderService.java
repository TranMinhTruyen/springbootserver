package com.ggapp.services;

import com.ggapp.common.dto.request.OrderRequest;
import com.ggapp.common.dto.request.UserOrderRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.OrderResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;

/**
 * @author Tran Minh Truyen
 */
public interface OrderService {
	OrderResponse createOrderByCart(CustomUserDetail customUserDetail) throws ApplicationException;
	OrderResponse createOrderByProductId(CustomUserDetail customUserDetail, int[] productId, int storeId) throws ApplicationException;
	CommonResponsePayload getOrderByCustomerId(CustomUserDetail customUserDetail, int page, int size);
	OrderResponse updateOrderByEmp(int orderId, CustomUserDetail customUserDetail, OrderRequest orderRequest) throws ApplicationException;
	OrderResponse updateOrderByUser(CustomUserDetail customUserDetail, UserOrderRequest userOrderRequest) throws ApplicationException;
	boolean deleteOrder(int id, int customerId) throws ApplicationException;
}
