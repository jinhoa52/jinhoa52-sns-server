package me.koobin.snsserver.mapper;

import me.koobin.snsserver.model.user.User;
import me.koobin.snsserver.model.user.UserSignUpParam;
import me.koobin.snsserver.model.user.UserUpdateInfo;
import me.koobin.snsserver.model.user.UsernameAndPw;
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
