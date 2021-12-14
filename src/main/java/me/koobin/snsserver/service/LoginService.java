package me.koobin.snsserver.service;

import me.koobin.snsserver.model.user.User;

public interface LoginService {

  void loginUser(User user);

  void logoutUser();


  Long getCurrentUserId();
}
