package com.ggapp.common.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeviceInfoRequest {
    @NotBlank(message = "OS is mandatory")
    private String OS;

    @NotBlank(message = "DeviceName is mandatory")
    private String deviceName;

    private String deviceMac;

    private String deviceIp;
}
