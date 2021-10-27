package me.koobin.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.mapper.UserMapper;
import me.koobin.snsserver.model.SignInfo;
import me.koobin.snsserver.model.User;
import me.koobin.snsserver.util.PasswordEncryptor;
import org.springframework.stereotype.Service;

/*
@Service
: "모델에 독립된 인터페이스로 제공되는 작업"으로 정의된 서비스임을 명시
  비즈니스 로직을 처리할 클래스
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;

  @Override
  public void signup(User user) {
    String encodedPassword = PasswordEncryptor.encrypt(user.getPassword());
    User encryptedUser = User.builder()
        .username(user.getUsername())
        .password(encodedPassword)
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .name(user.getName())
        .build();
    userMapper.insertUser(encryptedUser);

  }

  @Override
  public boolean isUsernameDupe(String username) {
    return userMapper.isUsernameDupe(username);
  }

  @Override
  public User getLoginUser(SignInfo signInfo) {
    String password = userMapper.getPassword(signInfo.getUsername());
    if(password == null || !PasswordEncryptor.isMatch(signInfo.getPassword(), password))return null;

    return userMapper.getUser(signInfo);
  }
}
