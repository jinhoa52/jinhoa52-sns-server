package me.koobin.snsserver.mapper;

import java.util.List;
import me.koobin.snsserver.model.FileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

  void insertFile(FileInfo file);

  void deleteFile(Long id);

  FileInfo findById(Long id);

  void insertFiles(List<FileInfo> fileInfos);
}
