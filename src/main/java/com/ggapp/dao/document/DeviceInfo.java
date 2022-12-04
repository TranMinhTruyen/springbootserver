package com.ggapp.dao.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "DeviceInfo")
public class DeviceInfo {
    @Field(value = "OS")
    private String OS;

    @Field(value = "deviceName")
    private String deviceName;

    @Field(value = "deviceMac")
    private String deviceMac;

    @Field(value = "deviceIp")
    private String deviceIp;

    @Field(value = "token")
    private String token;

    @Field(value = "status")
    private String status;
}
