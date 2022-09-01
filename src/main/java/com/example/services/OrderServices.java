package com.example.services;

import com.example.common.dto.request.OrderRequest;
import com.example.common.dto.response.CommonResponse;
import com.example.common.dto.response.OrderResponse;
import com.example.common.exception.ApplicationException;

/**
 * @author Tran Minh Truyen
 */
public interface OrderServices {
	OrderResponse createOrderByCart(int customerId) throws ApplicationException;
	OrderResponse createOrderByProductId(int customerId, int[] productId) throws ApplicationException;
	CommonResponse getOrderByCustomerId(int page, int size, int id);
	boolean updateOrder(int id, OrderRequest orderRequest) throws ApplicationException;
	boolean deleteOrder(int id, int customerId) throws ApplicationException;
}
