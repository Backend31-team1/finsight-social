package com.project.auth.service;

import com.project.auth.entity.User;
import com.project.auth.repository.UserRepository;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service @RequiredArgsConstructor
public class ProfileService {
  private final UserRepository userRepo;
  private final S3StorageService s3;

  @Transactional
  public String uploadProfilePhoto(Long userId, MultipartFile file) throws IOException {
    User user = userRepo.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    // 이전 키가 있으면 삭제
    if (user.getProfilePhotoKey() != null) {
      s3.delete(user.getProfilePhotoKey());
    }

    // 새 파일 업로드 → key 저장
    String newKey = s3.upload(file);
    user.setProfilePhotoKey(newKey);

    // 필요시 외부에 리턴할 URL 생성
    return s3.getUrl(newKey);
  }

  @Transactional
  public void deleteProfilePhoto(Long userId) {
    User user = userRepo.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    if (user.getProfilePhotoKey() != null) {
      s3.delete(user.getProfilePhotoKey());
      user.setProfilePhotoKey(null);
    }
  }
}
