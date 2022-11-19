package com.ggapp.common.utils.mapper;

import com.ggapp.common.dto.request.ProductReviewRequest;
import com.ggapp.common.dto.response.ProductReviewReponse;
import com.ggapp.dao.entity.ProductReview;
import org.mapstruct.Mapper;

/**
 * @author Tran Minh Truyen
 */
@Mapper
public interface ProductReviewMapper extends EntityMapper<ProductReviewReponse, ProductReviewRequest, ProductReview>{
}
