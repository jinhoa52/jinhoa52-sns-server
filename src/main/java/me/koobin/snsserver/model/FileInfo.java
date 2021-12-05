package me.koobin.snsserver.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter @AllArgsConstructor
@Builder
public class FileInfo {

  private Long id;
  private String fileName;
  private String saveFileName;
  private String contentType;
  private int deleteFlag;
  private LocalDateTime createDate;
  private LocalDateTime modifyDate;


}
