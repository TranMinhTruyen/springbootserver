package com.ggapp.common.utils.mapper;

import com.ggapp.common.dto.request.CategoryRequest;
import com.ggapp.common.dto.response.CategoryResponse;
import com.ggapp.dao.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper extends EntityMapper<CategoryResponse, CategoryRequest, Category>{
}
