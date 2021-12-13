package me.koobin.snsserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FileUploadInfo {
  private MultipartFile multipartFile;
  private String saveFileName;
}
