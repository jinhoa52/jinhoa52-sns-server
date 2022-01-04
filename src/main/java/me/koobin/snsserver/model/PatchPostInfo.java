package me.koobin.snsserver.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
 @Builder
public class PatchPostInfo {

  private Long userId;
  private Long postId;
  private String content;
  private List<MultipartFile> files ;
  private List<Long> fileIds;


}
