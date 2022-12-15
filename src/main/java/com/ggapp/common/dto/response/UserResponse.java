package com.ggapp.common.dto.response;

import lombok.Data;

import java.util.Date;

/**
 * @author Tran Minh Truyen
 */
@Data
public class UserResponse {
	private int version;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Date birthDay;
	private String address;
	private String district;
	private String city;
	private String postCode;
	private String citizenID;
	private String imageFilePath;
	private String role;
	private boolean isActive;
}
