package me.koobin.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateParam {
  private final String name;

  private final String phoneNumber;

  private final String email;

}
