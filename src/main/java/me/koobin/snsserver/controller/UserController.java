package me.koobin.snsserver.controller;


import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.service.UserService;
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
  private static final ResponseEntity<Void> RESPONSE_CONFLICT = new ResponseEntity<>(HttpStatus.CONFLICT);


  @PostMapping
  public ResponseEntity<Void> signup(@RequestBody User user) {
    userService.signup(user);
    return RESPONSE_OK;
  }

  @GetMapping("{username}/exists")
  public ResponseEntity<Void> checkUsernameDupe(@PathVariable String username) {
    return userService.isUsernameDupe(username) ? RESPONSE_CONFLICT : RESPONSE_OK;

  }

}
