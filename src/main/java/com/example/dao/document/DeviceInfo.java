package com.example.dao.document;

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

    @Field(value = "token")
    private String token;
}
