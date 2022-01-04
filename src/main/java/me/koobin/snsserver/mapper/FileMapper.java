package me.koobin.snsserver.mapper;

import java.util.List;
import me.koobin.snsserver.model.FileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

  void insertFile(FileInfo file);

  void deleteFileBy(Long id);

  FileInfo findById(Long id);

  void insertFiles(List<FileInfo> fileInfos);

  List<FileInfo> findByIdIn(List<Long> fileIds);

  void deleteFileByIn(List<FileInfo> fileInfos);
}
