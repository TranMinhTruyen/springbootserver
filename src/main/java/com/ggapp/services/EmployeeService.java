package com.ggapp.services;

import com.ggapp.common.dto.request.EmployeeRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.EmployeeResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest, String confirmKey) throws ApplicationException;
    CommonResponsePayload getAllEmployee(int page, int size) throws ApplicationException;
    CommonResponsePayload getEmployeeByKeyWord(int page, int size, String keyword) throws ApplicationException;
    EmployeeResponse getProfileEmployee(CustomUserDetail customUserDetail) throws ApplicationException;
    EmployeeResponse updateInfoEmployee(CustomUserDetail customUserDetail, EmployeeRequest employeeRequest) throws ApplicationException;
    EmployeeResponse updateEmployee(CustomUserDetail customUserDetail, EmployeeRequest employeeRequest) throws ApplicationException;
    boolean logicDeletedEmployee(int id) throws ApplicationException;
    boolean physicDeletedEmployee(int id) throws ApplicationException;
}
