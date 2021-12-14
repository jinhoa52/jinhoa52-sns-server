package me.koobin.snsserver.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPasswordUpdateParam {

  private final String currentPassword;

  private final String newPassword;

  private final String checkNewPassword;
}
