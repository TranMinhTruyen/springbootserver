package com.ggapp.services;

import com.ggapp.common.dto.request.ProductRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.ProductResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
public interface ProductService {
	ProductResponse createProduct(ProductRequest productRequest, CustomUserDetail customUserDetail) throws ApplicationException;
	CommonResponsePayload getAllProduct(int page, int size) throws ApplicationException;
	ProductResponse getProductById(Long id) throws ApplicationException;
	CommonResponsePayload getProductByKeyWord(int page, int size,
                                              @Nullable String name,
                                              @Nullable String brand,
                                              @Nullable String category,
                                              float fromPrice,
                                              float toPrice) throws ApplicationException;
	ProductResponse updateProduct(Long id, ProductRequest productRequest) throws ApplicationException;
	void deleteProduct(List<Long> id) throws ApplicationException;
	boolean deleteLogicProduct(List<Long> id) throws ApplicationException;
	void deleteLogicImageOfProduct(Long productId, List<Long> imageId) throws ApplicationException;
}
