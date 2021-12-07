package me.koobin.snsserver.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileInfoService {

  Long saveFile(MultipartFile multipartFile);

  void deleteFile(Long id);
}
