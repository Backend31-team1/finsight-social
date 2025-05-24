package com.project.auth.service;

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

  /**
   * S3에 파일 업로드 후 public URL 반환
   */
  public String upload(MultipartFile file) throws IOException {
    String filename = file.getOriginalFilename();
    String ext = (filename != null && filename.contains("."))
        ? filename.substring(filename.lastIndexOf('.'))
        : "";
    String key = profileFolder + "/" + UUID.randomUUID() + ext;

    PutObjectRequest por = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .acl(ObjectCannedACL.PUBLIC_READ)
        .contentType(file.getContentType())
        .contentLength(file.getSize())
        .build();

    s3Client.putObject(por, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    return s3Client.utilities()
        .getUrl(b -> b.bucket(bucket).key(key))
        .toExternalForm();
  }

  /**
   * URL 에서 S3 key 추출 후 삭제
   */
  public void delete(String fileUrl) {
    String prefix = "https://" + bucket + ".s3.amazonaws.com/";
    if (!fileUrl.startsWith(prefix)) return;
    String key = fileUrl.substring(prefix.length());

    DeleteObjectRequest dor = DeleteObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();
    s3Client.deleteObject(dor);
  }
}

