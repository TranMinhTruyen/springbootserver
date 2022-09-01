package com.example.services;

import com.example.common.dto.request.BrandRequest;
import com.example.common.dto.response.BrandResponse;
import com.example.common.dto.response.CommonResponse;
import com.example.common.jwt.CustomUserDetail;

public interface BrandServices {
	boolean createBrand(BrandRequest brandRequest, CustomUserDetail customUserDetail);
	CommonResponse getAllBrand(int page, int size);
	CommonResponse getBrandbyKeyword(int page, int size, String keyword);
	BrandResponse updateBrand(int id, BrandRequest brandRequest, CustomUserDetail customUserDetail);
	boolean deleteBrand(int id);
	boolean isExists(String brandName);
}
