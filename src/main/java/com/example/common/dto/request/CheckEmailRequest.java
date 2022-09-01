package com.example.common.dto.request;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckEmailRequest {

	@NotBlank(message = "email is mandatory")
	private String email;

	private String confirmKey;
}
