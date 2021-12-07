package me.koobin.snsserver.util;

import java.util.UUID;
import org.springframework.util.StringUtils;

public class FileUtil {

  private FileUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static String createFileName(String originalFileName){
    String uuid = UUID.randomUUID().toString();
    String extension = StringUtils.getFilenameExtension(originalFileName);
    return uuid + "." + extension;
  }
}
