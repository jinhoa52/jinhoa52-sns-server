package me.koobin.snsserver.controller;


import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.annotation.CheckLogin;
import me.koobin.snsserver.annotation.CurrentUser;
import me.koobin.snsserver.exception.FileIoException;
import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserIdAndPassword;
import me.koobin.snsserver.model.UserPassword;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserSignUpParam;
import me.koobin.snsserver.model.UserUpdateInfo;
import me.koobin.snsserver.model.UserUpdateParam;
import me.koobin.snsserver.service.LoginService;
import me.koobin.snsserver.service.UserService;
import me.koobin.snsserver.util.ResponsesEntities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;
  private final LoginService loginService;

  @PostMapping
  public ResponseEntity<Void> signup(@RequestBody UserSignUpParam userSignUpParam) {
    boolean result = userService.signUp(userSignUpParam);
    return result ? ResponsesEntities.RESPONSE_CREATED : ResponsesEntities.RESPONSE_CONFLICT;
  }

  @GetMapping("{username}/exists")
  public boolean checkUsernameDupe(@PathVariable String username) {
    return userService.isUsernameDupe(username);
  }

  @PostMapping("/login")
  public ResponseEntity<Void> login(@RequestBody UserIdAndPassword userIdAndPassword) {
    User user = userService.getLoginUser(userIdAndPassword);
    if (user == null) {
      return ResponsesEntities.RESPONSE_UNAUTHORIZED;
    }

    loginService.loginUser(user);
    return ResponsesEntities.RESPONSE_OK;

  }

  @GetMapping("/logout")
  public ResponseEntity<Void> logout() {
    loginService.logoutUser();
    return ResponsesEntities.RESPONSE_OK;
  }

  @PutMapping("/profile")
  @CheckLogin
  public ResponseEntity updateUser(
      UserUpdateParam userUpdateParam, @CurrentUser User currentUser
      , @RequestPart("profileImage") MultipartFile profile) {
    // profile 삭제 시 프로필 제거

    try {
      UserUpdateInfo userUpdateInfo = userService.updateUser(currentUser, userUpdateParam, profile);
      loginService.updateUserInfo(currentUser, userUpdateInfo);
    } catch (FileIoException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return ResponsesEntities.RESPONSE_OK;
  }

  @PutMapping("/profile/password")
  @CheckLogin
  public ResponseEntity<Void> updateUserPassword(
      @RequestBody UserPasswordUpdateParam userPasswordUpdateParam
      , @CurrentUser User currentUser) {

    try {
      userService.updateUserPassword(currentUser, userPasswordUpdateParam);
      loginService.logoutUser();
      return ResponsesEntities.RESPONSE_OK;

    } catch (InValidValueException e) {
      return ResponsesEntities.RESPONSE_CONFLICT;

    }
  }

  @DeleteMapping
  @CheckLogin
  public ResponseEntity<Void> deleteUser(@RequestBody UserPassword userPassword,
      @CurrentUser User currentUser) {
    try {
      userService.deleteUser(currentUser, userPassword.getCurrentPassword());
      loginService.logoutUser();
      return ResponsesEntities.RESPONSE_OK;
    } catch (InValidValueException e) {
      return ResponsesEntities.RESPONSE_UNAUTHORIZED;
    }
  }
}
