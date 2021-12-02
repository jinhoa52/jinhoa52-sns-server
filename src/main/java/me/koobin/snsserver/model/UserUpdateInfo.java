package me.koobin.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserUpdateInfo {

  private final String username;

  private final String name;

  private final String phoneNumber;

  private final String email;

  private final String profileMessage;

  private final Long profileId;
}
