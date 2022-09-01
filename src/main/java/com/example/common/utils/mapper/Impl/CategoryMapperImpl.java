package com.example.common.utils.mapper.Impl;

import com.example.common.dto.request.CategoryRequest;
import com.example.common.dto.response.CategoryResponse;
import com.example.common.utils.mapper.CategoryMapper;
import com.example.dao.entity.Category;
import org.springframework.stereotype.Component;

import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category requestToEntity(CategoryRequest categoryRequest) {
        if (categoryRequest == null)
            return null;
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setImage(categoryRequest.getImage());
        return category;
    }

    @Override
    public CategoryResponse entityToResponse(Category category) {
        if (category == null)
            return null;
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());
        categoryResponse.setImage(category.getImage());
        categoryResponse.setCreatedDate(category.getCreatedDate());
        categoryResponse.setCreatedBy(category.getCreatedBy());
        categoryResponse.setUpdateDate(category.getUpdateDate());
        categoryResponse.setUpdateBy(category.getUpdateBy());
        categoryResponse.setDeleteDate(category.getDeleteDate());
        categoryResponse.setDeleteBy(category.getDeleteBy());
        return categoryResponse;
    }

    @Override
    public List<Category> requestToEntity(List<CategoryRequest> categoryRequests) {
        if (categoryRequests.isEmpty())
            return null;
        List<Category> categoryList = new ArrayList<>(categoryRequests.size());
        for (CategoryRequest categoryRequest : categoryRequests) {
            categoryList.add(requestToEntity(categoryRequest));
        }
        return categoryList;
    }

    @Override
    public List<CategoryResponse> entityToResponse(List<Category> categories) {
        if (categories.isEmpty())
            return null;
        List<CategoryResponse> categoryResponseList = new ArrayList<>(categories.size());
        for (Category category : categories) {
            categoryResponseList.add(entityToResponse(category));
        }
        return categoryResponseList;
    }
}
