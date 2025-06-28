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
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationProvider provider;
  private final RestAuthenticationEntryPoint entryPoint;
  private final RestAccessDeniedHandler deniedHandler;

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(provider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(eh -> eh
            .authenticationEntryPoint(entryPoint)
            .accessDeniedHandler(deniedHandler))
        .authorizeHttpRequests(auth -> auth
            // 0) Actuator health/info 공개
            .requestMatchers("/actuator/health", "/actuator/info").permitAll()

            // 1) Swagger UI & OpenAPI 공개
            .requestMatchers(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/webjars/**"
            ).permitAll()

            // 2) API 인증·회원가입·토큰 발급 등 공개
            .requestMatchers(
                "/api/auth/signin",
                "/api/auth/signup",
                "/api/auth/signup/verify",
                "/api/auth/refresh"
            ).permitAll()

            // 3) WebSocket용 엔드포인트 (필요시)
            .requestMatchers(
                "/test-ws.html", "/ws/**",
                "/app/**", "/queue/**"
            ).permitAll()

            // 4) 로그아웃은 인증 필요(리프레시 토큰 검증)
            .requestMatchers("/api/auth/logout").authenticated()

            // 5) 관리자 엔드포인트
            .requestMatchers("/api/sns/admin/**").hasRole("ADMIN")

            // 6) 그 외 모든 API는 인증 필요
            .anyRequest().authenticated()
        )
        // JWT 필터 등록
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
