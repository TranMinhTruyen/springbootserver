package com.ggapp.common.dto.request;

import lombok.Data;

/**
 * @author Tran Minh Truyen
 */
@Data
public class ProductReviewRequest {
    private long id;
    private int version;
    private int star;
    private String text;
}
