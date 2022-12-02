package com.ggapp.common.dto.response;

import com.ggapp.common.dto.request.ProductReviewRequest;
import lombok.Data;

/**
 * @author Tran Minh Truyen
 */
@Data
public class ProductReviewReponse extends ProductReviewRequest {
    private String userName;
}
