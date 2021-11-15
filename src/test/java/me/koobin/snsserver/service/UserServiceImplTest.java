package me.koobin.snsserver.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.mapper.UserMapper;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserIdAndPassword;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserUpdateInfo;
import me.koobin.snsserver.model.UserUpdateParam;
import me.koobin.snsserver.util.PasswordEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class UserServiceImplTest {

  @Mock
  UserMapper userMapper;

  @InjectMocks
  UserServiceImpl userService;

  User testUser;

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
        .build();

    encryptedTestUser = User.builder()
        .username("id")
        .password(PasswordEncryptor.encrypt("password"))
        .email("email@email.com")
        .phoneNumber("010-0000-0000")
        .name("test1")
        .id(1L)
        .build();
  }

  @Test
  void signUp_success() {
    when(userMapper.isUsernameDupe(testUser.getUsername())).thenReturn(false);

    userService.signUp(testUser);

    verify(userMapper).isUsernameDupe(any(String.class));
    verify(userMapper).insertUser(any(User.class));
  }

  @Test
  void signUp_fail() {
    when(userMapper.isUsernameDupe(testUser.getUsername())).thenReturn(true);

    userService.signUp(testUser);

    verify(userMapper).isUsernameDupe(any(String.class));
    verify(userMapper, never()).insertUser(any(User.class));
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
    UserIdAndPassword userIdAndPassword = new UserIdAndPassword(testUser.getUsername(),
        testUser.getPassword());
    when(userMapper.getPassword(userIdAndPassword.getUsername())).thenReturn(
        encryptedTestUser.getPassword());
    when(userMapper.getUser(userIdAndPassword)).thenReturn(encryptedTestUser);

    User loginUser = userService.getLoginUser(userIdAndPassword);

    Assertions.assertNotNull(loginUser);
    Assertions.assertEquals(testUser.getUsername(), loginUser.getUsername());
    verify(userMapper).getPassword(userIdAndPassword.getUsername());
    verify(userMapper).getUser(userIdAndPassword);
  }

  @Test
  void getLoginUser_fail() {
    UserIdAndPassword userIdAndPassword = new UserIdAndPassword(testUser.getUsername(), "wrong");
    when(userMapper.getPassword(userIdAndPassword.getUsername())).thenReturn(
        encryptedTestUser.getPassword());

    User loginUser = userService.getLoginUser(userIdAndPassword);

    Assertions.assertNull(loginUser);
    verify(userMapper, never()).getUser(userIdAndPassword);
  }

  @Test
  void updateUser() {
    String username = testUser.getUsername();

    UserUpdateParam userUpdateParam =
        new UserUpdateParam("update_name", "010-1111-2222", "update@email.com");
    userService.updateUser(username, userUpdateParam);

    verify(userMapper).updateUser(any(UserUpdateInfo.class));
  }

  @Test
  void updateUserPassword_success() {
    UserPasswordUpdateParam userPasswordUpdateParam =
        new UserPasswordUpdateParam("password", "test", "test");
    Assertions.assertDoesNotThrow(() -> {
      userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
    });
    verify(userMapper).updateUserPassword(any(UserIdAndPassword.class));
  }

  @Test
  void updateUserPassword_fail_wrongCurrentPassword() {
    UserPasswordUpdateParam userPasswordUpdateParam =
        new UserPasswordUpdateParam("wrong_password", "test", "test");
    Assertions.assertThrows(InValidValueException.class,
        () -> userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam));
    verify(userMapper, never()).updateUserPassword(any(UserIdAndPassword.class));
  }

  @Test
  void updateUserPassword_fail_sameCurrentAndNewPassword() {
    UserPasswordUpdateParam userPasswordUpdateParam =
        new UserPasswordUpdateParam(encryptedTestUser.getPassword(),
            encryptedTestUser.getPassword(), encryptedTestUser.getPassword());
    Assertions.assertThrows(InValidValueException.class, () -> {
      userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
    });
    verify(userMapper, never()).updateUserPassword(any(UserIdAndPassword.class));
  }

  @Test
  void updateUserPassword_fail_notSameNewAndCheckPassword() {
    UserPasswordUpdateParam userPasswordUpdateParam =
        new UserPasswordUpdateParam("password", "new", "check");
    Assertions.assertThrows(InValidValueException.class, () -> {
      userService.updateUserPassword(encryptedTestUser, userPasswordUpdateParam);
    });
    verify(userMapper, never()).updateUserPassword(any(UserIdAndPassword.class));
  }

  @Test
  void deleteUser() {
    Assertions.assertDoesNotThrow(()->{
      userService.deleteUser(encryptedTestUser, testUser.getPassword());
    });
    verify(userMapper).deleteUser(testUser.getUsername());
  }
}