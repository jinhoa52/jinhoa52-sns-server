package me.koobin.snsserver.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PostFileInfo {
  private Long id;
  private Long userId;
  private String content;
  private List<PostFile> postFiles = new ArrayList<>();


}
