package com.example.dao.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "Session")
@Data
public class Session {
    private int id;

    @Field(value = "CreatedDate")
    private Date createdDate;

    @Field(value = "DeviceInfoList")
    private List<DeviceInfo> deviceInfoList;
}
