package com.ggapp.controller;

import com.ggapp.common.dto.request.EmployeeRequest;
import com.ggapp.common.dto.response.BaseResponse;
import com.ggapp.common.dto.response.EmployeeResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.ggapp.common.enums.MessageResponse.EMPLOYEE_CREATED_SUCCESS;
import static com.ggapp.common.enums.MessageResponse.GET_PROFILE_EMPLOYEE_SUCCESS;

@Tag(name = "EmployeeResource")
@RestController(value = "EmployeeResource")
@CrossOrigin("*")
@RequestMapping("api/employee")
public class EmployeeResource extends CommonResource{

    @Autowired
    private EmployeeService employeeService;

    /**
     *
     * @param employeeRequest
     * @param confirmKey
     * @return
     * @throws ApplicationException
     */
    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            summary = "This is API to create employee, a confirm key will be sent to email to activate user")
    @PostMapping(value = "createEmployee", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse createUser(@Valid @RequestBody EmployeeRequest employeeRequest,
                                   @RequestParam String confirmKey) throws ApplicationException {
        EmployeeResponse employeeResponse = employeeService.createEmployee(employeeRequest, confirmKey);
        return this.returnBaseReponse(employeeResponse, EMPLOYEE_CREATED_SUCCESS);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    },
            summary = "This is API to get profile employee")
    @PreAuthorize("hasAnyRole('ROLE_EMP', 'ROLE_ADMIN')")
    @PostMapping(value = "getEmployeeProfile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse getEmployeeProfile() throws ApplicationException {
        this.getAuthentication();
        EmployeeResponse employeeResponse = employeeService.getProfileEmployee(customUserDetail);
        return this.returnBaseReponse(employeeResponse, GET_PROFILE_EMPLOYEE_SUCCESS);
    }
}
