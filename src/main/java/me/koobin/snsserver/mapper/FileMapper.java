package me.koobin.snsserver.mapper;

import me.koobin.snsserver.model.FileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

  Long insertFile(FileInfo file);

  void deleteFile(Long id);

  FileInfo findById(Long id);
}
