package com.ggapp.controller;
import com.ggapp.common.config.CustomSchema.UserSchema.UserSucessResponse;
import com.ggapp.common.dto.request.DeviceInfoRequest;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.JWTTokenProvider;
import com.ggapp.common.jwt.CustomUserDetail;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "UserResource")
@RestController(value = "UserResource")
@CrossOrigin("*")
@RequestMapping("api/user")
public class UserResource {

    @Autowired
    private UserServices userServices;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private SessionServices sessionServices;

//    @Operation(responses = {
//            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class)) }),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "403", description = "Forbidden")
//    })
//    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public BaseResponse login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
//        BaseResponse baseResponse = new BaseResponse();
//        JwtResponse jwtResponse = userServices.login(loginRequest);
//        baseResponse.setStatus(HttpStatus.OK.value());
//        baseResponse.setStatusname(HttpStatus.OK.name());
//        baseResponse.setMessage("Login valid");
//        baseResponse.setPayload(jwtResponse);
//        return baseResponse;
//    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse login(@Valid @RequestBody LoginRequest loginRequest,
                                           @RequestParam(required = false) String confirmKey)
            throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        JwtResponse jwtResponse = userServices.loginAnotherDevice(loginRequest, confirmKey);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("Login valid");
        baseResponse.setPayload(jwtResponse);
        return baseResponse;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "loginAnotherDeviceSendConfirmKey", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse loginAnotherDeviceSendConfirmKey(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        userServices.sendEmailConfirmKey(loginRequest);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("Email has been sent");
        return baseResponse;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(value = "checkLoginStatus")
    public BaseResponse checkLoginStatus(@Valid @RequestBody DeviceInfoRequest deviceInfoRequest) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        BaseResponse baseResponse = new BaseResponse();
        sessionServices.checkSession(customUserDetail, deviceInfoRequest);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("Device info valid");
        return baseResponse;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(value = "logout")
    public BaseResponse logout(@Valid @RequestBody DeviceInfoRequest deviceInfoRequest,
                               HttpServletRequest request) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        } else throw new ApplicationException("Access denied", HttpStatus.UNAUTHORIZED);
        BaseResponse baseResponse = new BaseResponse();
        sessionServices.logoutDevice(customUserDetail, deviceInfoRequest, bearerToken);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("Logout successfully");
        return baseResponse;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            summary = "This is API to reset password, a new password will be sent to email")
    @PostMapping(value = "resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse resetPassword(@Valid @RequestBody ResetPassword resetPassword) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        UserResponse userResponse = userServices.resetPassword(resetPassword.getEmail());
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("Email has been sent");
        baseResponse.setPayload(userResponse);
        return baseResponse;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "sendConfirmKey")
    public BaseResponse sendConfirmKey(@RequestBody CheckEmailRequest checkEmailRequest) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        userServices.sendEmailConfirmKey(checkEmailRequest.getEmail());
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("Email has been sent");
        return baseResponse;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            summary = "This is API to create user, a confirm key will be sent to email to activate user")
    @PostMapping(value = "createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse createUser(@Valid @RequestBody UserRequest userRequest, @RequestParam String confirmKey) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        UserResponse userResponse = userServices.createUser(userRequest, confirmKey);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("User is added");
        baseResponse.setPayload(userResponse);
        return baseResponse;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserSucessResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value="getAllUser")
    public BaseResponse getAllUser(@RequestParam int page, @RequestParam int size) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        CommonResponse response = userServices.getAllUser(page, size);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("Get users success");
        baseResponse.setPayload(response);
        return baseResponse;
    }

    @Operation(responses = @ApiResponse(responseCode = "200"))
    @GetMapping(value="getUserByKeyword")
    public BaseResponse getUserByKeyword(@RequestParam int page,
                                         @RequestParam int size,
                                         @RequestParam(required = false) String keyword) throws ApplicationException {
        BaseResponse baseResponse = new BaseResponse();
        CommonResponse response = userServices.getUserByKeyWord(page, size, keyword);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("Get users success");
        baseResponse.setPayload(response);
        return baseResponse;
    }

    @Operation(responses = @ApiResponse(responseCode = "200"),
            security = {@SecurityRequirement(name = "Authorization")},
            summary = "This is API to update user infomation, user must login first to use this API")
    @PutMapping(value = "updateUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse updateUser(@Valid @RequestBody UserRequest userRequest) throws ApplicationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        BaseResponse baseResponse = new BaseResponse();
        UserResponse userResponse = userServices.updateUser(customUserDetail.getUser().getId(), userRequest);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusname(HttpStatus.OK.name());
        baseResponse.setMessage("User is update");
        baseResponse.setPayload(userResponse);
        return baseResponse;
    }

    @Operation(responses = @ApiResponse(responseCode = "200"),
            security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(value = "deleteUser")
    public BaseResponse deleteUser(@RequestParam int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BaseResponse baseResponse = new BaseResponse();
        if (authentication != null && (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER")) ||
                authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")))
        )
        {
            try {
                userServices.deleteUser(id);
                baseResponse.setStatus(HttpStatus.OK.value());
                baseResponse.setStatusname(HttpStatus.OK.name());
                baseResponse.setMessage("user is delete");
                return baseResponse;
            } catch (Exception exception) {
                baseResponse.setStatus(HttpStatus.FORBIDDEN.value());
                baseResponse.setStatusname(HttpStatus.FORBIDDEN.name());
                baseResponse.setMessage(exception.getMessage());
                return baseResponse;
            }
        }
        else{
            if (authentication == null) {
                baseResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                baseResponse.setStatusname(HttpStatus.UNAUTHORIZED.name());
                baseResponse.setMessage("Please login");
                return baseResponse;
            }
            else {
                baseResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                baseResponse.setStatusname(HttpStatus.UNAUTHORIZED.name());
                baseResponse.setMessage("You don't have permission");
                return baseResponse;
            }
        }
    }

    @Operation(responses = @ApiResponse(responseCode = "200"),
            security = {@SecurityRequirement(name = "Authorization")},
            summary = "This is API to get user profile, user must login first to use this API")
    @PostMapping(value = "getProfileUser")
    public BaseResponse getProfileUser() {
        BaseResponse baseResponse = new BaseResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        try {
            UserResponse userResponse = userServices.getProfileUser(customUserDetail.getUser().getId());
            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setStatusname(HttpStatus.OK.name());
            baseResponse.setMessage("Get profile success");
            baseResponse.setPayload(userResponse);
            return baseResponse;
        } catch (Exception exception) {
            baseResponse.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponse.setStatusname(HttpStatus.FORBIDDEN.name());
            baseResponse.setMessage(exception.getMessage());
            return baseResponse;
        }
    }
}
