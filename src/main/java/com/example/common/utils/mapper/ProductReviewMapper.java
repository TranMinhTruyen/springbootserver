package com.example.common.utils.mapper;

import com.example.common.dto.request.ProductReviewRequest;
import com.example.common.dto.response.ProductReviewReponse;
import com.example.dao.entity.ProductReview;
import org.mapstruct.Mapper;

/**
 * @author Tran Minh Truyen
 */
@Mapper
public interface ProductReviewMapper extends EntityMapper<ProductReviewReponse, ProductReviewRequest, ProductReview>{
}
