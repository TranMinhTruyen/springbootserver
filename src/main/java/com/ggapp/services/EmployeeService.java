package com.ggapp.services;

import com.ggapp.common.dto.request.EmployeeRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.EmployeeResponse;
import com.ggapp.common.exception.ApplicationException;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest, String confirmKey) throws ApplicationException;
    CommonResponsePayload getAllEmployee(int page, int size) throws ApplicationException;
    CommonResponsePayload getEmployeeByKeyWord(int page, int size, String keyword) throws ApplicationException;
    EmployeeResponse getProfileEmployee(Long id) throws ApplicationException;
    EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) throws ApplicationException;
    boolean logicDeletedEmployee(Long id) throws ApplicationException;
    boolean physicDeletedEmployee(Long id) throws ApplicationException;
}
