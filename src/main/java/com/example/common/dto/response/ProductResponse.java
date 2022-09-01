package com.example.common.dto.response;

import com.example.dao.entity.ProductReview;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Data
public class ProductResponse {
	private int id;
	private String name;
	private BigDecimal price;
	private BigDecimal priceAfterDiscount;
	private String type;
	private float discount;
	private long unitInStock;
	private String brand;
	private String category;
	private boolean isNew;
	private LocalDateTime createdDate;
	private String createdBy;
	private LocalDateTime updateDate;
	private String updateBy;
	private LocalDateTime deleteDate;
	private String deleteBy;
	private List<ProductImageResponse> image;
	private List<ProductReviewReponse> productReviewList;
}
