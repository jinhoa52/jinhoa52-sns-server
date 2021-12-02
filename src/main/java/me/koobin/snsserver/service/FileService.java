package me.koobin.snsserver.service;

import me.koobin.snsserver.exception.FileIoException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  Long saveFile(MultipartFile multipartFile);

  void deleteFile(Long id);
}
