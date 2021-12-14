package me.koobin.snsserver.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.mapper.FileMapper;
import me.koobin.snsserver.model.FileInfo;
import me.koobin.snsserver.model.FileUploadInfo;
import me.koobin.snsserver.service.FileInfoService;
import me.koobin.snsserver.service.FileService;
import me.koobin.snsserver.util.FileUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class FileInfoServiceImpl implements FileInfoService {

  private final FileMapper fileMapper;

  @Qualifier("s3FileService")
  private final FileService fileService;

  @Override
  public Long saveFile(MultipartFile multipartFile) {
    String originalFileName = multipartFile.getOriginalFilename();
    String saveFileName = FileUtil.createFileName(originalFileName);
    FileInfo fileInfo = FileInfo
        .builder()
        .fileName(originalFileName)
        .saveFileName(saveFileName)
        .contentType(multipartFile.getContentType())
        .build();

    fileService.uploadFile(new FileUploadInfo(multipartFile, saveFileName));
    fileMapper.insertFile(fileInfo);
    return fileInfo.getId();
  }

  @Override
  public List<Long> saveFiles(List<MultipartFile> multipartFiles) {
    List<FileInfo> fileInfos = new ArrayList<>();
    List<FileUploadInfo> fileUploadInfos = new ArrayList<>();
    for (MultipartFile multipartFile : multipartFiles) {
      String originalFileName = multipartFile.getOriginalFilename();
      String saveFileName = FileUtil.createFileName(originalFileName);
      FileInfo fileInfo = FileInfo
          .builder()
          .fileName(originalFileName)
          .saveFileName(saveFileName)
          .contentType(multipartFile.getContentType())
          .build();
      fileInfos.add(fileInfo);
      fileUploadInfos.add(new FileUploadInfo(multipartFile, saveFileName));
    }
    fileService.uploadFiles(fileUploadInfos);
    fileMapper.insertFiles(fileInfos);

    return fileInfos.stream().map(FileInfo::getId).collect(Collectors.toList());
  }

  @Override
  public void deleteFile(Long id) {
    FileInfo file = fileMapper.findById(id);
    fileService.deleteFile(file.getSaveFileName());
    fileMapper.deleteFile(id);
  }
}
