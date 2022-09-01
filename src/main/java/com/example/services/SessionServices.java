package com.example.services;


import com.example.common.dto.request.DeviceInfoRequest;
import com.example.common.dto.request.LoginRequest;
import com.example.common.exception.ApplicationException;
import com.example.common.jwt.CustomUserDetail;
import com.example.dao.document.Session;
import com.example.dao.document.User;

import javax.servlet.http.HttpServletRequest;

public interface SessionServices {
    void checkSession(CustomUserDetail customUserDetail, DeviceInfoRequest deviceInfoRequest) throws ApplicationException;
    void checkSessionAndLogoutDevice(CustomUserDetail customUserDetail, DeviceInfoRequest deviceInfoRequest, String token) throws ApplicationException;
    String checkSessionAndLoginAnotherDevice(User user, LoginRequest loginRequest) throws ApplicationException;
    String checkExists(User user, LoginRequest loginRequest) throws ApplicationException;
}
