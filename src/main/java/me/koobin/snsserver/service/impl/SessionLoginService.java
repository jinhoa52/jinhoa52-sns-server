package me.koobin.snsserver.service.impl;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserUpdateInfo;
import me.koobin.snsserver.service.LoginService;
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

  @Override
  public void updateUserInfo(User user, UserUpdateInfo userUpdateInfo) {
    final User updateUser = User.builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .name(userUpdateInfo.getName())
        .phoneNumber(userUpdateInfo.getPhoneNumber())
        .email(userUpdateInfo.getEmail())
        .profileMessage(userUpdateInfo.getProfileMessage())
        .profileId(userUpdateInfo.getProfileId())
        .build();
    httpSession.setAttribute(SessionKey.USER, updateUser);
  }

}
