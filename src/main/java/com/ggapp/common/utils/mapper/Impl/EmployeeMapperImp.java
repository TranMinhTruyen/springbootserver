package com.ggapp.common.utils.mapper.Impl;

import com.ggapp.common.dto.request.EmployeeRequest;
import com.ggapp.common.dto.response.EmployeeResponse;
import com.ggapp.common.utils.CommonUtils;
import com.ggapp.common.utils.mapper.EmployeeMapper;
import com.ggapp.dao.document.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ggapp.common.utils.Constant.DATE_TIME_FORMAT_PATTERN;
import static com.ggapp.common.utils.Constant.DATE_FORMAT_PATTERN;

@Component
public class EmployeeMapperImp implements EmployeeMapper {

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public Employee requestToEntity(EmployeeRequest employeeRequest) {
        if (employeeRequest == null)
            return null;
        Employee employee = new Employee();
        employee.setFullName(employeeRequest.getFullName());
        employee.setBirthDay(commonUtils.convertDateStringToLocalDateTime(employeeRequest.getBirthDay(),
                DATE_FORMAT_PATTERN));
        employee.setEmail(employeeRequest.getEmail());
        employee.setPhoneNumber(employeeRequest.getPhoneNumber());
        employee.setCitizenId(employeeRequest.getCitizenID());
        employee.setAddress(employeeRequest.getAddress());
        employee.setDistrict(employeeRequest.getDistrict());
        employee.setCity(employeeRequest.getCity());
        employee.setRole(employeeRequest.getRole());
        employee.setAuthorities(employeeRequest.getAuthorities());
        employee.setPosition(employeeRequest.getPosition());
        employee.setLevel(employeeRequest.getLevel());
        employee.setDepartmentName(employeeRequest.getDepartmentName());
        employee.setHireDate(commonUtils.convertDateStringToLocalDateTime(employeeRequest.getBirthDay(), DATE_FORMAT_PATTERN));
        employee.setRetiredDate(commonUtils.convertDateStringToLocalDateTime(employeeRequest.getBirthDay(), DATE_FORMAT_PATTERN));
        return employee;
    }

    @Override
    public EmployeeResponse entityToResponse(Employee employee) {
        if (employee == null)
            return null;
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setFullName(employee.getFullName());
        employeeResponse.setBirthDay(commonUtils.convertLocalDateTimeToDateString(employee.getBirthDay(), DATE_FORMAT_PATTERN));
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setPhoneNumber(employee.getPhoneNumber());
        employeeResponse.setCitizenID(employee.getCitizenId());
        employeeResponse.setAddress(employee.getAddress());
        employeeResponse.setDistrict(employee.getDistrict());
        employeeResponse.setCity(employee.getCity());
        employeeResponse.setImageFilePath(employee.getImageFilePath());
        employeeResponse.setRole(employee.getRole());
        employeeResponse.setAuthorities(employee.getAuthorities());
        employeeResponse.setActive(employee.isActive());
        employeeResponse.setPosition(employee.getPosition());
        employeeResponse.setDepartmentName(employee.getDepartmentName());
        employeeResponse.setLevel(employee.getLevel());
        employeeResponse.setHireDate(commonUtils.convertLocalDateTimeToDateString(employee.getHireDate(), DATE_FORMAT_PATTERN));
        employeeResponse.setRetiredDate(commonUtils.convertLocalDateTimeToDateString(employee.getRetiredDate(), DATE_FORMAT_PATTERN));
        return employeeResponse;
    }

    @Override
    public List<Employee> requestToEntity(List<EmployeeRequest> employeeRequests) {
        if (employeeRequests.isEmpty())
            return null;
        List<Employee> employeeList = new ArrayList<>(employeeRequests.size());
        for (EmployeeRequest employeeRequest: employeeRequests) {
            employeeList.add(requestToEntity(employeeRequest));
        }
        return employeeList;
    }

    @Override
    public List<EmployeeResponse> entityToResponse(List<Employee> employees) {
        if (employees.isEmpty())
            return null;
        List<EmployeeResponse> employeeResponseList = new ArrayList<>(employees.size());
        for (Employee employee: employees) {
            employeeResponseList.add(entityToResponse(employee));
        }
        return employeeResponseList;
    }
}
