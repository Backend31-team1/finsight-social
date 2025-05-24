package com.project.common.security;

import com.project.common.UserRole;
import com.project.common.UserVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtAuthenticationProvider {

  private static final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24; // 24시간
  private static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

  private final SecretKey signingKey;

  public JwtAuthenticationProvider(@Value("${jwt.secret}") String secretKey) {
    this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  //리프레쉬 토큰 발급 메서드 추가[004-Logout]
  public String createRefreshToken(String email, Long id) {
    Date now = new Date();
    return Jwts.builder()
        .setSubject(email)
        .claim("id", id)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
        .signWith(signingKey, SignatureAlgorithm.HS512)
        .compact();
  }

  // 엑세스 토큰 발급하는 메서드
  public String createAccessToken(String email, Long id, UserRole role) {
    Date now = new Date();
    return Jwts.builder()
        .setSubject(email)
        .claim("id", id)
        .claim("roles", List.of(role.name()))
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  // 토큰을 받아서 검증하는 메서드
  public boolean validateToken(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(token)
          .getBody();

      return !claims.getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  // 토큰에서 User 의 id 와 email 을 반환해주는 메서드
  public UserVo getUserVo(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(signingKey)
        .build()
        .parseClaimsJws(token)
        .getBody();

    Long id = claims.get("id", Long.class);
    String email = claims.getSubject();
    return new UserVo(id, email);
  }

  // Roles 클레임꺼내는 메서드
  @SuppressWarnings("unchecked")
  public List<String> getRoles(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(signingKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims.get("roles", List.class);
  }
}