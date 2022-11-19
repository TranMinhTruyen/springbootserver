package com.ggapp.common.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BrandRequest {
	@NotBlank(message = "name is mandatory")
	private String name;
	private String description;
	private String image;
}
