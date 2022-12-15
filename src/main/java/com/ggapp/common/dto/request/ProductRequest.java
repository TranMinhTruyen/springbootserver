package com.ggapp.common.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */

@Data
public class ProductRequest {

	@NotBlank(message = "name is mandatory")
	private String name;

	private int version;

	private String productCode;

	@NotBlank(message = "price is mandatory")
	private BigDecimal price;

	@NotBlank(message = "type is mandatory")
	private String type;

	@NotBlank(message = "discount is mandatory")
	private float discount;

	@NotBlank(message = "unitInStock is mandatory")
	private long unitInStock;

	@NotBlank(message = "id_brand is mandatory")
	private Long idBrand;

	@NotBlank(message = "id_category is mandatory")
	private Long idCategory;

	@NotBlank(message = "isNew is mandatory")
	private boolean isNew;

	private List<ProductImageRequest> image;
}
