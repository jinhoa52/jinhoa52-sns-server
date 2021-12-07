package me.koobin.snsserver.service.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
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
  public void uploadFile(MultipartFile multipartFile, String saveFileName) {

    try {
      PutObjectRequest putObjectRequest = PutObjectRequest
          .builder()
          .bucket(bucket)
          .key(saveFileName)
          .build();
      ByteBuffer byteBuffer = ByteBuffer.wrap(multipartFile.getBytes());
      RequestBody requestBody = RequestBody.fromByteBuffer(byteBuffer);
      s3Client.putObject(putObjectRequest, requestBody);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
