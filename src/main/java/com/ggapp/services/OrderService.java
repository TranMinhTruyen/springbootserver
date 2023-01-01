package com.ggapp.services;

import com.ggapp.common.dto.request.OrderRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.OrderResponse;
import com.ggapp.common.exception.ApplicationException;

/**
 * @author Tran Minh Truyen
 */
public interface OrderService {
	OrderResponse createOrderByCart(int customerId) throws ApplicationException;
	OrderResponse createOrderByProductId(int customerId, int[] productId, int storeId) throws ApplicationException;
	CommonResponsePayload getOrderByCustomerId(int page, int size, int id);
	boolean updateOrder(int id, OrderRequest orderRequest) throws ApplicationException;
	boolean deleteOrder(int id, int customerId) throws ApplicationException;
}
