package me.koobin.snsserver.service;

import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserIdAndPassword;
import me.koobin.snsserver.model.UserPasswordUpdateParam;
import me.koobin.snsserver.model.UserUpdateParam;

public interface UserService {

  boolean signUp(User user);

  boolean isUsernameDupe(String username);

  User getLoginUser(UserIdAndPassword userIdAndPassword);

  void updateUser(String username, UserUpdateParam userUpdateParam);

  void updateUserPassword(User user, UserPasswordUpdateParam userPasswordUpdateParam)throws InValidValueException;

  void deleteUser(User currentUser, String currentPassword) throws InValidValueException;
}
