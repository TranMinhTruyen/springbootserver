package com.ggapp.common.utils.mapper;

import com.ggapp.common.dto.request.CartRequest;
import com.ggapp.common.dto.response.CartResponse;
import com.ggapp.dao.document.Cart;
import org.mapstruct.Mapper;

@Mapper
public interface CartMapper extends EntityMapper<CartResponse, CartRequest, Cart>{
}
