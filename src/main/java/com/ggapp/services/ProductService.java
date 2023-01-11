package com.ggapp.services;

import com.ggapp.common.dto.request.ProductRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.ProductResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;

import javax.annotation.Nullable;

/**
 * @author Tran Minh Truyen
 */
public interface ProductService {
	ProductResponse createProduct(ProductRequest productRequest, CustomUserDetail customUserDetail) throws ApplicationException;
	CommonResponsePayload getAllProduct(int page, int size) throws ApplicationException;
	ProductResponse getProductById(int id) throws ApplicationException;
	CommonResponsePayload getProductByKeyWord(int page, int size,
                                              @Nullable String name,
                                              @Nullable String brand,
                                              @Nullable String category,
                                              float fromPrice,
                                              float toPrice) throws ApplicationException;
	ProductResponse updateProduct(int id, ProductRequest productRequest) throws ApplicationException;
	void physicalDeleteProduct(int[] id) throws ApplicationException;
	boolean logicalDeleteProduct(int[] id) throws ApplicationException;
	void deleteLogicImageOfProduct(int productId, int[] imageId) throws ApplicationException;
}
