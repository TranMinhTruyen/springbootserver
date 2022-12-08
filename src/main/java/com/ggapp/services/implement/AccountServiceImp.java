package com.ggapp.services.implement;

import com.ggapp.common.dto.request.LoginRequest;
import com.ggapp.common.dto.request.UserRequest;
import com.ggapp.common.dto.response.JwtResponse;
import com.ggapp.common.dto.response.UserResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.AccountDetail;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.common.utils.CommonUtils;
import com.ggapp.common.utils.Constant;
import com.ggapp.dao.document.Account;
import com.ggapp.dao.document.AutoIncrement;
import com.ggapp.dao.document.ConfirmKey;
import com.ggapp.dao.document.Employee;
import com.ggapp.dao.document.User;
import com.ggapp.dao.repository.mongo.AccountRepository;
import com.ggapp.dao.repository.mongo.ConfirmKeyRepository;
import com.ggapp.services.AccountService;
import com.ggapp.services.MailService;
import com.ggapp.services.SessionService;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.ggapp.common.enums.MessageResponse.CONFIRM_KEY_EXPIRED;
import static com.ggapp.common.enums.MessageResponse.CONFIRM_KEY_INVALID;
import static com.ggapp.common.enums.MessageResponse.USER_IS_DISABLE;
import static com.ggapp.common.enums.MessageResponse.USER_NOT_FOUND;
import static com.ggapp.common.utils.Constant.ADMIN_TYPE;
import static com.ggapp.common.utils.Constant.EMPLOYEE_TYPE;
import static com.ggapp.common.utils.Constant.USER_TYPE;

