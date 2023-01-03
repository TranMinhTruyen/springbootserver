package com.ggapp.services;

import com.ggapp.common.dto.request.CategoryRequest;
import com.ggapp.common.dto.response.CategoryResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;

public interface CategoryService {
	CategoryResponse createCategory(CategoryRequest categoryRequest, CustomUserDetail customUserDetail) throws ApplicationException;
	CommonResponsePayload getAllCategory(int page, int size);
	CommonResponsePayload getCategoryByKeyword(int page, int size, String keyword);
	CategoryResponse updateCategory(int id, CategoryRequest categoryRequest, CustomUserDetail customUserDetail) throws ApplicationException;
	CategoryResponse logicDeleteCategory(int id, CustomUserDetail customUserDetail) throws ApplicationException;
	boolean physicalDeleteBrand(int id) throws ApplicationException;
	boolean isExists(String categoryName) throws ApplicationException;
}
