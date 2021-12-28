package me.koobin.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
public class FileUploadInfo {

  private MultipartFile multipartFile;
  private String saveFileName;
}
