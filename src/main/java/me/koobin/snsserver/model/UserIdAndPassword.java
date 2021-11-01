package me.koobin.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserIdAndPassword {
  private final String username;
  private final String password;

}
