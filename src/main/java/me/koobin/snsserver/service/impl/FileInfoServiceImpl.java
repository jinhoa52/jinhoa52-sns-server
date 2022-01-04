package me.koobin.snsserver.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.mapper.FileMapper;
import me.koobin.snsserver.model.FileInfo;
import me.koobin.snsserver.service.FileInfoService;
import me.koobin.snsserver.service.FileService;
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

    FileInfo fileInfo = fileService.uploadFile(multipartFile);

    fileMapper.insertFile(fileInfo);
    return fileInfo.getId();
  }


  @Override
  public List<Long> saveFiles(List<MultipartFile> multipartFiles) {

    List<FileInfo> fileInfos = fileService.uploadFiles(multipartFiles);
    fileMapper.insertFiles(fileInfos);
    return fileInfos.stream().map(FileInfo::getId).collect(Collectors.toList());
  }

  @Override
  public void deleteFile(Long id) {
    FileInfo file = fileMapper.findById(id);
    fileService.deleteFile(file.getSaveFileName());
    fileMapper.deleteFileBy(id);
  }

  @Override
  public void deleteAllFile(List<Long> fileIds) {
    List<FileInfo> fileInfos = fileMapper.findByIdIn(fileIds);
    fileService.deleteFiles(fileInfos.stream().map(FileInfo::getSaveFileName).collect(Collectors.toList()));
    fileMapper.deleteFileByIn(fileInfos);

  }
}
