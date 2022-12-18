package com.ggapp.services.implement;

import com.ggapp.common.dto.request.UserRequest;
import com.ggapp.common.dto.response.CommonResponsePayload;
import com.ggapp.common.dto.response.UserResponse;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.common.utils.Constant;
import com.ggapp.common.utils.FileUtils;
import com.ggapp.common.utils.mapper.UserMapper;
import com.ggapp.dao.document.AutoIncrement;
import com.ggapp.dao.document.ConfirmKey;
import com.ggapp.dao.document.User;
import com.ggapp.dao.repository.mongo.ConfirmKeyRepository;
import com.ggapp.dao.repository.mongo.UserRepository;
import com.ggapp.services.AccountService;
import com.ggapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.ggapp.common.enums.MessageResponse.CONFIRM_KEY_INVALID;
import static com.ggapp.common.enums.MessageResponse.USER_IS_EXIST;
import static com.ggapp.common.enums.MessageResponse.USER_NOT_FOUND;
import static com.ggapp.common.enums.MessageResponse.USER_NOT_FOUND_GET_ALL;
import static com.ggapp.common.enums.MessageResponse.USER_NOT_MATCH;
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
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmKeyRepository confirmKeyRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileUtils fileUtils;

    @Override
    public UserResponse createUser(UserRequest userRequest, String confirmKey) throws ApplicationException {
        if (!accountService.accountIsExists(userRequest.getAccount()) && emailIsExists(userRequest.getEmail()) &&
                checkConfirmKey(userRequest.getEmail(), confirmKey)) {
            List<User> last = new AutoIncrement(userRepository).getLastOfCollection();
            User newUser = new User();
            newUser.setFullName(userRequest.getFullName());
            newUser.setBirthDay(LocalDateTime.parse(userRequest.getBirthDay(), DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT_PATTERN_BIRTHDAY)));
            if (last != null)
                newUser.setId(last.get(0).getId() + 1);
            else newUser.setId(1L);
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
            newUser.setCreatedBy(userRequest.getFullName());
            newUser.setCreatedDate(LocalDateTime.now());
            User result = userRepository.save(newUser);
            confirmKeyRepository.deleteByEmailEqualsAndTypeEquals(result.getEmail(), Constant.REGISTER_TYPE);
            accountService.createUserAccount(newUser, userRequest);
            UserResponse userResponse = userMapper.entityToResponse(result);
            userResponse.setImageFilePath(newUser.getImageFilePath());
            return userResponse;
        } else throw new ApplicationException(USER_IS_EXIST);
    }

    @Override
    public CommonResponsePayload getAllUser(int page, int size) throws ApplicationException {
        List<User> result = userRepository.findAll();
        List<UserResponse> userResponseList = userMapper.entityToResponse(result);
        if (result.isEmpty()) {
            throw new ApplicationException(USER_NOT_FOUND_GET_ALL);
        }
        return new CommonResponsePayload().getCommonResponse(page, size, userResponseList);
    }

    @Override
    public CommonResponsePayload getUserByKeyWord(int page, int size, String keyword) throws ApplicationException {
        Optional<List<User>> result = userRepository.findUserByFirstNameContainingOrLastNameContaining(keyword, keyword);
        result.orElseThrow(() -> new ApplicationException(USER_NOT_MATCH));
        List<UserResponse> userResponseList = userMapper.entityToResponse(result.get());
        return new CommonResponsePayload().getCommonResponse(page, size, userResponseList);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) throws ApplicationException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        User update = user.get();
        if (request.getFullName() != null)
            update.setFullName(request.getFullName());
        if (request.getAddress() != null)
            update.setAddress(request.getAddress());
        if (request.getDistrict() != null)
            update.setDistrict(request.getDistrict());
        if (request.getCity() != null)
            update.setCity(request.getCity());
        if (request.getPostCode() != null)
            update.setPostCode(request.getPostCode());
        if (request.getBirthDay() != null)
            update.setBirthDay(LocalDateTime.parse(request.getBirthDay(), DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT_PATTERN_BIRTHDAY)));
        if (request.getCitizenID() != null)
            update.setCitizenId(request.getCitizenID());
        if (request.getEmail() != null)
            update.setEmail(request.getEmail());
        if (request.getImageFileData() != null) {
            String filePath = fileUtils.updateFile(update.getImageFilePath(), request.getImageFileData());
            update.setImageFilePath(filePath);
        }
        User result = userRepository.save(update);
        return userMapper.entityToResponse(result);
    }

    @Override
    public boolean deleteUser(Long id) throws ApplicationException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else throw new ApplicationException(USER_NOT_FOUND);
    }

    public boolean emailIsExists(String email) throws ApplicationException {
        Optional<User> checkEmail = userRepository.findByEmailEqualsIgnoreCase(email);
        return checkEmail.isEmpty();
    }

    @Override
    public UserResponse getProfileUser(Long id) throws ApplicationException {
        UserResponse userResponse = new UserResponse();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userResponse.setFullName(user.get().getFullName());
            userResponse.setBirthDay(user.get().getBirthDay().format(DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT_PATTERN_BIRTHDAY)));
            userResponse.setEmail(user.get().getEmail());
            userResponse.setPhoneNumber(user.get().getPhoneNumber());
            userResponse.setCitizenID(user.get().getCitizenId());
            userResponse.setAddress(user.get().getAddress());
            userResponse.setDistrict(user.get().getDistrict());
            userResponse.setCity(user.get().getCity());
            userResponse.setPostCode(user.get().getPostCode());
            userResponse.setImageFilePath(fileUtils.getFile(user.get().getImageFilePath()));
            userResponse.setRole(user.get().getRole());
            return userResponse;
        } else
            throw new ApplicationException(USER_NOT_FOUND);
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
