package com.example.common.utils.mapper.Impl;

import com.example.common.dto.request.OrderRequest;
import com.example.common.dto.response.OrderResponse;
import com.example.common.utils.mapper.OrderMapper;
import com.example.dao.document.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponse entityToResponse(Order order) {
        if (order == null)
            return null;
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setCustomerId(orderResponse.getCustomerId());
        orderResponse.setCustomerName(orderResponse.getCustomerName());
        orderResponse.setEmployeeId(orderResponse.getEmployeeId());
        orderResponse.setEmployeeName(orderResponse.getEmployeeName());
        orderResponse.setCreateDate(order.getCreateDate());
        orderResponse.setAddress(orderResponse.getAddress());
        orderResponse.setListProducts(order.getListProducts());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setStatus(orderResponse.getStatus());
        return orderResponse;
    }

    @Override
    public List<OrderResponse> entityToResponse(List<Order> orders) {
        if (orders.isEmpty())
            return null;
        List<OrderResponse> orderResponseList = new ArrayList<>();
        for (Order order: orders) {
            orderResponseList.add(entityToResponse(order));
        }
        return orderResponseList;
    }

    @Override
    @Deprecated
    public List<Order> requestToEntity(List<OrderRequest> orderRequests) {
        return null;
    }

    @Override
    @Deprecated
    public Order requestToEntity(OrderRequest orderRequest) {
        return null;
    }
}
