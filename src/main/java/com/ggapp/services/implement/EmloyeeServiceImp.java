package com.ggapp.services.implement;

import com.ggapp.common.dto.request.EmployeeRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.EmployeeResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.dao.repository.mongo.EmployeeRepository;
import com.ggapp.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmloyeeServiceImp implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest, String confirmKey) throws ApplicationException {
        return null;
    }

    @Override
    public CommonResponsePayload getAllEmployee(int page, int size) throws ApplicationException {
        return null;
    }

    @Override
    public CommonResponsePayload getEmployeeByKeyWord(int page, int size, String keyword) throws ApplicationException {
        return null;
    }

    @Override
    public EmployeeResponse getProfileEmployee(Long id) throws ApplicationException {
        return null;
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) throws ApplicationException {
        return null;
    }

    @Override
    public boolean logicDeletedEmployee(Long id) throws ApplicationException {
        return false;
    }

    @Override
    public boolean physicDeletedEmployee(Long id) throws ApplicationException {
        return false;
    }
}
