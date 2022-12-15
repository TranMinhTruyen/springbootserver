package com.ggapp.common.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Tran Minh Truyen
 */
@Data
public class UserResponse {
	private int version;
	private String fullName;
	private String email;
	private String phoneNumber;
	private String birthDay;
	private String address;
	private String district;
	private String city;
	private String postCode;
	private String citizenID;
	private String imageFilePath;
	private String role;
}
