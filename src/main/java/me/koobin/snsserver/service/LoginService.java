package me.koobin.snsserver.service;

import me.koobin.snsserver.model.User;

public interface LoginService {

  void loginUser(User user);

  void logoutUser();


  Long getCurrentUserId();
}
