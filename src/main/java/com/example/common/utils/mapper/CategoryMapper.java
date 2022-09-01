package com.example.common.utils.mapper;

import com.example.common.dto.request.CategoryRequest;
import com.example.common.dto.response.CategoryResponse;
import com.example.dao.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper extends EntityMapper<CategoryResponse, CategoryRequest, Category>{
}
