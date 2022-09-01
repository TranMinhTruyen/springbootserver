package com.example.common.config.CustomSchema.UserSchema;

import com.example.common.dto.response.UserResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public abstract class UserSucessResponse {
    private Date timestamp = new Date();
    private int status;
    private String statusname;
    private String message;
    private List<UserResponse> payload;
}
