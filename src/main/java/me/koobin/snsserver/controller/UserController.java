package me.koobin.snsserver.controller;


import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.model.SignInfo;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.service.UserService;
import me.koobin.snsserver.util.SessionKey;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;
  private static final ResponseEntity<Void> RESPONSE_OK = new ResponseEntity<>(HttpStatus.OK);
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
  public ResponseEntity<Void> login(@RequestBody SignInfo signInfo, HttpSession httpSession) {
    User user = userService.getLoginUser(signInfo);
    if (user == null) {
      return RESPONSE_UNAUTHORIZED;
    } else {
      httpSession.setAttribute(SessionKey.USER, user);
      return RESPONSE_OK;
    }
  }

  @GetMapping("/logout")
  public ResponseEntity<Void> logout(HttpSession httpSession) {
    httpSession.invalidate();
    return RESPONSE_OK;
  }
}
