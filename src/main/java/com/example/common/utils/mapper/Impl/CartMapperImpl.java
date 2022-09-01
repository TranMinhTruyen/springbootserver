package com.example.common.utils.mapper.Impl;

import com.example.common.dto.request.CartRequest;
import com.example.common.dto.response.CartResponse;
import com.example.common.utils.mapper.CartMapper;
import com.example.dao.document.Cart;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapperImpl implements CartMapper {
    @Override
    public Cart requestToEntity(CartRequest cartRequest) {
        if (cartRequest == null)
            return null;
        Cart cart = new Cart();
        cart.setId(cartRequest.getCustomerId());
        cart.setProductList(cartRequest.getProductList());
        return cart;
    }

    @Override
    public CartResponse entityToResponse(Cart cart) {
        if (cart == null)
            return null;
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCustomerId(cart.getId());
        cartResponse.setProductList(cart.getProductList());
        cartResponse.setTotalPrice(cart.getTotalPrice());
        return null;
    }

    @Override
    public List<Cart> requestToEntity(List<CartRequest> cartRequests) {
        return null;
    }

    @Override
    public List<CartResponse> entityToResponse(List<Cart> carts) {
        return null;
    }
}
