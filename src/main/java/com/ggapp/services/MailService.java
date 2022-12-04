package com.ggapp.services;

import com.ggapp.common.dto.request.LoginRequest;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.dao.document.User;

/**
 * @author Tran Minh Truyen on 03/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
public interface MailService {
    void sendEmailRegisterConfirmKey(String email, String key) throws ApplicationException;
    void sendEmailLoginConfirmKey(LoginRequest loginRequest) throws ApplicationException;
}
