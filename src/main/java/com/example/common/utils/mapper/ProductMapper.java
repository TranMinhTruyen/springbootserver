package com.example.common.utils.mapper;

import com.example.common.dto.request.ProductRequest;
import com.example.common.dto.response.ProductResponse;
import com.example.dao.entity.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper extends EntityMapper<ProductResponse, ProductRequest, Product>{
}
