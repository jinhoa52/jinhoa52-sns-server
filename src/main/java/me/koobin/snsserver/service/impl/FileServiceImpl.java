package me.koobin.snsserver.service.impl;

import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.mapper.FileMapper;
import me.koobin.snsserver.model.FileInfo;
import me.koobin.snsserver.service.FileService;
import me.koobin.snsserver.service.FileUploadService;
import me.koobin.snsserver.util.FileUpload;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {


  private final FileMapper fileMapper;

  @Qualifier("s3FileUploadService")
  private final FileUploadService fileUploadService;

  @Override
  public Long saveFile(MultipartFile multipartFile)  {
    String originalFileName = multipartFile.getOriginalFilename();
    String saveFileName = FileUpload.createFileName(originalFileName);
    FileInfo fileInfo = FileInfo
        .builder()
        .fileName(originalFileName)
        .saveFileName(saveFileName)
        .contentType(multipartFile.getContentType())
        .build();

    fileUploadService.uploadFile(multipartFile, saveFileName);
    fileMapper.insertFile(fileInfo);
    return fileInfo.getId();
  }

  @Override
  public void deleteFile(Long id) {
    FileInfo file = fileMapper.findById(id);
    fileUploadService.deleteFile(file.getSaveFileName());
    fileMapper.deleteFile(id);
  }
}
