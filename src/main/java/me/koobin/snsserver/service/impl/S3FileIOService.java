package me.koobin.snsserver.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import me.koobin.snsserver.exception.FileIoException;
import me.koobin.snsserver.service.FileIOService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3FileIOService implements FileIOService {

  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Override
  public void deleteFile(String saveFileName) {
    amazonS3Client.deleteObject(bucket, saveFileName);
  }

  @Override
  public void uploadFile(MultipartFile multipartFile, String saveFileName) {

    File file = convertMultiPartToFile(multipartFile);
    amazonS3Client.putObject(
        new PutObjectRequest(bucket, saveFileName, file)
            .withCannedAcl(CannedAccessControlList.PublicRead));

    try {
      Files.delete(Paths.get(file.getPath()));
    } catch (IOException e) {
      throw new FileIoException(e.getMessage());
    }

  }

  private File convertMultiPartToFile(final MultipartFile multipartFile) {
    final File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
    try (final FileOutputStream outputStream = new FileOutputStream(file)) {
      outputStream.write(multipartFile.getBytes());
    } catch (IOException e) {
      throw new FileIoException(e.getMessage());
    }
    return file;
  }
}
