package com.ggapp.common.utils.mapper.Impl;

import com.ggapp.common.dto.request.UserRequest;
import com.ggapp.common.dto.response.UserResponse;
import com.ggapp.common.utils.Constant;
import com.ggapp.common.utils.mapper.UserMapper;
import com.ggapp.dao.document.User;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User requestToEntity(UserRequest userRequest) {
        if (userRequest == null)
            return null;
        User user = new User();
        user.setFullName(userRequest.getFullName());
        user.setBirthDay(LocalDateTime.parse(userRequest.getBirthDay(), DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT_PATTERN_BIRTHDAY)));
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setCitizenId(userRequest.getCitizenID());
        user.setAddress(userRequest.getAddress());
        user.setDistrict(userRequest.getDistrict());
        user.setCity(userRequest.getCity());
        user.setPostCode(userRequest.getPostCode());
        user.setRole(userRequest.getRole());
        return user;
    }

    @Override
    public UserResponse entityToResponse(User user) {
        if (user == null)
            return null;
        UserResponse userResponse = new UserResponse();
        userResponse.setFullName(user.getFullName());
        userResponse.setBirthDay(user.getBirthDay().format(DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT_PATTERN_BIRTHDAY)));
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setCitizenID(user.getCitizenId());
        userResponse.setAddress(user.getAddress());
        userResponse.setDistrict(user.getDistrict());
        userResponse.setCity(user.getCity());
        userResponse.setPostCode(user.getPostCode());
        userResponse.setImageFilePath(user.getImageFilePath());
        userResponse.setRole(user.getRole());
        userResponse.setActive(user.isActive());
        return userResponse;
    }

    @Override
    public List<User> requestToEntity(List<UserRequest> userRequests) {
        if (userRequests.isEmpty())
            return null;
        List<User> userList = new ArrayList<>(userRequests.size());
        for (UserRequest userRequest: userRequests) {
            userList.add(requestToEntity(userRequest));
        }
        return userList;
    }

    @Override
    public List<UserResponse> entityToResponse(List<User> users) {
        if (users.isEmpty())
            return null;
        List<UserResponse> userResponseList = new ArrayList<>(users.size());
        for (User userResponse: users) {
            userResponseList.add(entityToResponse(userResponse));
        }
        return userResponseList;
    }
}
