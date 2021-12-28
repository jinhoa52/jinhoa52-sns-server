package me.koobin.snsserver.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.koobin.snsserver.exception.FileException;
import me.koobin.snsserver.model.FileInfo;
import me.koobin.snsserver.model.FileStorage;
import me.koobin.snsserver.model.FileUploadInfo;
import me.koobin.snsserver.service.FileService;
import me.koobin.snsserver.util.FileUtil;
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
  public FileInfo uploadFile(MultipartFile multipartFile) {
    String originalFilename = multipartFile.getOriginalFilename();
    String saveFileName = FileUtil.createFileName(originalFilename);
    String uploadFilePath = BASE_DIR + saveFileName;
    try {
      multipartFile.transferTo(new File(uploadFilePath));
    } catch (IOException e) {
      throw new FileException(e.getMessage());
    }
    return FileInfo.builder()
        .fileName(originalFilename)
        .saveFileName(saveFileName)
        .contentType(multipartFile.getContentType())
        .fileStorage(FileStorage.LOCAL)
        .path(uploadFilePath)
        .build();


  }

  @Override
  public List<FileInfo> uploadFiles(List<MultipartFile> multipartFiles) {
    return multipartFiles.stream().map(this::uploadFile)
        .collect(Collectors.toList());
  }
}
