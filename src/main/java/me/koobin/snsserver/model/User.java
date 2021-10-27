package me.koobin.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class User {

  private final Long id;

  private final String username;

  private final String password;

  private String email;

  private String phoneNumber;

  private String name;

}
