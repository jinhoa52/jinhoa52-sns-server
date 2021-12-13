package me.koobin.snsserver.service.impl;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.service.LoginService;
import me.koobin.snsserver.util.SessionKeys;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

  private final HttpSession httpSession;

  @Override
  public void loginUser(User user) {
    httpSession.setAttribute(SessionKeys.USER_ID, user.getId());
  }

  @Override
  public void logoutUser() {
    httpSession.invalidate();
  }

  @Override
  public Long getCurrentUserId() {
    return (Long) httpSession.getAttribute(SessionKeys.USER_ID);
  }
}
