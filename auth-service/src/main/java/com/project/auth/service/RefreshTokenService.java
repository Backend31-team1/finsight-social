package com.project.auth.service;

import com.project.auth.entity.RefreshToken;
import com.project.auth.repository.RefreshTokenRepository;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import com.project.common.security.JwtAuthenticationProvider;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository repo;
  private final JwtAuthenticationProvider provider;

  private final long refreshTokenValidityMs = 1000L * 60 * 60 * 24 * 7;       // 기한 7일

  // 1,리프레쉬 토큰 발급 메서드
  @Transactional
  public String createToken(long userId, String email) {

    // 기존 토큰을 삭제하고
    repo.deleteByUserId(userId);

    // 리프레쉬 토큰 생성 (role 없이 진행했음)
    String refreshToken = provider.createRefreshToken(email, userId);

    repo.save(RefreshToken.builder()
        .userId(userId)
        .token(refreshToken)
        .expiryDate(Instant.now().plusMillis(refreshTokenValidityMs))
        .build());

    return refreshToken;
  }

  //2.리프레쉬토큰 검증 메서드
  @Transactional(readOnly = true)
  public RefreshToken verify(String token) {
    RefreshToken rt = repo.findByToken(token)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
    if (rt.getExpiryDate().isBefore(Instant.now())) {
      repo.delete(rt);
      throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }
    return rt;
  }

  //3.리프레쉬토큰 삭제 메서드
  @Transactional
  public void deleteByUserId(Long userId) {
    repo.deleteByUserId(userId);
  }

}
