package com.ggapp.common.dto.request;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CheckEmailRequest {

	@NotBlank(message = "email is mandatory")
	@Pattern(regexp = "^(.+)@(.+)$")
	private String email;

	private String confirmKey;
}
