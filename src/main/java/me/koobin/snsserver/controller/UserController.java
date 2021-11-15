package me.koobin.snsserver.controller;


import com.fasterxml.jackson.databind.node.TextNode;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.annotation.CheckLogin;
import me.koobin.snsserver.annotation.CurrentUser;
import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserIdAndPassword;
import me.koobin.snsserver.model.UserPassword;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserUpdateParam;
import me.koobin.snsserver.service.LoginService;
import me.koobin.snsserver.service.UserService;
import me.koobin.snsserver.util.ResponsesEntities;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;
  private final LoginService loginService;

  @PostMapping
  public ResponseEntity<Void> signup(@RequestBody User user) {
    boolean result = userService.signUp(user);
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
  public ResponseEntity<Void> updateUser(
      @RequestBody UserUpdateParam userUpdateParam, @CurrentUser User currentUser) {

    userService.updateUser(currentUser.getUsername(), userUpdateParam);
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
