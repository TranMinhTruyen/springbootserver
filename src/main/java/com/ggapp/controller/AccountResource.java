package com.ggapp.controller;

import com.ggapp.common.dto.request.ChangePasswordRequest;
import com.ggapp.common.dto.request.CheckEmailRequest;
import com.ggapp.common.dto.request.DeviceInfoRequest;
import com.ggapp.common.dto.request.LoginRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.JwtResponse;
import com.ggapp.common.dto.response.UserResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.dao.document.ResetPassword;
import com.ggapp.services.AccountService;
import com.ggapp.services.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ggapp.common.enums.MessageResponse.ACCESS_DENIED;
import static com.ggapp.common.enums.MessageResponse.DEVICE_INFO_INVALID;
import static com.ggapp.common.enums.MessageResponse.EMAIL_SEND_SUCCESS;
import static com.ggapp.common.enums.MessageResponse.LOGIN_VALID;
import static com.ggapp.common.enums.MessageResponse.LOGOUT_USER_SUCCESS;

/**
 * @author Tran Minh Truyen on 04/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */

@Tag(name = "AccountResource")
@RestController(value = "AccountResource")
@CrossOrigin("*")
@RequestMapping("api/account")
public class AccountResource extends CommonResource {

    @Autowired
    private AccountService accountService;

    @Autowired
    private SessionService sessionService;

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse login(@Valid @RequestBody LoginRequest loginRequest,
                              @RequestParam(required = false) String confirmKey)
            throws ApplicationException {
        JwtResponse jwtResponse = accountService.loginAnotherDevice(loginRequest, confirmKey);
        return this.returnBaseReponse(jwtResponse, LOGIN_VALID);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "loginAnotherDeviceSendConfirmKey", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse loginAnotherDeviceSendConfirmKey(@Valid @RequestBody LoginRequest loginRequest) throws ApplicationException {
        accountService.sendEmailLoginConfirmKey(loginRequest);
        return this.returnBaseReponse(null, EMAIL_SEND_SUCCESS);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(value = "checkLoginStatus")
    public BaseResponse checkLoginStatus(@Valid @RequestBody DeviceInfoRequest deviceInfoRequest) throws ApplicationException {
        sessionService.checkSession(this.customUserDetail, deviceInfoRequest);
        return this.returnBaseReponse(null, DEVICE_INFO_INVALID);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(value = "logout")
    public BaseResponse logout(@Valid @RequestBody DeviceInfoRequest deviceInfoRequest,
                               HttpServletRequest request) throws ApplicationException {
        this.getAuthentication();
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        } else throw new ApplicationException(ACCESS_DENIED);
        sessionService.logoutDevice(this.customUserDetail, deviceInfoRequest, bearerToken);
        return this.returnBaseReponse(null, LOGOUT_USER_SUCCESS);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            summary = "This is API to reset password, a new password will be sent to email")
    @PostMapping(value = "resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse resetPassword(@Valid @RequestBody ResetPassword resetPassword) throws ApplicationException {
        UserResponse userResponse = accountService.resetPassword(resetPassword.getEmail());
        return this.returnBaseReponse(userResponse, EMAIL_SEND_SUCCESS);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            summary = "This is API to reset password, a new password will be sent to email")
    @PostMapping(value = "changePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws ApplicationException {
        return this.returnBaseReponse(null, EMAIL_SEND_SUCCESS);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "sendConfirmKey")
    public BaseResponse sendConfirmKey(@RequestBody CheckEmailRequest checkEmailRequest) throws ApplicationException {
        accountService.sendEmailRegisterConfirmKey(checkEmailRequest.getEmail());
        return this.returnBaseReponse(null, EMAIL_SEND_SUCCESS);
    }
}
