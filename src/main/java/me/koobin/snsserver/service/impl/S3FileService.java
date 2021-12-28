package me.koobin.snsserver.service.impl;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.model.FileInfo;
import me.koobin.snsserver.model.FileStorage;
import me.koobin.snsserver.service.FileService;
import me.koobin.snsserver.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3FileService implements FileService {

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Override
  public void deleteFile(String saveFileName) {
    s3Client.deleteObject(DeleteObjectRequest.builder()
        .bucket(bucket)
        .key(saveFileName)
        .build());
  }

  @Override
  public FileInfo uploadFile(MultipartFile multipartFile) {
    String originalFilename = multipartFile.getOriginalFilename();
    String saveFileName = FileUtil.createFileName(originalFilename);
    URL reportUrl = null;
    try {

      PutObjectRequest putObjectRequest = PutObjectRequest
          .builder()
          .bucket(bucket)
          .key(saveFileName)
          .build();

      ByteBuffer byteBuffer = ByteBuffer.wrap(multipartFile.getBytes());

      RequestBody requestBody = RequestBody.fromByteBuffer(byteBuffer);
      s3Client.putObject(putObjectRequest, requestBody);

      reportUrl = s3Client.utilities()
          .getUrl(GetUrlRequest.builder()
              .bucket(bucket)
              .key(saveFileName)
              .build()
          );
    } catch (IOException e) {
      e.printStackTrace();
    }
    return FileInfo.builder()
        .fileName(originalFilename)
        .saveFileName(saveFileName)
        .contentType(multipartFile.getContentType())
        .fileStorage(FileStorage.S3)
        .path(String.valueOf(reportUrl))
        .build();
  }

  @Override
  public List<FileInfo> uploadFiles(List<MultipartFile> fileUploadInfos) {
    return fileUploadInfos.stream().map(this::uploadFile)
        .collect(Collectors.toList());
  }
}
