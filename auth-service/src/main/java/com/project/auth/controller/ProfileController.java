package com.project.auth.controller;

import com.project.auth.service.ProfileService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth/users/{id}/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileService profileService;

  @PostMapping("/photo")
  public ResponseEntity<String> uploadPhoto(
      @PathVariable Long id,
      @RequestParam("file") MultipartFile file
  ) throws IOException {
    String url = profileService.uploadProfilePhoto(id, file);
    return ResponseEntity.ok(url);
  }

  @DeleteMapping("/photo")
  public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
    profileService.deleteProfilePhoto(id);
    return ResponseEntity.noContent().build();
  }
}
