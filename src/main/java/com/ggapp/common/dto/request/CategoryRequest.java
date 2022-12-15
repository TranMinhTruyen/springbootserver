package com.ggapp.common.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryRequest {

	@NotBlank(message = "name is mandatory")
	private String name;
}
