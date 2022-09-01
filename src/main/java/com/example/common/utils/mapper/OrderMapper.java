package com.example.common.utils.mapper;

import com.example.common.dto.request.OrderRequest;
import com.example.common.dto.response.OrderResponse;
import com.example.dao.document.Order;
import org.mapstruct.Mapper;

/**
 * @author Tran Minh Truyen
 */
@Mapper
public interface OrderMapper extends EntityMapper<OrderResponse, OrderRequest, Order>{
}
