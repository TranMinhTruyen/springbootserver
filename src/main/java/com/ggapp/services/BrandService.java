package com.ggapp.services;

import com.ggapp.common.dto.request.BrandRequest;
import com.ggapp.common.dto.response.BrandResponse;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;

public interface BrandService {
	BrandResponse createBrand(BrandRequest brandRequest, CustomUserDetail customUserDetail) throws ApplicationException;
	CommonResponse getAllBrand(int page, int size);
	CommonResponse getBrandbyKeyword(int page, int size, String keyword);
	BrandResponse updateBrand(int id, BrandRequest brandRequest, CustomUserDetail customUserDetail) throws ApplicationException;
	BrandResponse logicDeleteBrand(int id, CustomUserDetail customUserDetail) throws ApplicationException;
	boolean physicalDeleteBrand(int id) throws ApplicationException;
	boolean isExists(String brandName);
}
