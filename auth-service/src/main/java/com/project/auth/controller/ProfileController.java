package com.project.auth.controller;

import com.project.auth.service.ProfileService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users/{id}/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileService profileService;

  /**
   * 프로필 사진 업로드
   * POST /users/{id}/profile/photo
   * @param id   사용자 ID
   * @param file MultipartFile (form-data key="file")
   * @return 업로드된 public URL
   */
  @PostMapping("/photo")
  public ResponseEntity<String> uploadPhoto(
      @PathVariable Long id,
      @RequestParam("file") MultipartFile file
  ) throws IOException {
    String url = profileService.uploadProfilePhoto(id, file);
    return ResponseEntity.ok(url);
  }

  /**
   * 프로필 사진 삭제
   * DELETE /users/{id}/profile/photo
   */
  @DeleteMapping("/photo")
  public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
    profileService.deleteProfilePhoto(id);
    return ResponseEntity.noContent().build();
  }
}

