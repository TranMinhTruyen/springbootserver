package com.ggapp.services;

import com.ggapp.common.dto.request.BrandRequest;
import com.ggapp.common.dto.response.BrandResponse;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;

public interface BrandService {
	BrandResponse createBrand(BrandRequest brandRequest, CustomUserDetail customUserDetail) throws ApplicationException;
	CommonResponsePayload getAllBrand(int page, int size);
	CommonResponsePayload getBrandbyKeyword(int page, int size, String keyword);
	BrandResponse updateBrand(int id, BrandRequest brandRequest, CustomUserDetail customUserDetail) throws ApplicationException;
	BrandResponse logicDeleteBrand(int id, CustomUserDetail customUserDetail) throws ApplicationException;
	boolean physicalDeleteBrand(int id) throws ApplicationException;
	boolean isExists(String brandName);
}
