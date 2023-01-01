package com.ggapp.common.utils.mapper;

import com.ggapp.common.dto.request.EmployeeRequest;
import com.ggapp.common.dto.response.EmployeeResponse;
import com.ggapp.dao.document.Employee;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeMapper extends EntityMapper<EmployeeResponse, EmployeeRequest, Employee>{
}
