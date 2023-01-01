package com.ggapp.common.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeResponse {
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
    private List<String> authorities;
    private String position;
    private String departmentName;
    private String level;
    private String hireDate;
    private String retiredDate;
    private boolean isActive;
}
