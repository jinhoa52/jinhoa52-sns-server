package me.koobin.snsserver.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import me.koobin.snsserver.exception.FileException;
import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.mapper.UserMapper;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserSignUpParam;
import me.koobin.snsserver.model.UserUpdateInfo;
import me.koobin.snsserver.model.UserUpdateParam;
import me.koobin.snsserver.model.UsernameAndPw;
import me.koobin.snsserver.service.impl.UserServiceImpl;
import me.koobin.snsserver.util.PwEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith({MockitoExtension.class})
class UserServiceImplTest {

  @Mock
  UserMapper userMapper;

  @InjectMocks
  UserServiceImpl userService;

  @Mock
  FileInfoService fileInfoService;

  User testUser;
  User isNullProfileIdEncryptedTestUser;

  User encryptedTestUser;

  @BeforeEach
  void init() {
    testUser = User.builder()
        .username("id")
        .password("password")
        .email("email@email.com")
        .phoneNumber("010-0000-0000")
        .name("test1")
        .id(1L)
        .profileId(1L)
        .build();

    encryptedTestUser = User.builder()
        .username("id")
        .password(PwEncryptor.encrypt("password"))
        .email("email@email.com")
        .phoneNumber("010-0000-0000")
        .name("test1")
        .id(1L)
        .profileId(1L)
        .build();

    isNullProfileIdEncryptedTestUser = User.builder()
        .username("id")
        .password("password")
        .email("email@email.com")
        .phoneNumber("010-0000-0000")
        .name("test1")
        .id(1L)
        .build();
  }

  @Test
  void signUp_success() {
    UserSignUpParam userSignUpParam = UserSignUpParam
        .builder()
        .email(testUser.getEmail())
        .name(testUser.getName())
        .username(testUser.getUsername())
        .password(testUser.getPassword())
        .phoneNumber(testUser.getPhoneNumber())
        .build();

    when(userMapper.isUsernameDupe(userSignUpParam.getUsername())).thenReturn(false);
    userService.signUp(userSignUpParam);

    verify(userMapper).isUsernameDupe(any(String.class));
    verify(userMapper).insertUser(any(UserSignUpParam.class));
  }

  @Test
  void signUp_fail() {
    UserSignUpParam userSignUpParam = UserSignUpParam
        .builder()
        .email(testUser.getEmail())
        .name(testUser.getName())
        .username(testUser.getUsername())
        .password(testUser.getPassword())
        .phoneNumber(testUser.getPhoneNumber())
        .build();
    when(userMapper.isUsernameDupe(userSignUpParam.getUsername())).thenReturn(true);
    userService.signUp(userSignUpParam);

    verify(userMapper).isUsernameDupe(any(String.class));
    verify(userMapper, never()).insertUser(any(UserSignUpParam.class));
  }

  @Test
  void isUsernameDupe_True_Overlap() {
    when(userMapper.isUsernameDupe(testUser.getUsername())).thenReturn(true);

    boolean result = userService.isUsernameDupe(testUser.getUsername());

    Assertions.assertTrue(result);
    verify(userMapper).isUsernameDupe(testUser.getUsername());
  }

  @Test
  void isUsernameDupe_False_NotOverlap() {
    when(userMapper.isUsernameDupe(testUser.getUsername())).thenReturn(false);

    boolean result = userService.isUsernameDupe(testUser.getUsername());

    Assertions.assertFalse(result);
    verify(userMapper).isUsernameDupe(testUser.getUsername());
  }

  @Test
  void getLoginUser_success() {

    UsernameAndPw usernameAndPw = new UsernameAndPw(testUser.getUsername(), testUser.getPassword());
    when(userMapper.getPw(usernameAndPw.getUsername())).thenReturn(encryptedTestUser.getPassword());
    when(userMapper.findByUsernameAndPw(any(UsernameAndPw.class))).thenReturn(encryptedTestUser);

    User loginUser = userService.getLoginUser(usernameAndPw);

    Assertions.assertNotNull(loginUser);
    Assertions.assertEquals(testUser.getUsername(), loginUser.getUsername());
    verify(userMapper).getPw(usernameAndPw.getUsername());
    verify(userMapper).findByUsernameAndPw(any(UsernameAndPw.class));
  }

