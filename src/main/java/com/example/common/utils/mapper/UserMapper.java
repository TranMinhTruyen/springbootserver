package com.example.common.utils.mapper;

import com.example.common.dto.request.UserRequest;
import com.example.common.dto.response.UserResponse;
import com.example.dao.document.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper extends EntityMapper<UserResponse, UserRequest, User>{
}
