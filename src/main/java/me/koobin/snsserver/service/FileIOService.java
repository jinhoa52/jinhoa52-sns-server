package me.koobin.snsserver.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileIOService {

  void deleteFile(String saveFileName);

  void uploadFile(MultipartFile multipartFile, String saveFileName);
}
