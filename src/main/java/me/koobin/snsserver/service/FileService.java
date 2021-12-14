package me.koobin.snsserver.service;

import java.util.List;
import me.koobin.snsserver.model.FileUploadInfo;

public interface FileService {

  void deleteFile(String saveFileName);

  void uploadFiles(List<FileUploadInfo> fileUploadInfos);

  void uploadFile(FileUploadInfo fileUploadInfo);
}
