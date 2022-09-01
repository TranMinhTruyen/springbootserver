package com.example.common.utils.mapper.Impl;

import com.example.common.dto.request.ProductReviewRequest;
import com.example.common.dto.response.ProductReviewReponse;
import com.example.common.utils.mapper.ProductReviewMapper;
import com.example.dao.entity.ProductReview;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Component
public class ProductReviewMapperImpl implements ProductReviewMapper {
    @Override
    public ProductReview requestToEntity(ProductReviewRequest productReviewRequest) {
        if (productReviewRequest == null)
            return null;
        ProductReview productReview = new ProductReview();
        productReview.setStar(productReviewRequest.getStar());
        productReview.setText(productReviewRequest.getText());
        return productReview;
    }

    @Override
    public ProductReviewReponse entityToResponse(ProductReview productReview) {
        if (productReview == null)
            return null;
        ProductReviewReponse productReviewReponse = new ProductReviewReponse();
        productReviewReponse.setId(productReview.getId());
        productReviewReponse.setProductId(productReview.getProduct().getId());
        productReviewReponse.setStar(productReview.getStar());
        productReviewReponse.setUserName(productReview.getText());
        productReviewReponse.setText(productReview.getText());
        return productReviewReponse;
    }

    @Override
    public List<ProductReviewReponse> entityToResponse(List<ProductReview> productReviews) {
        if (productReviews.isEmpty())
            return null;
        List<ProductReviewReponse> productReviewReponseList = new ArrayList<>(productReviews.size());
        for (ProductReview productReview : productReviews) {
            productReviewReponseList.add(entityToResponse(productReview));
        }
        return productReviewReponseList;
    }

    @Override
    @Deprecated
    public List<ProductReview> requestToEntity(List<ProductReviewRequest> productReviewRequests) {
        return null;
    }
}
