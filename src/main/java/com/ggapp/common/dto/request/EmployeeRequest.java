package com.ggapp.common.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmployeeRequest {
    private int version;

    @NotBlank(message = "account is mandatory")
    private String account;

    @NotBlank(message = "password is mandatory")
    private String password;

    @NotBlank(message = "lastName is mandatory")
    private String fullName;

    @NotBlank(message = "email is mandatory")
    @Pattern(regexp = "^(.+)@(.+)$")
    private String email;

    @NotBlank(message = "email is mandatory")
    @Length(min = 10, max = 10, message = "phone is mandatory and contain 10 number")
    private String phoneNumber;

    private String birthDay;

    @NotBlank(message = "address is mandatory")
    private String address;

    @NotBlank(message = "district is mandatory")
    private String district;

    @NotBlank(message = "city is mandatory")
    private String city;

    @NotBlank(message = "postCode is mandatory")
    private String postCode;

    @NotBlank(message = "citizenID is mandatory")
    private String citizenID;

    private String imageFileData;

    @NotBlank(message = "role is mandatory")
    private String role;

    @NotEmpty(message = "authorities is mandatory")
    private List<String> authorities;

    @NotBlank(message = "position is mandatory")
    private String position;

    @NotBlank(message = "department name is mandatory")
    private String departmentName;

    @NotBlank(message = "level is mandatory")
    private String level;

    private String hireDate;

    private String retiredDate;

    private boolean isActive;
}
