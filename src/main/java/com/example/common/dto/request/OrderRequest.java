package com.example.common.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Tran Minh Truyen
 */
@Data
public class OrderRequest {

	@NotBlank(message = "employeeId is mandatory")
	private int employeeId;

	@NotBlank(message = "address is mandatory")
	private String address;

	@NotBlank(message = "status is mandatory")
	private String status;
}
