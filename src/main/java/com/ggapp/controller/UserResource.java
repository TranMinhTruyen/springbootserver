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
import com.ggapp.services.SessionService;
import com.ggapp.services.UserService;
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

import static com.ggapp.common.enums.MessageResponse.ACCESS_DENIED;
import static com.ggapp.common.enums.MessageResponse.DELETED_USER_SUCCESS;
import static com.ggapp.common.enums.MessageResponse.DEVICE_INFO_INVALID;
import static com.ggapp.common.enums.MessageResponse.EMAIL_SEND_SUCCESS;
import static com.ggapp.common.enums.MessageResponse.GET_PROFILE_USER_SUCCESS;
import static com.ggapp.common.enums.MessageResponse.GET_USER_SUCCESS;
import static com.ggapp.common.enums.MessageResponse.LOGIN_VALID;
import static com.ggapp.common.enums.MessageResponse.LOGOUT_USER_SUCCESS;
import static com.ggapp.common.enums.MessageResponse.UPDATE_USER_SUCCESS;
import static com.ggapp.common.enums.MessageResponse.USER_CREATED_SUCCESS;

/**
 * @author Tran Minh Truyen
 */

@Tag(name = "UserResource")
@RestController(value = "UserResource")
@CrossOrigin("*")
@RequestMapping("api/user")
public class UserResource extends CommonResource {
    @Autowired
    private UserService userService;

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            summary = "This is API to create user, a confirm key will be sent to email to activate user")
    @PostMapping(value = "createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse createUser(@Valid @RequestBody UserRequest userRequest, @RequestParam String confirmKey) throws ApplicationException {
        UserResponse userResponse = userService.createUser(userRequest, confirmKey);
        return this.returnBaseReponse(userResponse, USER_CREATED_SUCCESS);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserSucessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "getAllUser")
    public BaseResponse getAllUser(@RequestParam int page, @RequestParam int size) throws ApplicationException {
        CommonResponse response = userService.getAllUser(page, size);
        return this.returnBaseReponse(response, GET_USER_SUCCESS);
    }

    @Operation(responses = @ApiResponse(responseCode = "200"))
    @GetMapping(value = "getUserByKeyword")
    public BaseResponse getUserByKeyword(@RequestParam int page,
                                         @RequestParam int size,
                                         @RequestParam(required = false) String keyword) throws ApplicationException {
        CommonResponse response = userService.getUserByKeyWord(page, size, keyword);
        return this.returnBaseReponse(response, GET_USER_SUCCESS);
    }

    @Operation(responses = @ApiResponse(responseCode = "200"),
            security = {@SecurityRequirement(name = "Authorization")},
            summary = "This is API to update user infomation, user must login first to use this API")
    @PutMapping(value = "updateUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse updateUser(@Valid @RequestBody UserRequest userRequest) throws ApplicationException {
        this.getAuthentication();
        UserResponse userResponse = userService.updateUser(this.customUserDetail.getAccountDetail().getOwnerId(), userRequest);
        return this.returnBaseReponse(userResponse, UPDATE_USER_SUCCESS);
    }

    @Operation(responses = @ApiResponse(responseCode = "200"),
            security = {@SecurityRequirement(name = "Authorization")})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "deleteUser")
    public BaseResponse deleteUser(@RequestParam int id) throws ApplicationException {
        userService.deleteUser(id);
        return this.returnBaseReponse(null, DELETED_USER_SUCCESS);
    }

    @Operation(responses = @ApiResponse(responseCode = "200"),
            security = {@SecurityRequirement(name = "Authorization")},
            summary = "This is API to get user profile, user must login first to use this API")
    @PostMapping(value = "getProfileUser")
    public BaseResponse getProfileUser() throws ApplicationException {
        this.getAuthentication();
        UserResponse userResponse = userService.getProfileUser(this.customUserDetail.getAccountDetail().getOwnerId());
        return this.returnBaseReponse(userResponse, GET_PROFILE_USER_SUCCESS);
    }
}
