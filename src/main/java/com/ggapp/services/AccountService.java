package com.ggapp.services;

import com.ggapp.common.dto.request.LoginRequest;
import com.ggapp.common.dto.request.UserRequest;
import com.ggapp.common.dto.response.JwtResponse;
import com.ggapp.common.dto.response.UserResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.dao.document.Employee;
import com.ggapp.dao.document.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Tran Minh Truyen on 04/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
public interface AccountService {
    void createUserAccount(User user, UserRequest userRequest);
    void createEmployeeAccount(Employee employee);
    JwtResponse loginAnotherDevice(LoginRequest loginRequest, String confirmKey) throws ApplicationException;
    UserResponse resetPassword(String email) throws ApplicationException;
    boolean accountIsExists(String account);
    UserDetails loadUserById(int id) throws ApplicationException;
    void sendEmailRegisterConfirmKey(String email) throws ApplicationException;
    void sendEmailLoginConfirmKey(LoginRequest loginRequest) throws ApplicationException;
}
