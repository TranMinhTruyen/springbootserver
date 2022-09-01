package com.example.common.utils.mapper;

import com.example.common.dto.request.CartRequest;
import com.example.common.dto.response.CartResponse;
import com.example.dao.document.Cart;
import org.mapstruct.Mapper;

@Mapper
public interface CartMapper extends EntityMapper<CartResponse, CartRequest, Cart>{
}
