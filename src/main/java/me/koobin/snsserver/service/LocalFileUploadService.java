package me.koobin.snsserver.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import me.koobin.snsserver.exception.FileIoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@PropertySource("classpath:application.yml")
public class LocalFileUploadService implements FileUploadService {

  // 맥북 기준
  @Value("${sns.baseDir}")
  private String BASE_DIR;

  @Override
  public void deleteFile(String saveFileName) {
    try {
      String saveFilePath = BASE_DIR + saveFileName;
      Files.delete(Paths.get(saveFilePath));
    } catch (IOException e) {
      throw new FileIoException(e.getMessage());
    }
  }

  @Override
  public void uploadFile(MultipartFile multipartFile, String saveFileName) {
    String uploadFilePath = BASE_DIR + saveFileName;
    try {
      multipartFile.transferTo(new File(uploadFilePath));
    } catch (IOException e) {
      throw new FileIoException(e.getMessage());
    }
  }
}
