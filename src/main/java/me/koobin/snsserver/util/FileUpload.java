package me.koobin.snsserver.util;

import java.util.UUID;

public class FileUpload {

  private FileUpload() {
    throw new IllegalStateException("Utility class");
  }

  public static String createFileName(String originalFileName){
    return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
  }

  public static String getFileExtension(String fileName){
    try {
      return fileName.substring(fileName.lastIndexOf("."));
    }catch (StringIndexOutOfBoundsException e){
      throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName));
    }
  }

}
