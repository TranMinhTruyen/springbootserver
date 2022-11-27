package com.ggapp.common.config.CustomSchema.UserSchema;

import com.ggapp.common.dto.response.UserResponse;
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
