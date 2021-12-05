package me.koobin.snsserver.service;

import java.io.IOException;
import me.koobin.snsserver.exception.FileIoException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

  void deleteFile(String saveFileName);

  void uploadFile(MultipartFile multipartFile, String saveFileName);
}
