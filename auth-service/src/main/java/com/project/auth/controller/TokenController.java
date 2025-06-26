package com.project.auth.controller;

import com.project.auth.dto.AuthResponse;
import com.project.auth.entity.RefreshToken;
import com.project.auth.entity.User;
import com.project.auth.repository.UserRepository;
import com.project.auth.service.RefreshTokenService;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.common.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TokenController {

  private final RefreshTokenService rts;
  private final JwtAuthenticationProvider provider;
  private final UserRepository userRepo;

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refreshToken(@RequestBody Map<String, String> body) {
    String oldRt = body.get("refreshToken");
    RefreshToken rt = rts.verify(oldRt);
    User u = userRepo.findById(rt.getUserId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    String newAccessToken = provider.createAccessToken(u.getEmail(), u.getId(), u.getRole());
    String newRefresh = rts.createToken(u.getId(), u.getEmail());
    return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefresh));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestBody Map<String, String> body){
    String oldRt = body.get("refreshToken");
    RefreshToken rt = rts.verify(oldRt);
    rts.deleteByUserId(rt.getUserId());
    return ResponseEntity.noContent().build();
  }
}
