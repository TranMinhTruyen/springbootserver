package com.ggapp.common.dto.request;

import com.ggapp.dao.document.ListProduct;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Tran Minh Truyen
 */
@Data
public class CartRequest {

	@NotBlank(message = "customerId is mandatory")
	private int customerId;

	@NotBlank(message = "productList is mandatory")
	private List<ListProduct> productList;
}
