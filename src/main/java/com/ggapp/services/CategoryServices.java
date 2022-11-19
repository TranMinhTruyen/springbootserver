package com.ggapp.services;

import com.ggapp.common.dto.request.CategoryRequest;
import com.ggapp.common.dto.response.CategoryResponse;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.common.jwt.CustomUserDetail;

public interface CategoryServices {
	boolean createCategory(CategoryRequest categoryRequest, CustomUserDetail customUserDetail);
	CommonResponse getAllCategory(int page, int size);
	CommonResponse getCategoryByKeyword(int page, int size, String keyword);
	CategoryResponse updateCategory(int id, CategoryRequest categoryRequest, CustomUserDetail customUserDetail);
	boolean deleteCategory(int id);
	boolean isExists(String categoryName);
}
