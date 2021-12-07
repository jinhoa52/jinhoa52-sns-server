package me.koobin.snsserver.service.impl;

import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.mapper.UserMapper;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserIdAndPassword;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserSignUpParam;
import me.koobin.snsserver.model.UserUpdateInfo;
import me.koobin.snsserver.model.UserUpdateParam;
import me.koobin.snsserver.service.FileInfoService;
import me.koobin.snsserver.service.UserService;
import me.koobin.snsserver.util.PasswordEncryptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/*
@Service
: "모델에 독립된 인터페이스로 제공되는 작업"으로 정의된 서비스임을 명시
  비즈니스 로직을 처리할 클래스
*/
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;

  private final FileInfoService fileInfoService;

  @Override
  public boolean signUp(UserSignUpParam userSignUpParam) {
    boolean dupe = isUsernameDupe(userSignUpParam.getUsername());
    if (dupe) {
      return false;
    }
    insertUser(userSignUpParam);
    return true;

  }

  private void insertUser(UserSignUpParam userSignUpParam) {
    String encodedPassword = PasswordEncryptor.encrypt(userSignUpParam.getPassword());
    UserSignUpParam encryptedUser = UserSignUpParam.builder()
        .username(userSignUpParam.getUsername())
        .password(encodedPassword)
        .email(userSignUpParam.getEmail())
        .phoneNumber(userSignUpParam.getPhoneNumber())
        .name(userSignUpParam.getName())
        .build();
    userMapper.insertUser(encryptedUser);
  }

  @Override
  public boolean isUsernameDupe(String username) {
    return userMapper.isUsernameDupe(username);
  }

  @Override
  public User getLoginUser(UserIdAndPassword userIdAndPassword) {
    String encodedPassword = userMapper.getPassword(userIdAndPassword.getUsername());
    boolean match = PasswordEncryptor.isMatch(userIdAndPassword.getPassword(), encodedPassword);
    if (encodedPassword == null || !match) {
      return null;
    }

    UserIdAndPassword encodedUserIdAndPassword = new UserIdAndPassword(
        userIdAndPassword.getUsername(),
        encodedPassword);

    return userMapper.getUser(encodedUserIdAndPassword);
  }

  @Override
  public UserUpdateInfo updateUser(User currentUser, UserUpdateParam userUpdateParam, MultipartFile profile){

    // 기존 프로필 삭제 작업
    if (currentUser.getProfileId() != null) {
      fileInfoService.deleteFile(currentUser.getProfileId());
    }

    Long fileId = null;
    if (!profile.isEmpty()) {
      // 새로운 프로필 저장
      fileId = fileInfoService.saveFile(profile);
    }

    // 회원 정보 수정
    UserUpdateInfo userUpdateInfo = UserUpdateInfo.builder()
        .username(currentUser.getUsername())
        .name(userUpdateParam.getName())
        .phoneNumber(userUpdateParam.getPhoneNumber())
        .email(userUpdateParam.getEmail())
        .profileMessage(userUpdateParam.getProfileMessage())
        .profileId(fileId)
        .build();

    userMapper.updateUser(userUpdateInfo);
    return userUpdateInfo;

  }

  @Override
  public void updateUserPassword(User currentUser, UserPasswordUpdateParam userPasswordUpdateParam)
      throws InValidValueException {

    String currentUserPassword = currentUser.getPassword();
    String currentPassword = userPasswordUpdateParam.getCurrentPassword();
    boolean isValidPassword = PasswordEncryptor.isMatch(currentPassword, currentUserPassword);

    String newPassword = userPasswordUpdateParam.getNewPassword();
    String checkNewPassword = userPasswordUpdateParam.getCheckNewPassword();

    if (!isValidPassword
        || !StringUtils.equals(newPassword, checkNewPassword)
        || StringUtils.equals(currentPassword, newPassword)) {
      throw new InValidValueException("올바르지 않은 값입니다. 다시 입력해주세요.");
    }

    String encryptedPassword = PasswordEncryptor.encrypt(userPasswordUpdateParam.getNewPassword());
    String currentUsername = currentUser.getUsername();
    UserIdAndPassword userIdAndPassword = new UserIdAndPassword(currentUsername, encryptedPassword);

    userMapper.updateUserPassword(userIdAndPassword);

  }

  @Override
  public void deleteUser(User currentUser, String currentPassword) throws InValidValueException {
    boolean isMatch = PasswordEncryptor.isMatch(currentPassword, currentUser.getPassword());

    if (!isMatch) {
      throw new InValidValueException();
    }

    userMapper.deleteUser(currentUser.getUsername());

  }
}
