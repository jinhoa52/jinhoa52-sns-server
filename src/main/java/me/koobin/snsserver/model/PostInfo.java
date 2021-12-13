package me.koobin.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
public class PostInfo {

  private Long id;
  private String content;
  private Long userId;
}
