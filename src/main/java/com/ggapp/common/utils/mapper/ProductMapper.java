package com.ggapp.common.utils.mapper;

import com.ggapp.common.dto.request.ProductRequest;
import com.ggapp.common.dto.response.ProductResponse;
import com.ggapp.dao.entity.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper extends EntityMapper<ProductResponse, ProductRequest, Product>{
}
