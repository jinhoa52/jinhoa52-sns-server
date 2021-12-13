package me.koobin.snsserver.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileInfoService {

  Long saveFile(MultipartFile multipartFile);

  List<Long> saveFiles(List<MultipartFile> multipartFiles);

  void deleteFile(Long id);
}
