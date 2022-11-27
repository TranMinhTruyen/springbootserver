package com.ggapp.common.utils.mapper;

import com.ggapp.common.dto.request.OrderRequest;
import com.ggapp.common.dto.response.OrderResponse;
import com.ggapp.dao.document.Order;
import org.mapstruct.Mapper;

/**
 * @author Tran Minh Truyen
 */
@Mapper
public interface OrderMapper extends EntityMapper<OrderResponse, OrderRequest, Order>{
}