/**
 * @author Tran Minh Truyen on 04/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
@Service
public class AccountServiceImp implements AccountService, UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ConfirmKeyRepository confirmKeyRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private MailService mailService;

    @Override
    public void createUserAccount(User user, UserRequest userRequest) {
        Account account = new Account();
        List<User> last = new AutoIncrement(accountRepository).getLastOfCollection();
        if (last != null)
            account.setId(last.get(0).getId() + 1);
        else account.setId(1);
        account.setAccount(userRequest.getAccount());
        account.setPassword(Hashing.sha512().hashString(userRequest.getPassword(), StandardCharsets.UTF_8).toString());
        account.setEmail(user.getEmail());
        account.setOwnerId(user.getId());
        account.setActive(true);
        account.setAccountType(USER_TYPE);
        account.setCreatedBy(user.getCreatedBy());
        account.setCreatedDate(user.getCreatedDate());
        accountRepository.save(account);
    }

    @Override
    public void createEmployeeAccount(Employee employee) {
        Account account = new Account();
        List<User> last = new AutoIncrement(accountRepository).getLastOfCollection();
        if (last != null)
            account.setId(last.get(0).getId() + 1);
        else account.setId(1);
        account.setEmail(employee.getEmail());
        account.setOwnerId(employee.getId());
        account.setActive(employee.isActive());
        account.setAccountType(EMPLOYEE_TYPE);
        account.setCreatedBy(employee.getCreatedBy());
        account.setCreatedDate(employee.getCreatedDate());
        accountRepository.save(account);
    }

    @Override
    public JwtResponse loginAnotherDevice(LoginRequest loginRequest, String confirmKey) throws ApplicationException {
        Optional<Account> account = accountRepository.findByAccountOrEmailEqualsAndPasswordEquals(loginRequest.getAccount(),
                Hashing.sha512().hashString(loginRequest.getPassword(), StandardCharsets.UTF_8).toString());
        account.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        if (!account.get().isActive()) {
            throw new ApplicationException(USER_IS_DISABLE);
        }
        String jwt = null;
        AccountDetail accountDetail = commonUtils.accountToAccountDetail(account.get());

        //  if have account but not found session and have confirm key (case create new session)
        if (checkConfirmKey(account.get().getEmail(), confirmKey)) {
            jwt = sessionService.createJWTAndSession(accountDetail, loginRequest);
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(account.get().getEmail(), Constant.LOGIN_TYPE);
        } else {
            sessionService.checkSessionAndDeviceInfo(accountDetail, loginRequest);
            jwt = sessionService.getJWTFromSession(accountDetail, loginRequest);
        }
        return new JwtResponse(jwt);
    }

    @Override
    public UserResponse resetPassword(String email) throws ApplicationException {
        return null;
    }

    @Override
    public boolean accountIsExists(String account) {
        Optional<Account> checkAccount = accountRepository.findUserByAccount(account);
        return checkAccount.isPresent();
    }

    private boolean checkConfirmKey(String email, String key) throws ApplicationException {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(key)) {
            return false;
        }
        Optional<ConfirmKey> isKeyExists = confirmKeyRepository.findByEmailEqualsAndTypeEquals(email, Constant.LOGIN_TYPE);
        isKeyExists.orElseThrow(() -> new ApplicationException(CONFIRM_KEY_INVALID));
        LocalDateTime now = LocalDateTime.now();
        if (isKeyExists.get().getKey().equals(key) && now.isBefore(isKeyExists.get().getExpire())) {
            return true;
        } else {
            throw new ApplicationException(CONFIRM_KEY_EXPIRED);
        }
    }

    @Override
    public UserDetails loadUserById(int id) throws ApplicationException {
        Optional<Account> result = accountRepository.findById(id);
        if (result.isPresent()) {
            AccountDetail accountDetail = commonUtils.accountToAccountDetail(result.get());
            return new CustomUserDetail(accountDetail);
        } else throw new ApplicationException("Account not found with id: " + id, HttpStatus.NOT_FOUND);
    }

    @Override
    public void sendEmailRegisterConfirmKey(String email) throws ApplicationException {
        try {
            if (emailIsExists(email)) {
                Random random = new Random();
                String confirmKey = String.format("%06d", random.nextInt(999999));
                ConfirmKey newConfirmKey = new ConfirmKey();
                newConfirmKey.setEmail(email);
                newConfirmKey.setKey(confirmKey);
                LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);
                newConfirmKey.setExpire(expireTime);
                newConfirmKey.setType(Constant.REGISTER_TYPE);
                Optional<ConfirmKey> isKeyExists = confirmKeyRepository.findByEmailEqualsAndTypeEquals(email, Constant.REGISTER_TYPE);
                if (isKeyExists.isPresent()) {
                    confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(email, Constant.REGISTER_TYPE);
                }
                confirmKeyRepository.save(newConfirmKey);
                mailService.sendEmailRegisterConfirmKey(email, confirmKey);
            }
        } catch (MailException e) {
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(email, Constant.REGISTER_TYPE);
            throw new ApplicationException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void sendEmailLoginConfirmKey(LoginRequest loginRequest) throws ApplicationException {
        Optional<Account> account = accountRepository.findByAccountOrEmailEqualsAndPasswordEquals(loginRequest.getAccount(),
                Hashing.sha512().hashString(loginRequest.getPassword(), StandardCharsets.UTF_8).toString());
        account.orElseThrow(() -> new ApplicationException("Not found user", HttpStatus.NOT_FOUND));
        if (!account.get().isActive()) {
            throw new ApplicationException("User is disable", HttpStatus.UNAUTHORIZED);
        }
        try {
            String confirmKey;
            Random random = new Random();
            confirmKey = String.format("%06d", random.nextInt(999999));
            ConfirmKey newConfirmKey = new ConfirmKey();
            newConfirmKey.setEmail(account.get().getEmail());
            newConfirmKey.setKey(confirmKey);
            LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);
            newConfirmKey.setExpire(expireTime);
            newConfirmKey.setExpire(expireTime);
            newConfirmKey.setType(Constant.LOGIN_TYPE);
            Optional<ConfirmKey> isKeyExists = confirmKeyRepository.findByEmailEqualsAndTypeEquals(account.get().getEmail(), Constant.LOGIN_TYPE);
            if (isKeyExists.isPresent()) {
                confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(account.get().getEmail(), Constant.LOGIN_TYPE);
            }
            confirmKeyRepository.save(newConfirmKey);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(account.get().getEmail());
            mailMessage.setSubject("GG-App Login Confirm Key");
            mailMessage.setText("This is your login confirm key: " + confirmKey + "\n" + "Key will expired in 5 minutes");
            emailSender.send(mailMessage);
        } catch (MailException e) {
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(account.get().getEmail(), Constant.LOGIN_TYPE);
            throw new ApplicationException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void activateAccount(String email) throws ApplicationException {
        Optional<ConfirmKey> confirmKey = confirmKeyRepository.findByEmailEqualsAndTypeEquals(email, Constant.REGISTER_TYPE);
        confirmKey.orElseThrow(() -> new ApplicationException(CONFIRM_KEY_INVALID));
    }

    @Override
    @Deprecated
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    public boolean emailIsExists(String email) {
        Optional<Account> checkEmail = accountRepository.findByEmailEqualsIgnoreCase(email);
        return checkEmail.isEmpty();
    }
}
