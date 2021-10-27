package me.koobin.snsserver.service;

import me.koobin.snsserver.model.SignInfo;
import me.koobin.snsserver.model.User;

public interface UserService {

  void signup(User user);

  boolean isUsernameDupe(String username);

  User getLoginUser(SignInfo signInfo);
}
