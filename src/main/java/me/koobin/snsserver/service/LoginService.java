package me.koobin.snsserver.service;

import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserUpdateInfo;

public interface LoginService {

  void loginUser(User user);

  void logoutUser();

  void updateUserInfo(User user, UserUpdateInfo userUpdateInfo);
}
