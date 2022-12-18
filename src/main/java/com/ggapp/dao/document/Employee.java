package com.ggapp.dao.document;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Tran Minh Truyen on 04/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */

@Document(collection = "Employee")
@Data
public class Employee {

    private Long id;

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

    @Field(value = "citizenID")
    private String citizenId;

    @Field(value = "email")
    private String email;

    @Field(value = "phoneNumber")
    private String phoneNumber;

    @Field(value = "role")
    private String role;

    @Field(value = "position")
    private String position;

    @Field(value = "departmentName")
    private String departmentName;

    @Field(value = "level")
    private String level;

    @Field(value = "imageFilePath")
    private String imageFilePath;

    @Field(value = "isActive")
    private boolean isActive;

    @Field(name = "hireDate")
    private LocalDateTime hireDate;

    @Field(name = "retiredDate")
    private LocalDateTime retiredDate;

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
