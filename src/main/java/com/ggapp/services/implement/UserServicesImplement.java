package com.ggapp.services.implement;

import com.ggapp.common.dto.request.LoginRequest;
import com.ggapp.common.dto.request.UserRequest;
import com.ggapp.common.dto.response.CommonResponse;
import com.ggapp.common.dto.response.JwtResponse;
import com.ggapp.common.dto.response.UserResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.jwt.CustomUserDetail;
import com.ggapp.common.utils.Constant;
import com.ggapp.common.utils.FileUtils;
import com.ggapp.common.utils.mapper.UserMapper;
import com.ggapp.dao.document.AutoIncrement;
import com.ggapp.dao.document.ConfirmKey;
import com.ggapp.dao.document.User;
import com.ggapp.dao.repository.mongo.ConfirmKeyRepository;
import com.ggapp.dao.repository.mongo.UserRepository;
import com.ggapp.services.SessionServices;
import com.ggapp.services.UserServices;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.ggapp.common.commonenum.MessageResponse.CONFIRM_KEY_INVALID;
import static com.ggapp.common.commonenum.MessageResponse.USER_IS_DISABLE;
import static com.ggapp.common.commonenum.MessageResponse.USER_IS_EXIST;
import static com.ggapp.common.commonenum.MessageResponse.USER_NOT_FOUND;
import static com.ggapp.common.commonenum.MessageResponse.USER_NOT_FOUND_GET_ALL;
import static com.ggapp.common.commonenum.MessageResponse.USER_NOT_MATCH;
import static com.ggapp.common.utils.Constant.USER_FILE_PATH;


/**
 * @author Tran Minh Truyen
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE = 205
 */

