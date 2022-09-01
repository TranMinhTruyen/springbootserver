package com.example.services.implement;

import com.example.common.dto.request.DeviceInfoRequest;
import com.example.common.dto.request.LoginRequest;
import com.example.common.exception.ApplicationException;
import com.example.common.jwt.CustomUserDetail;
import com.example.common.jwt.JWTTokenProvider;
import com.example.dao.document.DeviceInfo;
import com.example.dao.document.Session;
import com.example.dao.document.User;
import com.example.dao.repository.mongo.SessionRepository;
import com.example.services.SessionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.common.utils.MessageError.DEVICE_INFO_INVALID;
import static com.example.common.utils.MessageError.SESSION_NOT_FOUND;

/**
 * @author Tran Minh Truyen
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE = 200
 */

@Service
public class SessionServicesImplement implements SessionServices {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;


    @Override
    public String checkExists(User user, LoginRequest loginRequest) throws ApplicationException {
        Optional<Session> session = sessionRepository.findById(user.getId());
        session.orElseThrow(() -> new ApplicationException(SESSION_NOT_FOUND));
        DeviceInfo deviceInfo = null;
        for (DeviceInfo item : session.get().getDeviceInfoList()) {
            if (item.getOS().equals(loginRequest.getDeviceInfo().getOS())
                    && item.getDeviceName().equals(loginRequest.getDeviceInfo().getDeviceName())) {
                deviceInfo = new DeviceInfo();
                deviceInfo.setDeviceName(item.getDeviceName());
                deviceInfo.setOS(item.getOS());
                deviceInfo.setToken(item.getToken());
                break;
            }
        }
        if (deviceInfo != null) {
            return deviceInfo.getToken();
        } else
            throw new ApplicationException(DEVICE_INFO_INVALID);
    }

    @Override
    public void checkSession(CustomUserDetail customUserDetail, DeviceInfoRequest deviceInfoRequest) throws ApplicationException {
        Optional<Session> session = sessionRepository.findById(customUserDetail.getUser().getId());
        if (session.isPresent() &&
                (session.get().getDeviceInfoList().stream().noneMatch(deviceInfo -> deviceInfo.getOS().equals(deviceInfoRequest.getOS())) ||
                        session.get().getDeviceInfoList().stream().noneMatch(deviceInfo -> deviceInfo.getDeviceName().equals(deviceInfoRequest.getDeviceName())))) {
            throw new ApplicationException(DEVICE_INFO_INVALID);
        }
    }

    @Override
    public void checkSessionAndLogoutDevice(CustomUserDetail customUserDetail, DeviceInfoRequest deviceInfoRequest, String token)
            throws ApplicationException {
        Optional<Session> session = sessionRepository.findById(customUserDetail.getUser().getId());
        session.orElseThrow(() -> new ApplicationException(SESSION_NOT_FOUND));
        DeviceInfo deviceInfo = null;
        for (DeviceInfo item : session.get().getDeviceInfoList()) {
            if (item.getOS().equals(deviceInfoRequest.getOS()) && item.getDeviceName().equals(deviceInfoRequest.getDeviceName())
                    && item.getToken().equals(token)) {
                deviceInfo = new DeviceInfo();
                deviceInfo.setDeviceName(item.getDeviceName());
                deviceInfo.setOS(item.getOS());
                deviceInfo.setToken(item.getToken());
                break;
            }
        }
        if (deviceInfo != null) {
            session.get().getDeviceInfoList().remove(deviceInfo);
            sessionRepository.save(session.get());
        } else
            throw new ApplicationException(DEVICE_INFO_INVALID);
    }

    @Override
    public String checkSessionAndLoginAnotherDevice(User user, LoginRequest loginRequest) throws ApplicationException {
        CustomUserDetail customUserDetail = new CustomUserDetail(user);
        Optional<Session> session = sessionRepository.findById(user.getId());
        String jwt = jwtTokenProvider.generateToken(customUserDetail, loginRequest.isRemember());
        if (session.isPresent()) {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDeviceName(loginRequest.getDeviceInfo().getDeviceName());
            deviceInfo.setOS(loginRequest.getDeviceInfo().getOS());
            deviceInfo.setToken(jwt);
            session.get().getDeviceInfoList().add(deviceInfo);
            sessionRepository.save(session.get());
        } else {
            Session newSession = new Session();
            List<DeviceInfo> deviceInfoList = new ArrayList<>();
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDeviceName(loginRequest.getDeviceInfo().getDeviceName());
            deviceInfo.setOS(loginRequest.getDeviceInfo().getOS());
            deviceInfo.setToken(jwt);
            deviceInfoList.add(deviceInfo);
            newSession.setId(customUserDetail.getUser().getId());
            newSession.setDeviceInfoList(deviceInfoList);
            newSession.setCreatedDate(new Date());
            sessionRepository.save(newSession);
        }
        return jwt;
    }
}
