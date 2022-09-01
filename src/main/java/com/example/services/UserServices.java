package com.example.services;
import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.request.UserRequest;
import com.example.common.dto.response.CommonResponse;
import com.example.common.dto.response.JwtResponse;
import com.example.common.dto.response.UserResponse;

import com.example.common.exception.ApplicationException;
import com.example.dao.document.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Tran Minh Truyen
 */
public interface UserServices {
	UserResponse createUser(UserRequest userRequest, String confirmKey) throws ApplicationException;
	CommonResponse getAllUser(int page, int size) throws ApplicationException;
	CommonResponse getUserByKeyWord(int page, int size, String keyword) throws ApplicationException;
	JwtResponse login(LoginRequest loginRequest) throws ApplicationException;
	JwtResponse loginAnotherDevice(LoginRequest loginRequest, String confirmKey) throws ApplicationException;
	UserResponse resetPassword(String email) throws ApplicationException;
	UserResponse getProfileUser(int id) throws ApplicationException;
	void sendEmailConfirmKey(String email) throws ApplicationException;
	void sendEmailConfirmKey(LoginRequest loginRequest) throws ApplicationException;
	UserResponse updateUser(int id, UserRequest request) throws ApplicationException;
	boolean deleteUser(int id) throws ApplicationException;
	boolean accountIsExists(String account);
	UserDetails loadUserById(int id) throws ApplicationException;
}
