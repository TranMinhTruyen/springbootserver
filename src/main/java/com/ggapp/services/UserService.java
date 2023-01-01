package com.ggapp.services;
import com.ggapp.common.dto.request.UserRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.UserResponse;

import com.ggapp.common.exception.ApplicationException;

/**
 * @author Tran Minh Truyen
 */
public interface UserService {
	UserResponse createUser(UserRequest userRequest, String confirmKey) throws ApplicationException;
	CommonResponsePayload getAllUser(int page, int size) throws ApplicationException;
	CommonResponsePayload getUserByKeyWord(int page, int size, String keyword) throws ApplicationException;
	UserResponse getProfileUser(int id) throws ApplicationException;
	UserResponse updateUser(int id, UserRequest request) throws ApplicationException;
	boolean deleteUser(int id) throws ApplicationException;
}
