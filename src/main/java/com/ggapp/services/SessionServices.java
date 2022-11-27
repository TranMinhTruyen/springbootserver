package com.ggapp.services;


import com.ggapp.common.dto.request.DeviceInfoRequest;
import com.ggapp.common.dto.request.LoginRequest;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.dao.document.User;

public interface SessionServices {
    void checkSession(CustomUserDetail customUserDetail, DeviceInfoRequest deviceInfoRequest) throws ApplicationException;
    void logoutDevice(CustomUserDetail customUserDetail, DeviceInfoRequest deviceInfoRequest, String token) throws ApplicationException;
    String createJWTAndSession(User user, LoginRequest loginRequest) throws ApplicationException;
    String getJWTFromSession(User user, LoginRequest loginRequest);
    void checkSessionAndDeviceInfo(User user, LoginRequest loginRequest) throws ApplicationException;
}
