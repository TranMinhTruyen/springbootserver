package com.example.services;

import com.example.common.dto.request.CategoryRequest;
import com.example.common.dto.response.CategoryResponse;
import com.example.common.dto.response.CommonResponse;
import com.example.common.jwt.CustomUserDetail;

public interface CategoryServices {
	boolean createCategory(CategoryRequest categoryRequest, CustomUserDetail customUserDetail);
	CommonResponse getAllCategory(int page, int size);
	CommonResponse getCategoryByKeyword(int page, int size, String keyword);
	CategoryResponse updateCategory(int id, CategoryRequest categoryRequest, CustomUserDetail customUserDetail);
	boolean deleteCategory(int id);
	boolean isExists(String categoryName);
}
