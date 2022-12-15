package com.ggapp.common.jwt;

import lombok.Data;

/**
 * @author Tran Minh Truyen on 04/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
@Data
public class AccountDetail {
    private Long ownerId;
    private String account;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String position;
    private String departmentName;
    private boolean isActive;
}
