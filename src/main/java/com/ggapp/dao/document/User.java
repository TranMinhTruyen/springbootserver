package com.ggapp.dao.document;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


/**
 * @author Tran Minh Truyen
 */

@Document(collection = "User")
@Data
public class User {

    private int id;

    @Version
    private int version;

    @Field(value = "fullName")
    private String fullName;

    @Field(value = "birthDay")
    private LocalDateTime birthDay;

    @Field(value = "address")
    private String address;

    @Field(value = "district")
    private String district;

    @Field(value = "city")
    private String city;

    @Field(value = "postCode")
    private String postCode;

    @Field(value = "citizenID")
    private String citizenId;

    @Field(value = "email")
    private String email;

    @Field(value = "phoneNumber")
    private String phoneNumber;

    @Field(value = "role")
    private String role;

    @Field(value = "authorities")
    private List<String> authorities;

    @Field(value = "imageFilePath")
    private String imageFilePath;

    @Field(value = "isActive")
    private boolean isActive;

    @Column(name = "isDeleted")
    private boolean isDeleted;

    @Field(name = "createdDate")
    private LocalDateTime createdDate;

    @Field(name = "createdBy")
    private String createdBy;

    @Field(name = "updateDate")
    private LocalDateTime updateDate;

    @Field(name = "updateBy")
    private String updateBy;

    @Field(name = "deleteDate")
    private LocalDateTime deleteDate;

    @Field(name = "deleteBy")
    private String deleteBy;
}
