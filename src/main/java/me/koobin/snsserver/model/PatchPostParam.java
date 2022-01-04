package me.koobin.snsserver.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PatchPostParam {
  private Long postId;
  private List<Long> fileIds;
  private String content;
}
