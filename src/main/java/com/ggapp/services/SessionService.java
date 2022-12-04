package com.ggapp.services;


import com.ggapp.common.dto.request.DeviceInfoRequest;
import com.ggapp.common.dto.request.LoginRequest;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.AccountDetail;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.dao.document.Account;

public interface SessionService {
    void checkSession(CustomUserDetail customUserDetail, DeviceInfoRequest deviceInfoRequest) throws ApplicationException;
    void logoutDevice(CustomUserDetail customUserDetail, DeviceInfoRequest deviceInfoRequest, String token) throws ApplicationException;
    String createJWTAndSession(AccountDetail accountDetail, LoginRequest loginRequest) throws ApplicationException;
    String getJWTFromSession(AccountDetail accountDetail, LoginRequest loginRequest);
    boolean checkSessionAndDeviceInfo(AccountDetail accountDetail, LoginRequest loginRequest) throws ApplicationException;
}
