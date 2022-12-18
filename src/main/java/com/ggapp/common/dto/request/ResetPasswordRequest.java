package com.ggapp.common.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^(.+)@(.+)$")
    private String email;
}
