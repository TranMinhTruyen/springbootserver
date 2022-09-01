package com.example.common.dto.request;

import lombok.Data;

/**
 * @author Tran Minh Truyen
 */
@Data
public class ProductReviewRequest {
    private int productId;
    private int star;
    private String text;
}
