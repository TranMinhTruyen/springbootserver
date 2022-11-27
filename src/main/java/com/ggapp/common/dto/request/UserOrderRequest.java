package com.ggapp.common.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class
UserOrderRequest implements Serializable {

	@NotBlank(message = "address is mandatory")
	private String address;

	private int [] productId;
}
