package com.example.services;


import com.example.common.dto.request.DeviceInfoRequest;
import com.example.common.dto.request.LoginRequest;
import com.example.common.exception.ApplicationException;
import com.example.common.jwt.CustomUserDetail;
import com.example.dao.document.User;

public interface SessionServices {
    void checkSession(CustomUserDetail customUserDetail, DeviceInfoRequest deviceInfoRequest) throws ApplicationException;
    void logoutDevice(CustomUserDetail customUserDetail, DeviceInfoRequest deviceInfoRequest, String token) throws ApplicationException;
    String createJWTAndSession(User user, LoginRequest loginRequest) throws ApplicationException;
    String getJWTFromSession(User user, LoginRequest loginRequest);
    void checkSessionAndDeviceInfo(User user, LoginRequest loginRequest) throws ApplicationException;
}
