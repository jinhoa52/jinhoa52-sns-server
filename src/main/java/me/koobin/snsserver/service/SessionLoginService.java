package me.koobin.snsserver.service;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.util.SessionKey;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

  private final HttpSession httpSession;
  @Override
  public void loginUser(User user) {
    httpSession.setAttribute(SessionKey.USER, user);
  }

  @Override
  public void logoutUser() {
    httpSession.invalidate();
  }
}
