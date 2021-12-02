package me.koobin.snsserver.mapper;

import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UserIdAndPassword;
import me.koobin.snsserver.model.UserSignUpParam;
import me.koobin.snsserver.model.UserUpdateInfo;
import org.apache.ibatis.annotations.Mapper;

/*
@Mapper
: 대상 interface를 MyBatis Mapper로 등록
 */
@Mapper
public interface UserMapper {

  void insertUser(UserSignUpParam userSignUpParam);

  boolean isUsernameDupe(String username);

  String getPassword(String username);

  User getUser(UserIdAndPassword userIdAndPassword);

  void updateUser(UserUpdateInfo userUpdateInfo);

  void updateUserPassword(UserIdAndPassword userIdAndPassword);

  void deleteUser(String username);
}
