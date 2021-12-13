package me.koobin.snsserver.service;

import java.util.List;
import me.koobin.snsserver.model.FileUploadInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  void deleteFile(String saveFileName);

  void uploadFiles(List<FileUploadInfo> fileUploadInfos);

  void uploadFile(FileUploadInfo fileUploadInfo);
}
