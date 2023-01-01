package com.ggapp.services.implement;

import com.ggapp.common.dto.request.EmployeeRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.EmployeeResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.utils.CommonUtils;
import com.ggapp.common.utils.Constant;
import com.ggapp.common.utils.FileUtils;
import com.ggapp.common.utils.mapper.EmployeeMapper;
import com.ggapp.dao.document.Account;
import com.ggapp.dao.document.AutoIncrement;
import com.ggapp.dao.document.ConfirmKey;
import com.ggapp.dao.document.Employee;
import com.ggapp.dao.repository.mongo.ConfirmKeyRepository;
import com.ggapp.dao.repository.mongo.EmployeeRepository;
import com.ggapp.services.AccountService;
import com.ggapp.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ggapp.common.enums.MessageResponse.CONFIRM_KEY_INVALID;
import static com.ggapp.common.enums.MessageResponse.EMPLOYEE_IS_EXIST;
import static com.ggapp.common.enums.MessageResponse.USER_IS_EXIST;
import static com.ggapp.common.utils.Constant.DATE_TIME_FORMAT_PATTERN;
import static com.ggapp.common.utils.Constant.DATE_FORMAT_PATTERN;
import static com.ggapp.common.utils.Constant.USER_FILE_PATH;

@Service
public class EmloyeeServiceImp implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ConfirmKeyRepository confirmKeyRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest, String confirmKey) throws ApplicationException {
        if (!accountService.accountIsExists(employeeRequest.getAccount()) && !emailIsExists(employeeRequest.getEmail()) &&
                checkConfirmKey(employeeRequest.getEmail(), confirmKey)) {
            List<Employee> last = new AutoIncrement(employeeRepository).getLastOfCollection();
            Employee newEmployee = new Employee();
            newEmployee.setFullName(employeeRequest.getFullName());
            newEmployee.setBirthDay(commonUtils.convertDateStringToLocalDateTime(employeeRequest.getBirthDay(),
                    DATE_FORMAT_PATTERN));
            if (last != null)
                newEmployee.setId(last.get(0).getId() + 1);
            else newEmployee.setId(1);
            newEmployee.setAddress(employeeRequest.getAddress());
            newEmployee.setDistrict(employeeRequest.getDistrict());
            newEmployee.setCity(employeeRequest.getCity());
            newEmployee.setEmail(employeeRequest.getEmail());

            if (!employeeRequest.getRole().contains("ROLE_")) {
                newEmployee.setRole("ROLE_" + employeeRequest.getRole());
            } else {
                newEmployee.setRole(employeeRequest.getRole());
            }

            newEmployee.setAuthorities(employeeRequest.getAuthorities());
            newEmployee.setImageFilePath(fileUtils.saveFile(employeeRequest.getAccount() + "_" + newEmployee.getId(),
                    employeeRequest.getImageFileData(), USER_FILE_PATH + employeeRequest.getAccount()));
            newEmployee.setActive(true);
            newEmployee.setDeleted(false);
            newEmployee.setPosition(employeeRequest.getPosition());
            newEmployee.setDepartmentName(employeeRequest.getDepartmentName());
            newEmployee.setLevel(employeeRequest.getLevel());
            newEmployee.setHireDate(commonUtils.convertDateStringToLocalDateTime(employeeRequest.getBirthDay(), DATE_FORMAT_PATTERN));
            newEmployee.setRetiredDate(commonUtils.convertDateStringToLocalDateTime(employeeRequest.getBirthDay(), DATE_FORMAT_PATTERN));
            newEmployee.setCreatedDate(LocalDateTime.now());
            accountService.createEmployeeAccount(newEmployee, employeeRequest);
            Employee result = employeeRepository.save(newEmployee);
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(result.getEmail(), Constant.REGISTER_TYPE);
            EmployeeResponse employeeResponse = employeeMapper.entityToResponse(result);
            employeeResponse.setImageFilePath(newEmployee.getImageFilePath());
            return employeeResponse;
        } else throw new ApplicationException(EMPLOYEE_IS_EXIST);
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
    public EmployeeResponse getProfileEmployee(int id) throws ApplicationException {
        return null;
    }

    @Override
    public EmployeeResponse updateEmployee(int id, EmployeeRequest employeeRequest) throws ApplicationException {
        return null;
    }

    @Override
    public boolean logicDeletedEmployee(int id) throws ApplicationException {
        return false;
    }

    @Override
    public boolean physicDeletedEmployee(int id) throws ApplicationException {
        return false;
    }

    private boolean emailIsExists(String email) throws ApplicationException {
        Optional<Employee> checkEmail = employeeRepository.findByEmailEqualsIgnoreCase(email);
        return checkEmail.isPresent();
    }

    private boolean checkConfirmKey(String email, String key) throws ApplicationException {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(key)) {
            return false;
        }
        Optional<ConfirmKey> isKeyExists = confirmKeyRepository.findByEmailEqualsAndTypeEquals(email, Constant.REGISTER_TYPE);
        isKeyExists.orElseThrow(() -> new ApplicationException(CONFIRM_KEY_INVALID));
        LocalDateTime now = LocalDateTime.now();
        return isKeyExists.get().getKey().equals(key) && now.isBefore(isKeyExists.get().getExpire());
    }
}
