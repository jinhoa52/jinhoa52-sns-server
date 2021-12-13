package me.koobin.snsserver.mapper;

import me.koobin.snsserver.model.User;
import me.koobin.snsserver.model.UsernameAndPw;
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

  String getPw(String username);

  User findByUsernameAndPw(UsernameAndPw usernameAndPw);

  User findByUserId(Long userId);

  void updateProfileInfo(UserUpdateInfo userUpdateInfo);

  void updatePassword(UsernameAndPw usernameAndPw);

  void deleteByUsername(String username);

}
