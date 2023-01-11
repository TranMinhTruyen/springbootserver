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
	OrderResponse createOrder(CustomUserDetail customUserDetail, UserOrderRequest userOrderRequest) throws ApplicationException;
	CommonResponsePayload getOrderByCustomerId(CustomUserDetail customUserDetail, int page, int size);
	OrderResponse updateOrderByEmp(int orderId, CustomUserDetail customUserDetail, OrderRequest orderRequest) throws ApplicationException;
	OrderResponse updateOrderByUser(CustomUserDetail customUserDetail, UserOrderRequest userOrderRequest) throws ApplicationException;
	void deleteOrder(CustomUserDetail customUserDetail, int id) throws ApplicationException;
}
