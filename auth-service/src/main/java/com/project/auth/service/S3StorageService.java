package com.project.auth.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3StorageService {
  private final S3Client s3Client;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;
  @Value("${spring.cloud.aws.s3.profile-folder}")
  private String profileFolder;

  /** 업로드 후 S3 객체 Key를 반환 */
  public String upload(MultipartFile file) throws IOException {
    String ext = Optional.ofNullable(file.getOriginalFilename())
        .filter(n -> n.contains("."))
        .map(n -> n.substring(n.lastIndexOf(".")))
        .orElse("");
    String key = profileFolder + "/" + UUID.randomUUID() + ext;

    s3Client.putObject(PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(file.getContentType())
            .contentLength(file.getSize())
            .build(),
        RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    return key;
  }

  /** 주어진 Key로부터 public URL을 생성 */
  public String getUrl(String key) {
    return s3Client.utilities()
        .getUrl(b -> b.bucket(bucket).key(key))
        .toExternalForm();
  }

  /** Key로 바로 객체 삭제 */
  public void delete(String key) {
    s3Client.deleteObject(DeleteObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build());
  }
}
