package com.ggapp.services;

import com.ggapp.common.dto.request.ProductRequest;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.common.dto.response.ProductResponse;
import com.ggapp.common.exception.ApplicationException;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
public interface ProductServices {
	ProductResponse createProduct(ProductRequest productRequest) throws ApplicationException;
	CommonResponse getAllProduct(int page, int size) throws ApplicationException;
	ProductResponse getProductById(int id) throws ApplicationException;
	CommonResponse getProductByKeyWord(int page, int size,
									   @Nullable String name,
									   @Nullable String brand,
									   @Nullable String category,
									   float fromPrice,
									   float toPrice) throws ApplicationException;
	ProductResponse updateProduct(int id, ProductRequest productRequest) throws ApplicationException;
	boolean deleteProduct(List<Integer> id) throws ApplicationException;
	boolean deleteImageOfProduct(int productId, List<Integer> imageId) throws ApplicationException;
}
