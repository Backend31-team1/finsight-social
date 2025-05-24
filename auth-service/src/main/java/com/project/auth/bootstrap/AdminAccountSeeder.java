package com.project.auth.bootstrap;

import com.project.auth.entity.User;
import com.project.auth.repository.UserRepository;
import com.project.common.UserRole;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * 실행시 관리자 계정을 만들어주는 시더 입니다.
 * application.yml 의 환경변수 지정명 보고 각자의 방식으로 알맞게 적용해서 사용하시면 됩니다.
  */

@Component
@RequiredArgsConstructor
public class AdminAccountSeeder implements ApplicationRunner {

  private final UserRepository repo;
  private final PasswordEncoder pe;

  @Value("${admin.email}")
  private String adminEmail;

  @Value("${admin.password}")
  private String adminRawPwd;

  @Override
  public void run(ApplicationArguments args) {
    if (!repo.findByEmail(adminEmail).isPresent()) {
      repo.save(User.builder()
          .email(adminEmail)
          .password(pe.encode(adminRawPwd))
          .name("관리자")
          .nickname("admin")
          .birth(LocalDate.of(1999,1,1))
          .emailVerified(true)
          .role(UserRole.ADMIN)
          .build());
    }
  }
}
