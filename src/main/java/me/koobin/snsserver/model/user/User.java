package me.koobin.snsserver.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder @NoArgsConstructor
public class User {

  private Long id;

  private String username;

  private String password;

  private String email;

  private String phoneNumber;

  private String name;

  private String profileMessage;

   private Long profileId;
}
