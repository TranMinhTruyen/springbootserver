package com.ggapp.services;

import com.ggapp.common.dto.request.CategoryRequest;
import com.ggapp.common.dto.response.CategoryResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.jwt.CustomUserDetail;

public interface CategoryService {
	boolean createCategory(CategoryRequest categoryRequest, CustomUserDetail customUserDetail);
	CommonResponsePayload getAllCategory(int page, int size);
	CommonResponsePayload getCategoryByKeyword(int page, int size, String keyword);
	CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest, CustomUserDetail customUserDetail);
	boolean deleteCategory(Long id);
	boolean isExists(String categoryName);
}
