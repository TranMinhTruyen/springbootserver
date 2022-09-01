package com.example.common.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Tran Minh Truyen
 */
@Data
public class LoginRequest {

    @NotBlank(message = "account is mandatory")
    private String account;

    @NotBlank(message = "password is mandatory")
    private String password;

    private boolean isRemember;

    private DeviceInfoRequest deviceInfo;
}
