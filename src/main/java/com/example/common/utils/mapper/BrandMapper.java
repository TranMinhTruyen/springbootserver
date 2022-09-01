package com.example.common.utils.mapper;

import com.example.common.dto.request.BrandRequest;
import com.example.common.dto.response.BrandResponse;
import com.example.dao.entity.Brand;
import org.mapstruct.Mapper;

@Mapper
public interface BrandMapper extends EntityMapper<BrandResponse, BrandRequest, Brand>{
}
