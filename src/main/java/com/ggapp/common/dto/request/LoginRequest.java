package com.ggapp.common.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

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
