package com.project.sns.service;

import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3Client s3Client;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucketName;

  @Value("${spring.cloud.aws.s3.base-url}")
  private String bucketBaseUrl;

  public String uploadFile(MultipartFile file) {
    // 원본 파일명 확장자 얻는부분
    String originalFilename = file.getOriginalFilename();
    String ext = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    // S3에 저장할 고유 키 생성, 형식은 "posts/20250603-uuid.jpg" 이러한 형태로 생성되도록 하는 코드
    String key = "posts/" + Instant.now().toEpochMilli() + "-" + UUID.randomUUID() + ext;

    try {
      PutObjectRequest putObjReq = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .contentType(file.getContentType())
          .contentLength(file.getSize())
          .build();
      //S3에 업로드
      s3Client.putObject(putObjReq, RequestBody.fromBytes(file.getBytes()));

      return bucketBaseUrl + "/" + key;

    } catch (IOException e) {

      throw new CustomException(ErrorCode.S3_UPLOAD_FAILED);
    }

  }
  /**
   * S3에 저장된 객체를 삭제하는 메서드
   * @param fileUrl public URL 형태는 "https://<bucket>.s3.<region>.amazonaws.com/<key>"
   */
  public void deleteFile(String fileUrl) {
    if (fileUrl == null || fileUrl.isEmpty()) {
      return;
    }
    // bucketBaseUrl + "/" + key
    String prefix = bucketBaseUrl + "/";
    if (!fileUrl.startsWith(prefix)) {
      return;
    }
    String key = fileUrl.substring(prefix.length());

    DeleteObjectRequest deleteReq = DeleteObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    s3Client.deleteObject(deleteReq);
  }

}
