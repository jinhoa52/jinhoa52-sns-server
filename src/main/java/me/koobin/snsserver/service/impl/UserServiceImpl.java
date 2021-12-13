package me.koobin.snsserver.service.impl;

import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.mapper.UserMapper;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserSignUpParam;
import me.koobin.snsserver.model.UserUpdateInfo;
import me.koobin.snsserver.model.UserUpdateParam;
import me.koobin.snsserver.model.UsernameAndPw;
import me.koobin.snsserver.service.FileInfoService;
import me.koobin.snsserver.service.UserService;
import me.koobin.snsserver.util.PwEncryptor;
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

  @Override
  public boolean isUsernameDupe(String username) {
    return userMapper.isUsernameDupe(username);
  }

  @Override
  public User getLoginUser(UsernameAndPw usernameAndPw) {
    String username = usernameAndPw.getUsername();

    String encodedPw = userMapper.getPw(username);
    if (encodedPw == null)return null;

    boolean match = PwEncryptor.isMatch(usernameAndPw.getPw(), encodedPw);
    if (!match) return null;

    UsernameAndPw encodedUsernameAndPw = new UsernameAndPw(
        username,
        encodedPw);

    return userMapper.findByUsernameAndPw(encodedUsernameAndPw);
  }

  private void insertUser(UserSignUpParam userSignUpParam) {
    String encodedPw = PwEncryptor.encrypt(userSignUpParam.getPassword());
    UserSignUpParam encryptedUser = UserSignUpParam.builder()
        .username(userSignUpParam.getUsername())
        .password(encodedPw)
        .email(userSignUpParam.getEmail())
        .phoneNumber(userSignUpParam.getPhoneNumber())
        .name(userSignUpParam.getName())
        .build();
    userMapper.insertUser(encryptedUser);
  }

  @Override
  public void updateUser(User currentUser, UserUpdateParam userUpdateParam, MultipartFile profile) {

    // 기존 프로필 이미지 삭제
    if (currentUser.getProfileId() != null) {
      fileInfoService.deleteFile(currentUser.getProfileId());
    }

    Long fileId = null;
    // 새로운 프로필 저장
    if (!profile.isEmpty()) {
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

    userMapper.updateProfileInfo(userUpdateInfo);
  }

  @Override
  public void updateUserPassword(User currentUser, UserPasswordUpdateParam userPasswordUpdateParam)
      throws InValidValueException {

    String currentUserPassword = currentUser.getPassword();
    String currentPassword = userPasswordUpdateParam.getCurrentPassword();
    boolean isValidPassword = PwEncryptor.isMatch(currentPassword, currentUserPassword);

    String newPassword = userPasswordUpdateParam.getNewPassword();
    String checkNewPassword = userPasswordUpdateParam.getCheckNewPassword();

    if (!isValidPassword
        || !StringUtils.equals(newPassword, checkNewPassword)
        || StringUtils.equals(currentPassword, newPassword)) {
      throw new InValidValueException("올바르지 않은 값입니다. 다시 입력해주세요.");
    }

    String encryptedPassword = PwEncryptor.encrypt(userPasswordUpdateParam.getNewPassword());
    String currentUsername = currentUser.getUsername();
    UsernameAndPw usernameAndPw = new UsernameAndPw(currentUsername,
        encryptedPassword);

    userMapper.updatePassword(usernameAndPw);

  }

  @Override
  public void deleteUser(User currentUser, String currentPassword) throws InValidValueException {
    boolean isMatch = PwEncryptor.isMatch(currentPassword, currentUser.getPassword());

    if (!isMatch) {
      throw new InValidValueException();
    }

    userMapper.deleteByUsername(currentUser.getUsername());

  }
}
