package com.project.common.config;

import com.project.common.security.JwtAuthenticationFilter;
import com.project.common.security.JwtAuthenticationProvider;
import com.project.common.security.RestAccessDeniedHandler;
import com.project.common.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
//관리자만 사용할 컨트롤러나 서비스 메서드위에 @PreAuthorize("hasRole('ADMIN')") 이걸 붙이기 위한 어노테이션
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationProvider provider;
  private final RestAuthenticationEntryPoint entryPoint;
  private final RestAccessDeniedHandler deniedHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // API이므로 세션 기반 CSRF 필요 없음
        .csrf(csrf -> csrf.disable())
        // 세션 사용하지 않고 JWT만으로 인증
        .sessionManagement(sm -> sm
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // 예외 처리 핸들러 적용
        .exceptionHandling(eh -> eh
            .authenticationEntryPoint(entryPoint)
            .accessDeniedHandler(deniedHandler))
        // 인가 설정 ,로그인·회원가입 엔드포인트는 모두 허용, 나머지 요청은 모두 토큰인증 필요.
        .authorizeHttpRequests(auth -> auth
            // 인증 없이 열어줄 경로
            .requestMatchers(
                "/signin",
                "/signup",
                "/signup/verify",
                "/swagger-ui/**",
                "/v3/api-docs/**"
            ).permitAll()
            // ADMIN 권한이 필요한 URL
            .requestMatchers("/admin/**")
            .hasRole("ADMIN")
            // 나머지 경로는 모두 로그인 인증 필요
            .anyRequest().authenticated()
        )
        // JwtAuthenticationFilter 를 Spring Security 필터체인에 등록
        .addFilterBefore(
            new JwtAuthenticationFilter(provider),
            UsernamePasswordAuthenticationFilter.class
        );

    return http.build();
  }
}