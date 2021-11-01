package me.koobin.snsserver.service;

import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.model.UserIdAndPassword;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserUpdateParam;

public interface UserService {

  void signup(User user);

  boolean isUsernameDupe(String username);

  User getLoginUser(UserIdAndPassword userIdAndPassword);

  void updateUser(String username, UserUpdateParam userUpdateParam);

  void updateUserPassword(User username, UserPasswordUpdateParam userPasswordUpdateParam)throws InValidValueException;

  void deleteUser(String username);
}
