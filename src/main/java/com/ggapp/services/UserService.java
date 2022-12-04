package com.ggapp.services;
import com.ggapp.common.dto.request.LoginRequest;
import com.ggapp.common.dto.request.UserRequest;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.common.dto.response.JwtResponse;
import com.ggapp.common.dto.response.UserResponse;

import com.ggapp.common.exception.ApplicationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Tran Minh Truyen
 */
public interface UserService {
	UserResponse createUser(UserRequest userRequest, String confirmKey) throws ApplicationException;
	CommonResponse getAllUser(int page, int size) throws ApplicationException;
	CommonResponse getUserByKeyWord(int page, int size, String keyword) throws ApplicationException;
	UserResponse getProfileUser(int id) throws ApplicationException;
	UserResponse updateUser(int id, UserRequest request) throws ApplicationException;
	boolean deleteUser(int id) throws ApplicationException;
}
