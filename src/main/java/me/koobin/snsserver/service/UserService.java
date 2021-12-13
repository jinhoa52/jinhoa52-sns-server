package me.koobin.snsserver.service;

import me.koobin.snsserver.exception.InValidValueException;
import me.koobin.snsserver.model.user.User;
import me.koobin.snsserver.model.user.UserPasswordUpdateParam;
import me.koobin.snsserver.model.user.UserSignUpParam;
import me.koobin.snsserver.model.user.UserUpdateParam;
import me.koobin.snsserver.model.user.UsernameAndPw;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  boolean signUp(UserSignUpParam userSignUpParam);

  boolean isUsernameDupe(String username);

  User getLoginUser(UsernameAndPw usernameAndPw);

  void updateUser(User currentUser, UserUpdateParam userUpdateParam, MultipartFile profile);

  void updateUserPassword(User currentUser, UserPasswordUpdateParam userPasswordUpdateParam)throws InValidValueException;

  void deleteUser(User currentUser, String currentPassword) throws InValidValueException;
}
