package com.ggapp.controller;

import com.ggapp.common.enums.MessageResponse;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.jwt.CustomUserDetail;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Nullable;

/**
 * @author Tran Minh Truyen on 30/11/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
public abstract class CommonResource {

    protected CustomUserDetail customUserDetail;

    protected void getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.customUserDetail = (CustomUserDetail) authentication.getPrincipal();
    }

    protected BaseResponse returnBaseReponse (@Nullable Object payload, @Nullable String message, @NonNull HttpStatus httpStatus) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(httpStatus.value());
        baseResponse.setStatusname(httpStatus.name());
        baseResponse.setMessage(message);
        baseResponse.setPayload(payload);
        return baseResponse;
    }

    protected BaseResponse returnBaseReponse (@Nullable Object payload, MessageResponse message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(message.getHttpStatus().value());
        baseResponse.setStatusname(message.getHttpStatus().name());
        baseResponse.setMessage(message.getMessage());
        baseResponse.setPayload(payload);
        return baseResponse;
    }
}
