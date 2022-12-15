package com.ggapp.services;

import com.ggapp.common.dto.request.OrderRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.OrderResponse;
import com.ggapp.common.exception.ApplicationException;

/**
 * @author Tran Minh Truyen
 */
public interface OrderService {
	OrderResponse createOrderByCart(Long customerId) throws ApplicationException;
	OrderResponse createOrderByProductId(Long customerId, int[] productId) throws ApplicationException;
	CommonResponsePayload getOrderByCustomerId(int page, int size, Long id);
	boolean updateOrder(Long id, OrderRequest orderRequest) throws ApplicationException;
	boolean deleteOrder(Long id, Long customerId) throws ApplicationException;
}
