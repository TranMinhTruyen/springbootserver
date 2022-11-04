package com.example.services.implement;

import com.example.common.dto.request.LoginRequest;
import com.example.common.dto.request.UserRequest;
import com.example.common.dto.response.CommonResponse;
import com.example.common.dto.response.JwtResponse;
import com.example.common.dto.response.UserResponse;
import com.example.common.exception.ApplicationException;
import com.example.common.jwt.CustomUserDetail;
import com.example.common.utils.Constant;
import com.example.common.utils.mapper.UserMapper;
import com.example.dao.document.AutoIncrement;
import com.example.dao.document.ConfirmKey;
import com.example.dao.document.User;
import com.example.dao.repository.mongo.ConfirmKeyRepository;
import com.example.dao.repository.mongo.UserRepository;
import com.example.services.SessionServices;
import com.example.services.UserServices;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.example.common.commonenum.MessageError.CONFIRM_KEY_INVALID;
import static com.example.common.commonenum.MessageError.USER_IS_DISABLE;
import static com.example.common.commonenum.MessageError.USER_IS_EXIST;
import static com.example.common.commonenum.MessageError.USER_NOT_FOUND;
import static com.example.common.commonenum.MessageError.USER_NOT_FOUND_GET_ALL;
import static com.example.common.commonenum.MessageError.USER_NOT_MATCH;


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

    @Override
    public UserResponse createUser(UserRequest userRequest, String confirmKey) throws ApplicationException {
        if (!accountIsExists(userRequest.getAccount()) && !emailIsExists(userRequest.getEmail()) &&
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
            String fileName = Constant.IMAGE_FILE_PATH + userRequest.getAccount() + ".png";
            try {
                byte[] data = Base64.getDecoder().decode(userRequest.getImage());
                FileOutputStream fos = new FileOutputStream(new File(fileName));
                fos.write(data);
                fos.close();
            } catch (IOException exception) {
                throw new ApplicationException(exception.getMessage(), exception.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            newUser.setImage(fileName);
            newUser.setActive(true);
            newUser.setCreatedBy(userRequest.getFirstName() + userRequest.getLastName());
            newUser.setCreatedDate(LocalDateTime.now());
            User result = userRepository.save(newUser);
            return getUserAfterUpdateOrCreate(result);
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
    public JwtResponse login(LoginRequest loginRequest) throws ApplicationException {
        Optional<User> result = userRepository.findUsersByAccountEqualsAndPasswordEquals(loginRequest.getAccount(),
                Hashing.sha512().hashString(loginRequest.getPassword(), StandardCharsets.UTF_8).toString());
        result.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        if (!result.get().isActive()) {
            throw new ApplicationException(USER_IS_DISABLE);
        }
        String jwt = sessionServices.checkExists(result.get(), loginRequest);
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
        if (checkConfirmKey(loginRequest, confirmKey, Constant.LOGIN_TYPE)) {
            String jwt = sessionServices.checkSessionAndLoginAnotherDevice(result.get(), loginRequest);
            return new JwtResponse(jwt);
        }
        return null;
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
        if (request.getImage() != null) {
            String fileName = Constant.IMAGE_FILE_PATH + update.getAccount() + ".png";
            try {
                Path path = Paths.get(update.getImage());
                Files.deleteIfExists(path);
                byte[] data = Base64.getDecoder().decode(request.getImage());
                FileOutputStream fos = new FileOutputStream(new File(fileName));
                fos.write(data);
                fos.close();
            } catch (IOException exception) {
                throw new ApplicationException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            update.setImage(fileName);
        }
        update.setActive(request.isActive());
        User result = userRepository.save(update);
        return getUserAfterUpdateOrCreate(result);
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
        return checkEmail.isPresent();
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

    public UserResponse getUserAfterUpdateOrCreate(User user) {
        UserResponse response = new UserResponse();
        response.setAddress(user.getAddress());
        response.setBirthDay(user.getBirthDay());
        response.setLastName(user.getLastName());
        response.setFirstName(user.getFirstName());
        response.setCitizenID(user.getCitizenId());
        response.setActive(user.isActive());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setImage(user.getImage());
        return response;
    }

    @Override
    public UserResponse getProfileUser(int id) throws ApplicationException {
        UserResponse userResponse = new UserResponse();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userResponse.setFirstName(user.get().getFirstName());
            userResponse.setLastName(user.get().getLastName());
            userResponse.setBirthDay(user.get().getBirthDay());
            userResponse.setAddress(user.get().getAddress());
            userResponse.setCitizenID(user.get().getCitizenId());
            userResponse.setEmail(user.get().getEmail());
            try {
                File file = ResourceUtils.getFile(user.get().getImage());
                InputStream in = new FileInputStream(file);
                userResponse.setImage(Base64.getEncoder().encodeToString(in.readAllBytes()));
            } catch (IOException exception) {
                userResponse.setImage(null);
            }
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
            return getUserAfterUpdateOrCreate(after);
        } else throw new ApplicationException(USER_NOT_FOUND);
    }

    @Override
    public void sendEmailConfirmKey(String email) throws ApplicationException {
        try {
            if (!emailIsExists(email)) {
                String confirmKey;
                Random random = new Random();
                confirmKey = String.format("%06d", random.nextInt(999999));
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
                mailMessage.setSubject("GG-App Confirm Key");
                mailMessage.setText("This is your confirm key: " + confirmKey + "\n" + "Key will expired in 5 minutes");
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
            mailMessage.setSubject("GG-App Confirm Key");
            mailMessage.setText("This is your confirm key: " + confirmKey + "\n" + "Key will expired in 5 minutes");
            emailSender.send(mailMessage);
        } catch (MailException e) {
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(result.get().getEmail(), Constant.LOGIN_TYPE);
            throw new ApplicationException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    public boolean checkConfirmKey(String email, String key, String type) throws ApplicationException {
        Optional<ConfirmKey> isKeyExists = confirmKeyRepository.findByEmailEqualsAndTypeEquals(email, type);
        isKeyExists.orElseThrow(() -> new ApplicationException(CONFIRM_KEY_INVALID));
        LocalDateTime now = LocalDateTime.now();
        if (isKeyExists.get().getKey().equals(key) && now.isBefore(isKeyExists.get().getExpire())) {
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(email, type);
            return true;
        } else return false;
    }

    public boolean checkConfirmKey(LoginRequest loginRequest, String key, String type) throws ApplicationException {
        Optional<User> result = userRepository.findUsersByAccountEqualsAndPasswordEquals(loginRequest.getAccount(),
                Hashing.sha512().hashString(loginRequest.getPassword(), StandardCharsets.UTF_8).toString());
        result.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        if (!result.get().isActive()) {
            throw new ApplicationException(USER_NOT_FOUND);
        }
        Optional<ConfirmKey> isKeyExists = confirmKeyRepository.findByEmailEqualsAndTypeEquals(result.get().getEmail(), type);
        isKeyExists.orElseThrow(() -> new ApplicationException(CONFIRM_KEY_INVALID));
        LocalDateTime now = LocalDateTime.now();
        if (isKeyExists.get().getKey().equals(key) && now.isBefore(isKeyExists.get().getExpire())) {
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(result.get().getEmail(), type);
            return true;
        } else return false;
    }
}
