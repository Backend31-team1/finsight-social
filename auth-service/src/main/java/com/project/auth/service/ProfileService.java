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

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final UserRepository userRepo;
  private final S3StorageService s3;

  /**
   * 프로필 사진 업로드
   * 1. 사용자 존재 확인
   * 2. 기존 사진이 있으면 S3에서 삭제
   * 3. 새 파일을 S3에 올리고 URL 을 받아
   * 4. 사용자 엔티티에 URL 저장 후 반환
   */
  @Transactional
  public String uploadProfilePhoto(Long userId, MultipartFile file) throws IOException {
    // 1) 디비 에서 사용자 조회 - 없으면 404
    User user = userRepo.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    // 2) 이미 프로필 URL 이 있으면, S3에서 이전 사진 삭제
    if (user.getProfilePhotoUrl() != null) {
      s3.delete(user.getProfilePhotoUrl());
    }

    // 3) 새 사진 업로드 → S3에서 발급된 public URL 획득
    String url = s3.upload(file);

    // 4) 엔티티에 URL만 업데이트 (flush/commit 시 자동 반영)
    user.setProfilePhotoUrl(url);

    return url;
  }

  /**
   * 프로필 사진 삭제
   * 1. 유저 조회
   * 2. URL 이 있으면 S3에서 삭제
   * 3. 엔티티 필드만 null 로 변경
   */
  @Transactional
  public void deleteProfilePhoto(Long userId) {
    // 사용자 조회
    User user = userRepo.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    // URL 이 있으면 S3에서 지우고
    if (user.getProfilePhotoUrl() != null) {
      s3.delete(user.getProfilePhotoUrl());
      // DB에 저장된 URL 정보 지우기
      user.setProfilePhotoUrl(null);
    }
  }
}
