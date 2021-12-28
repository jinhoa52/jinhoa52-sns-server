package me.koobin.snsserver.service;

import java.util.List;
import me.koobin.snsserver.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  void deleteFile(String saveFileName);

  List<FileInfo> uploadFiles(List<MultipartFile> fileUploadInfos);

  FileInfo uploadFile(MultipartFile fileUploadInfo);
}
