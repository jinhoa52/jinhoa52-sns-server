package me.koobin.snsserver.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import me.koobin.snsserver.exception.FileException;
import me.koobin.snsserver.model.FileUploadInfo;
import me.koobin.snsserver.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartFile;

@PropertySource("classpath:application.yml")
public class LocalFileService implements FileService {

  // 맥북 기준
  @Value("${sns.baseDir}")
  private String BASE_DIR;

  @Override
  public void deleteFile(String saveFileName) {
    try {
      String saveFilePath = BASE_DIR + saveFileName;
      Files.delete(Paths.get(saveFilePath));
    } catch (IOException e) {
      throw new FileException(e.getMessage());
    }
  }


  @Override
  public void uploadFile(FileUploadInfo fileUploadInfo) {
    String saveFileName = fileUploadInfo.getSaveFileName();
    MultipartFile multipartFile = fileUploadInfo.getMultipartFile();
    String uploadFilePath = BASE_DIR + saveFileName;
    try {
      multipartFile.transferTo(new File(uploadFilePath));
    } catch (IOException e) {
      throw new FileException(e.getMessage());
    }
  }

  @Override
  public void uploadFiles(List<FileUploadInfo> fileUploadInfos) {
    fileUploadInfos.forEach(this::uploadFile);
  }
}