  @Test
  void getLoginUser_fail() {
    UsernameAndPw usernameAndPw = new UsernameAndPw(testUser.getUsername(), "wrong");
    when(userMapper.getPw(usernameAndPw.getUsername())).thenReturn(
        encryptedTestUser.getPassword());

    User loginUser = userService.getLoginUser(usernameAndPw);

    Assertions.assertNull(loginUser);
    verify(userMapper, never()).findByUsernameAndPw(usernameAndPw);
  }

  @Test
  void updateUser_isNullProfileId() {
    UserUpdateParam userUpdateParam =
        new UserUpdateParam("update_name", "010-1111-2222", "update@email.com", "message");
    MockMultipartFile mockFile = new MockMultipartFile("profileImage", "orig", null, "bar".getBytes());

    when(fileInfoService.saveFile(mockFile)).thenReturn(1L);


    Assertions.assertDoesNotThrow(() -> userService.updateUser(isNullProfileIdEncryptedTestUser, userUpdateParam, mockFile));

    verify(userMapper).updateProfileInfo(any(UserUpdateInfo.class));
    verify(fileInfoService).saveFile(mockFile);
    verify(fileInfoService, never()).deleteFile(any(Long.class));
  }

  @Test
  void updateUser_notNullProfileId() {
    UserUpdateParam userUpdateParam =
        new UserUpdateParam("update_name", "010-1111-2222", "update@email.com", "message");
    MockMultipartFile mockFile = new MockMultipartFile("profileImage", "orig", "image/png", "bar".getBytes());

    when(fileInfoService.saveFile(mockFile)).thenReturn(1L);


    Assertions.assertDoesNotThrow(() -> userService.updateUser(encryptedTestUser, userUpdateParam, mockFile));

    verify(userMapper).updateProfileInfo(any(UserUpdateInfo.class));
    verify(fileInfoService).saveFile(mockFile);
    verify(fileInfoService).deleteFile(any(Long.class));
  }

  @Test
  void updateUser_errorFileDelete() {
    UserUpdateParam userUpdateParam =
        new UserUpdateParam("update_name", "010-1111-2222", "update@email.com", "message");
    MockMultipartFile mockFile = new MockMultipartFile("profileImage", "orig", "image/png", "bar".getBytes());

    when(fileInfoService.saveFile(mockFile)).thenThrow(FileException.class);

    Assertions.assertThrows(FileException.class, ()->{
      userService.updateUser(encryptedTestUser, userUpdateParam, mockFile);
    });

    verify(userMapper, never()).updateProfileInfo(any(UserUpdateInfo.class));
    verify(fileInfoService).saveFile(mockFile);
    verify(fileInfoService).deleteFile(any(Long.class));
  }

  @Test
  void updateUserPassword_success() {
    UserPasswordUpdateParam userPasswordUpdateParam =
        new UserPasswordUpdateParam("password", "test", "test");
    Assertions.assertDoesNotThrow(() -> {
      userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
    });
    verify(userMapper).updatePassword(any(UsernameAndPw.class));
  }

  @Test
  void updateUserPassword_fail_wrongCurrentPassword() {
    UserPasswordUpdateParam userPasswordUpdateParam =
        new UserPasswordUpdateParam("wrong_password", "test", "test");
    Assertions.assertThrows(InValidValueException.class,
        () -> userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam));
    verify(userMapper, never()).updatePassword(any(UsernameAndPw.class));
  }

  @Test
  void updateUserPassword_fail_sameCurrentAndNewPassword() {
    UserPasswordUpdateParam userPasswordUpdateParam =
        new UserPasswordUpdateParam(encryptedTestUser.getPassword(),
            encryptedTestUser.getPassword(), encryptedTestUser.getPassword());
    Assertions.assertThrows(InValidValueException.class, () -> {
      userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
    });
    verify(userMapper, never()).updatePassword(any(UsernameAndPw.class));
  }

  @Test
  void updateUserPassword_fail_notSameNewAndCheckPassword() {
    UserPasswordUpdateParam userPasswordUpdateParam =
        new UserPasswordUpdateParam("password", "new", "check");
    Assertions.assertThrows(InValidValueException.class, () -> {
      userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
    });
    verify(userMapper, never()).updatePassword(any(UsernameAndPw.class));
  }

  @Test
  void deleteUser() {
    Assertions.assertDoesNotThrow(() -> {
      userService.deleteUser(encryptedTestUser, testUser.getPassword());
    });
    verify(userMapper).deleteByUsername(testUser.getUsername());
  }
}