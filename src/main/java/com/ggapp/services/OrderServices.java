package com.ggapp.services;

import com.ggapp.common.dto.request.OrderRequest;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.common.dto.response.OrderResponse;
import com.ggapp.common.exception.ApplicationException;

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
