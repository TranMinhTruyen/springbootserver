package com.ggapp.dao.document;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
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

@Document(collection = "Account")
@Data
public class Account {
    private Long id;

    @Version
    private int version;

    @Field(value = "account")
    private String account;

    @Field(value = "mail")
    private String email;

    @Field(value = "password")
    private String password;

    @Field(value = "ownerId")
    private Long ownerId;

    @Field(value = "accountType")
    private String accountType;

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
