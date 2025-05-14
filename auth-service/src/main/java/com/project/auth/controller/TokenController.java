package com.project.auth.controller;

import com.project.auth.dto.AuthResponse;
import com.project.auth.entity.RefreshToken;
import com.project.auth.entity.User;
import com.project.auth.repository.UserRepository;
import com.project.auth.service.RefreshTokenService;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.common.security.JwtAuthenticationProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class TokenController {

  private final RefreshTokenService rts;
  private final JwtAuthenticationProvider provider;
  private final UserRepository userRepo;

  /**
   * 새로운 엑세스 토큰 발급 및 리프레쉬 토큰 롤링 API
   * 프론트엔드 개발시 액세스 토큰이 만료된 경우 호출하세요.
   *  - 요청: POST /auth/refresh
   *    { "refreshToken": "<HttpOnly 쿠키 또는 body에 담긴 리프레시 토큰>" }
   *  - 응답: { "accessToken": "<새 액세스 토큰>", "refreshToken": "<새 리프레시 토큰>" }
   */
  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refreshToken(@RequestBody Map<String, String> body) {
    String oldRt = body.get("refreshToken");
    RefreshToken rt = rts.verify(oldRt);

    // DB 에서 User 엔티티 조회
    User u = userRepo.findById(rt.getUserId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    // 엑세스 토큰 새로 발급
    String newAccessToken = provider.createAccessToken(
        u.getEmail(),
        u.getId(),
        u.getRole()
    );

    // 리프레쉬 토큰 롤링 적용 (보안을 위해 새로발급)
    String newRefresh = rts.createToken(u.getId(), u.getEmail());

    return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefresh));
  }

  /**
   * 로그아웃 API (리프레쉬 토큰 삭제)
   */
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestBody Map<String, String> body){
    String oldRt = body.get("refreshToken");
    RefreshToken rt = rts.verify(oldRt);
    //리프레쉬 토큰 삭제
    rts.deleteByUserId(rt.getUserId());

    return ResponseEntity.noContent().build(); // noContent() -> HTTP 204 응답
  }
}
