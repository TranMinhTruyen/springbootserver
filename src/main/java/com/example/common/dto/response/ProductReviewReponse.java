package com.example.common.dto.response;

import com.example.common.dto.request.ProductReviewRequest;
import lombok.Data;

/**
 * @author Tran Minh Truyen
 */
@Data
public class ProductReviewReponse extends ProductReviewRequest {
    private int id;
    private String userName;
}
