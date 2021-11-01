package me.koobin.snsserver.controller;


import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.annotation.CheckLogin;
import me.koobin.snsserver.annotation.CurrentUser;
import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserIdAndPassword;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserUpdateParam;
import me.koobin.snsserver.service.LoginService;
import me.koobin.snsserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;
  private final LoginService loginService;

  private static final ResponseEntity<Void> RESPONSE_OK = new ResponseEntity(HttpStatus.OK);
  private static final ResponseEntity<Void> RESPONSE_CONFLICT = new ResponseEntity<>(
      HttpStatus.CONFLICT);
  private static final ResponseEntity<Void> RESPONSE_UNAUTHORIZED = new ResponseEntity<>(
      HttpStatus.UNAUTHORIZED);

  @PostMapping
  public ResponseEntity<Void> signup(@RequestBody User user) {
    userService.signup(user);
    return RESPONSE_OK;
  }

  @GetMapping("{username}/exists")
  public ResponseEntity<Void> checkUsernameDupe(@PathVariable String username) {
    return userService.isUsernameDupe(username) ? RESPONSE_CONFLICT : RESPONSE_OK;
  }

  @PostMapping("/login")
  public ResponseEntity<Void> login(@RequestBody UserIdAndPassword userIdAndPassword) {
    User user = userService.getLoginUser(userIdAndPassword);
    if (user == null) {
      return RESPONSE_UNAUTHORIZED;
    }
    loginService.loginUser(user);
    return RESPONSE_OK;

  }

  @GetMapping("/logout")
  public ResponseEntity<Void> logout() {
    loginService.logoutUser();
    return RESPONSE_OK;
  }

  @PutMapping("/profile")
  @CheckLogin
  public ResponseEntity<Void> updateUser(
      @RequestBody UserUpdateParam userUpdateParam, @CurrentUser User currentUser) {

    userService.updateUser(currentUser.getUsername(), userUpdateParam);
    return RESPONSE_OK;

  }

  @PutMapping("/profile/password")
  @CheckLogin
  public ResponseEntity updateUserPassword(
      @RequestBody UserPasswordUpdateParam userPasswordUpdateParam
      , @CurrentUser User currentUser) {

    try {
      userService.updateUserPassword(currentUser, userPasswordUpdateParam);
      loginService.logoutUser();
      return RESPONSE_OK;

    } catch (InValidValueException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

    }
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteUser(@CurrentUser User currentUser) {
    if (currentUser == null) {
      return RESPONSE_UNAUTHORIZED;
    }
    userService.deleteUser(currentUser.getUsername());
    return RESPONSE_OK;
  }
}
