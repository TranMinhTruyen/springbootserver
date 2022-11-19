package com.ggapp.common.utils.mapper;

import com.ggapp.common.dto.request.BrandRequest;
import com.ggapp.common.dto.response.BrandResponse;
import com.ggapp.dao.entity.Brand;
import org.mapstruct.Mapper;

@Mapper
public interface BrandMapper extends EntityMapper<BrandResponse, BrandRequest, Brand>{
}
