package me.koobin.snsserver.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  void deleteFile(String saveFileName);

  void uploadFile(MultipartFile multipartFile, String saveFileName);
}
