package me.koobin.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.mapper.UserMapper;
import me.koobin.snsserver.model.UserIdAndPassword;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserUpdateInfo;
import me.koobin.snsserver.model.UserUpdateParam;
import me.koobin.snsserver.util.PasswordEncryptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/*
@Service
: "모델에 독립된 인터페이스로 제공되는 작업"으로 정의된 서비스임을 명시
  비즈니스 로직을 처리할 클래스
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;

  @Override
  public void signup(User user) {
    String encodedPassword = PasswordEncryptor.encrypt(user.getPassword());
    User encryptedUser = User.builder()
        .username(user.getUsername())
        .password(encodedPassword)
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .name(user.getName())
        .build();
    userMapper.insertUser(encryptedUser);

  }

  @Override
  public boolean isUsernameDupe(String username) {
    return userMapper.isUsernameDupe(username);
  }

  @Override
  public User getLoginUser(UserIdAndPassword userIdAndPassword) {
    String password = userMapper.getPassword(userIdAndPassword.getUsername());
    if (password == null || !PasswordEncryptor.isMatch(userIdAndPassword.getPassword(), password)) {
      return null;
    }

    return userMapper.getUser(userIdAndPassword);
  }

  @Override
  public void updateUser(String username, UserUpdateParam userUpdateParam) {
    UserUpdateInfo userUpdateInfo = UserUpdateInfo.builder()
        .username(username)
        .name(userUpdateParam.getName())
        .phoneNumber(userUpdateParam.getPhoneNumber())
        .email(userUpdateParam.getEmail())
        .build();
    userMapper.updateUser(userUpdateInfo);
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
        || StringUtils.equals(currentPassword, newPassword)
        || !StringUtils.equals(newPassword, checkNewPassword)) {
      throw new InValidValueException("올바르지 않은 값입니다. 다시 입력해주세요.");
    }

    String encryptedPassword = PasswordEncryptor.encrypt(userPasswordUpdateParam.getNewPassword());
    String currentUsername = currentUser.getUsername();
    UserIdAndPassword userIdAndPassword = new UserIdAndPassword(currentUsername, encryptedPassword);

    userMapper.updateUserPassword(userIdAndPassword);

  }

  @Override
  public void deleteUser(String username) {
    userMapper.deleteUser(username);
  }
}
