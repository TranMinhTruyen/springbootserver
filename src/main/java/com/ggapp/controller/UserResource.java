package com.ggapp.controller;

import com.ggapp.common.config.CustomSchema.UserSchema.UserSucessResponse;
import com.ggapp.common.dto.request.DeviceInfoRequest;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.dao.document.ResetPassword;
import com.ggapp.common.dto.request.CheckEmailRequest;
import com.ggapp.common.dto.request.LoginRequest;
import com.ggapp.common.dto.request.UserRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.common.dto.response.JwtResponse;
import com.ggapp.common.dto.response.UserResponse;
import com.ggapp.services.SessionServices;
import com.ggapp.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ggapp.common.commonenum.MessageResponse.ACCESS_DENIED;
import static com.ggapp.common.commonenum.MessageResponse.DELETED_USER_SUCCESS;
import static com.ggapp.common.commonenum.MessageResponse.DEVICE_INFO_INVALID;
import static com.ggapp.common.commonenum.MessageResponse.EMAIL_SEND_SUCCESS;
import static com.ggapp.common.commonenum.MessageResponse.GET_PROFILE_USER_SUCCESS;
import static com.ggapp.common.commonenum.MessageResponse.GET_USER_SUCCESS;
import static com.ggapp.common.commonenum.MessageResponse.LOGIN_VALID;
import static com.ggapp.common.commonenum.MessageResponse.LOGOUT_USER_SUCCESS;
import static com.ggapp.common.commonenum.MessageResponse.UPDATE_USER_SUCCESS;
import static com.ggapp.common.commonenum.MessageResponse.USER_CREATED_SUCCESS;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "UserResource")
@RestController(value = "UserResource")
@CrossOrigin("*")
@RequestMapping("api/user")
public class UserResource extends CommonResource {
    @Autowired
    private UserServices userServices;

    @Autowired
    private SessionServices sessionServices;

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse login(@Valid @RequestBody LoginRequest loginRequest,
                              @RequestParam(required = false) String confirmKey)
            throws ApplicationException {
        JwtResponse jwtResponse = userServices.loginAnotherDevice(loginRequest, confirmKey);
        return this.returnBaseReponse(jwtResponse, LOGIN_VALID);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "loginAnotherDeviceSendConfirmKey", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse loginAnotherDeviceSendConfirmKey(@Valid @RequestBody LoginRequest loginRequest) throws ApplicationException {
        userServices.sendEmailConfirmKey(loginRequest);
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
        sessionServices.checkSession(this.customUserDetail, deviceInfoRequest);
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
        sessionServices.logoutDevice(this.customUserDetail, deviceInfoRequest, bearerToken);
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
        UserResponse userResponse = userServices.resetPassword(resetPassword.getEmail());
        return this.returnBaseReponse(userResponse, EMAIL_SEND_SUCCESS);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "sendConfirmKey")
    public BaseResponse sendConfirmKey(@RequestBody CheckEmailRequest checkEmailRequest) throws ApplicationException {
        userServices.sendEmailConfirmKey(checkEmailRequest.getEmail());
        return this.returnBaseReponse(null, EMAIL_SEND_SUCCESS);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            summary = "This is API to create user, a confirm key will be sent to email to activate user")
    @PostMapping(value = "createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse createUser(@Valid @RequestBody UserRequest userRequest, @RequestParam String confirmKey) throws ApplicationException {
        UserResponse userResponse = userServices.createUser(userRequest, confirmKey);
        return this.returnBaseReponse(userResponse, USER_CREATED_SUCCESS);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserSucessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "getAllUser")
    public BaseResponse getAllUser(@RequestParam int page, @RequestParam int size) throws ApplicationException {
        CommonResponse response = userServices.getAllUser(page, size);
        return this.returnBaseReponse(response, GET_USER_SUCCESS);
    }

    @Operation(responses = @ApiResponse(responseCode = "200"))
    @GetMapping(value = "getUserByKeyword")
    public BaseResponse getUserByKeyword(@RequestParam int page,
                                         @RequestParam int size,
                                         @RequestParam(required = false) String keyword) throws ApplicationException {
        CommonResponse response = userServices.getUserByKeyWord(page, size, keyword);
        return this.returnBaseReponse(response, GET_USER_SUCCESS);
    }

    @Operation(responses = @ApiResponse(responseCode = "200"),
            security = {@SecurityRequirement(name = "Authorization")},
            summary = "This is API to update user infomation, user must login first to use this API")
    @PutMapping(value = "updateUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse updateUser(@Valid @RequestBody UserRequest userRequest) throws ApplicationException {
        this.getAuthentication();
        UserResponse userResponse = userServices.updateUser(this.customUserDetail.getUser().getId(), userRequest);
        return this.returnBaseReponse(userResponse, UPDATE_USER_SUCCESS);
    }

    @Operation(responses = @ApiResponse(responseCode = "200"),
            security = {@SecurityRequirement(name = "Authorization")})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "deleteUser")
    public BaseResponse deleteUser(@RequestParam int id) throws ApplicationException {
        userServices.deleteUser(id);
        return this.returnBaseReponse(null, DELETED_USER_SUCCESS);
    }

    @Operation(responses = @ApiResponse(responseCode = "200"),
            security = {@SecurityRequirement(name = "Authorization")},
            summary = "This is API to get user profile, user must login first to use this API")
    @PostMapping(value = "getProfileUser")
    public BaseResponse getProfileUser() throws ApplicationException {
        this.getAuthentication();
        UserResponse userResponse = userServices.getProfileUser(this.customUserDetail.getUser().getId());
        return this.returnBaseReponse(userResponse, GET_PROFILE_USER_SUCCESS);
    }
}