@Service
public class UserServicesImplement implements UserDetailsService, UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmKeyRepository confirmKeyRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SessionServices sessionServices;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileUtils fileUtils;

    @Override
    public UserResponse createUser(UserRequest userRequest, String confirmKey) throws ApplicationException {
        if (!accountIsExists(userRequest.getAccount()) && emailIsExists(userRequest.getEmail()) &&
                checkConfirmKey(userRequest.getEmail(), confirmKey, Constant.REGISTER_TYPE)) {
            List<User> last = new AutoIncrement(userRepository).getLastOfCollection();
            User newUser = new User();
            newUser.setAccount(userRequest.getAccount());
            newUser.setPassword(Hashing.sha512().hashString(userRequest.getPassword(), StandardCharsets.UTF_8).toString());
            newUser.setFirstName(userRequest.getFirstName());
            newUser.setLastName(userRequest.getLastName());
            newUser.setBirthDay(userRequest.getBirthDay());
            if (last != null)
                newUser.setId(last.get(0).getId() + 1);
            else newUser.setId(1);
            newUser.setAddress(userRequest.getAddress());
            newUser.setDistrict(userRequest.getDistrict());
            newUser.setCity(userRequest.getCity());
            newUser.setPostCode(userRequest.getPostCode());
            newUser.setEmail(userRequest.getEmail());
            newUser.setRole(userRequest.getRole());
            newUser.setImageFilePath(fileUtils.saveFile(userRequest.getAccount() + "_" + newUser.getId(),
                    userRequest.getImageFileData(), USER_FILE_PATH + userRequest.getAccount()));
            newUser.setActive(true);
            newUser.setDeleted(false);
            newUser.setCreatedBy(userRequest.getFirstName() + userRequest.getLastName());
            newUser.setCreatedDate(LocalDateTime.now());
            User result = userRepository.save(newUser);
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(result.getEmail(), Constant.REGISTER_TYPE);
            UserResponse userResponse = userMapper.entityToResponse(result);
            userResponse.setImageFilePath(newUser.getImageFilePath());
            return userResponse;
        } else throw new ApplicationException(USER_IS_EXIST);
    }

    @Override
    public CommonResponse getAllUser(int page, int size) throws ApplicationException {
        List<User> result = userRepository.findAll();
        List<UserResponse> userResponseList = userMapper.entityToResponse(result);
        if (result.isEmpty()) {
            throw new ApplicationException(USER_NOT_FOUND_GET_ALL);
        }
        return new CommonResponse().getCommonResponse(page, size, userResponseList);
    }

    @Override
    public CommonResponse getUserByKeyWord(int page, int size, String keyword) throws ApplicationException {
        Optional<List<User>> result = userRepository.findUserByFirstNameContainingOrLastNameContaining(keyword, keyword);
        result.orElseThrow(() -> new ApplicationException(USER_NOT_MATCH));
        List<UserResponse> userResponseList = userMapper.entityToResponse(result.get());
        return new CommonResponse().getCommonResponse(page, size, userResponseList);
    }

    @Override
    @Deprecated
    public JwtResponse login(LoginRequest loginRequest) throws ApplicationException {
        Optional<User> result = userRepository.findUsersByAccountEqualsAndPasswordEquals(loginRequest.getAccount(),
                Hashing.sha512().hashString(loginRequest.getPassword(), StandardCharsets.UTF_8).toString());
        result.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        if (!result.get().isActive()) {
            throw new ApplicationException(USER_NOT_FOUND);
        }
        String jwt = sessionServices.getJWTFromSession(result.get(), loginRequest);
        return new JwtResponse(jwt);
    }

    @Override
    public JwtResponse loginAnotherDevice(LoginRequest loginRequest, String confirmKey) throws ApplicationException {
        Optional<User> result = userRepository.findUsersByAccountEqualsAndPasswordEquals(loginRequest.getAccount(),
                Hashing.sha512().hashString(loginRequest.getPassword(), StandardCharsets.UTF_8).toString());
        result.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        if (!result.get().isActive()) {
            throw new ApplicationException(USER_IS_DISABLE);
        }
        String jwt = null;

        //  if have account but not found session and have confirm key (case create new session)
        if (checkConfirmKey(result.get().getEmail(), confirmKey, Constant.LOGIN_TYPE)) {
            jwt = sessionServices.createJWTAndSession(result.get(), loginRequest);
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(result.get().getEmail(), Constant.LOGIN_TYPE);
        } else {
            sessionServices.checkSessionAndDeviceInfo(result.get(), loginRequest);
            jwt = sessionServices.getJWTFromSession(result.get(), loginRequest);
        }
        return new JwtResponse(jwt);
    }

    @Override
    public UserResponse updateUser(int id, UserRequest request) throws ApplicationException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        User update = user.get();
        if (request.getFirstName() != null)
            update.setFirstName(request.getFirstName());
        if (request.getPassword() != null)
            update.setPassword(Hashing.sha512().hashString(request.getPassword(), StandardCharsets.UTF_8).toString());
        if (request.getLastName() != null)
            update.setLastName(request.getLastName());
        if (request.getAddress() != null)
            update.setAddress(request.getAddress());
        if (request.getDistrict() != null)
            update.setDistrict(request.getDistrict());
        if (request.getCity() != null)
            update.setCity(request.getCity());
        if (request.getPostCode() != null)
            update.setPostCode(request.getPostCode());
        if (request.getBirthDay() != null)
            update.setBirthDay(request.getBirthDay());
        if (request.getCitizenID() != null)
            update.setCitizenId(request.getCitizenID());
        if (request.getEmail() != null)
            update.setEmail(request.getEmail());
        if (request.getImageFileData() != null) {
            String filePath = fileUtils.updateFile(update.getImageFilePath(), request.getImageFileData());
            update.setImageFilePath(filePath);
        }
        update.setActive(request.isActive());
        User result = userRepository.save(update);
        return userMapper.entityToResponse(result);
    }

    @Override
    public boolean deleteUser(int id) throws ApplicationException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else throw new ApplicationException(USER_NOT_FOUND);
    }


    @Override
    public boolean accountIsExists(String account) {
        Optional<User> checkAccount = userRepository.findUserByAccount(account);
        return checkAccount.isPresent();
    }

    public boolean emailIsExists(String email) throws ApplicationException {
        Optional<User> checkEmail = userRepository.findByEmailEqualsIgnoreCase(email);
        return checkEmail.isEmpty();
    }

    @Override
    @Deprecated
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails loadUserById(int id) throws ApplicationException {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()) {
            User user = result.get();
            return new CustomUserDetail(user);
        } else throw new ApplicationException("User not found with id: " + id, HttpStatus.NOT_FOUND);
    }

    @Override
    public UserResponse getProfileUser(int id) throws ApplicationException {
        UserResponse userResponse = new UserResponse();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userResponse.setFirstName(user.get().getFirstName());
            userResponse.setLastName(user.get().getLastName());
            userResponse.setBirthDay(user.get().getBirthDay());
            userResponse.setEmail(user.get().getEmail());
            userResponse.setPhoneNumber(user.get().getPhoneNumber());
            userResponse.setCitizenID(user.get().getCitizenId());
            userResponse.setAddress(user.get().getAddress());
            userResponse.setDistrict(user.get().getDistrict());
            userResponse.setCity(user.get().getCity());
            userResponse.setPostCode(user.get().getPostCode());
            userResponse.setImageFilePath(fileUtils.getFile(user.get().getImageFilePath()));
            userResponse.setRole(user.get().getRole());
            userResponse.setActive(user.get().isActive());
            return userResponse;
        } else
            throw new ApplicationException(USER_NOT_FOUND);
    }

    @Override
    public UserResponse resetPassword(String email) throws ApplicationException {
        Optional<User> user = userRepository.findByEmailEqualsIgnoreCase(email);
        if (user.isPresent()) {
            String newPassword = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
            user.get().setPassword(Hashing.sha512().hashString(newPassword, StandardCharsets.UTF_8).toString());
            User after = userRepository.save(user.get());
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("GG-App Reset Password");
            mailMessage.setText("This is your new password: " + newPassword + "\n" + "Please change your password");
            emailSender.send(mailMessage);
            return userMapper.entityToResponse(after);
        } else throw new ApplicationException(USER_NOT_FOUND);
    }

    @Override
    public void sendEmailConfirmKey(String email) throws ApplicationException {
        try {
            if (emailIsExists(email)) {
                Random random = new Random();
                String confirmKey = String.format("%06d", random.nextInt(999999));
                ConfirmKey newConfirmKey = new ConfirmKey();
                newConfirmKey.setEmail(email);
                newConfirmKey.setKey(confirmKey);
                LocalDateTime now = LocalDateTime.now();
                LocalDate expireDate = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
                LocalTime expireMinute = LocalTime.of(now.getHour(), now.getMinute() + 5, now.getSecond());
                LocalDateTime expireTime = LocalDateTime.of(expireDate, expireMinute);
                newConfirmKey.setExpire(expireTime);
                newConfirmKey.setType(Constant.REGISTER_TYPE);
                Optional<ConfirmKey> isKeyExists = confirmKeyRepository.findByEmailEqualsAndTypeEquals(email, Constant.REGISTER_TYPE);
                if (isKeyExists.isPresent()) {
                    confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(email, Constant.REGISTER_TYPE);
                }
                confirmKeyRepository.save(newConfirmKey);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(email);
                mailMessage.setSubject("GG-App Register Confirm Key");
                mailMessage.setText("This is your register confirm key: " + confirmKey + "\n" + "Key will expired in 5 minutes");
                emailSender.send(mailMessage);
            }
        } catch (MailException e) {
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(email, Constant.REGISTER_TYPE);
            throw new ApplicationException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void sendEmailConfirmKey(LoginRequest loginRequest) throws ApplicationException {
        Optional<User> result = userRepository.findUsersByAccountEqualsAndPasswordEquals(loginRequest.getAccount(),
                Hashing.sha512().hashString(loginRequest.getPassword(), StandardCharsets.UTF_8).toString());
        result.orElseThrow(() -> new ApplicationException("Not found user", HttpStatus.NOT_FOUND));
        if (!result.get().isActive()) {
            throw new ApplicationException("User is disable", HttpStatus.UNAUTHORIZED);
        }
        try {
            String confirmKey;
            Random random = new Random();
            confirmKey = String.format("%06d", random.nextInt(999999));
            ConfirmKey newConfirmKey = new ConfirmKey();
            newConfirmKey.setEmail(result.get().getEmail());
            newConfirmKey.setKey(confirmKey);
            LocalDateTime now = LocalDateTime.now();
            LocalDate expireDate = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
            LocalTime expireMinute = LocalTime.of(now.getHour(), now.getMinute() + 5, now.getSecond());
            LocalDateTime expireTime = LocalDateTime.of(expireDate, expireMinute);
            newConfirmKey.setExpire(expireTime);
            newConfirmKey.setType(Constant.LOGIN_TYPE);
            Optional<ConfirmKey> isKeyExists = confirmKeyRepository.findByEmailEqualsAndTypeEquals(result.get().getEmail(), Constant.LOGIN_TYPE);
            if (isKeyExists.isPresent()) {
                confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(result.get().getEmail(), Constant.LOGIN_TYPE);
            }
            confirmKeyRepository.save(newConfirmKey);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(result.get().getEmail());
            mailMessage.setSubject("GG-App Login Confirm Key");
            mailMessage.setText("This is your login confirm key: " + confirmKey + "\n" + "Key will expired in 5 minutes");
            emailSender.send(mailMessage);
        } catch (MailException e) {
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(result.get().getEmail(), Constant.LOGIN_TYPE);
            throw new ApplicationException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    private boolean checkConfirmKey(String email, String key, String type) throws ApplicationException {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(key)) {
            return false;
        }
        Optional<ConfirmKey> isKeyExists = confirmKeyRepository.findByEmailEqualsAndTypeEquals(email, type);
        isKeyExists.orElseThrow(() -> new ApplicationException(CONFIRM_KEY_INVALID));
        LocalDateTime now = LocalDateTime.now();
        return isKeyExists.get().getKey().equals(key) && now.isBefore(isKeyExists.get().getExpire());
    }
}
