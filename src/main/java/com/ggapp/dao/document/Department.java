package com.ggapp.dao.document;

import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * @author Tran Minh Truyen on 04/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
public class Department {

    private Long id;

    @Version
    private int version;

    @Field(value = "managerId")
    private String managerId;

    @Field(value = "managerName")
    private String managerName;

    @Field(value = "deptName")
    private String deptName;

    @Field(value = "deptCode")
    private String deptCode;

    @Field(value = "address")
    private String address;

    @Field(value = "status")
    private String status;

    @Field(name = "startDate")
    private LocalDateTime startDate;

    @Field(name = "endDate")
    private LocalDateTime endDate;

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
