package com.ggapp.common.utils.mapper;

import com.ggapp.common.dto.request.UserRequest;
import com.ggapp.common.dto.response.UserResponse;
import com.ggapp.dao.document.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper extends EntityMapper<UserResponse, UserRequest, User>{
}
