package com.project.auth.service;

import static com.project.common.exception.ErrorCode.ALREADY_VERIFY;
import static com.project.common.exception.ErrorCode.EXPIRE_CODE;
import static com.project.common.exception.ErrorCode.NOT_FOUND_USER;
import static com.project.common.exception.ErrorCode.WRONG_VERIFICATION;

import com.project.auth.dto.SignupForm;
import com.project.auth.entity.User;
import com.project.auth.repository.UserRepository;
import com.project.common.UserRole;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // 클라이언트 입력정보로 부터 회원가입 ( DB 저장 )
  public User userSignup(SignupForm form) {

    UserRole role = UserRole.USER; // 클라이언트 입력에 의존하지 않음

    return userRepository.save(User.builder()
        .email(form.getEmail().toLowerCase(Locale.ROOT))
        // 비밀번호는 암호화 처리해서 저장
        .password(passwordEncoder.encode(form.getPassword()))
        .name(form.getName())
        .birth(form.getBirth())
        .nickname(form.getNickname().toLowerCase(Locale.ROOT))
        .emailVerified(false)
        .role(role)
        .build());
  }

  // 회원 가입시 이메일 중복 검증 (이미 존재하는 이메일의 경우 true 반환)
  public boolean isEmailExist(String email) {
    return userRepository.findByEmail(email.toLowerCase(Locale.ROOT))
        .isPresent();
  }

  // 회원 가입시 닉네임 중복 검증 (이미 존재하는 닉네임의 경우 true 반환)
  public boolean isNicknameExist(String nickname) {
    return userRepository.findByNickname(nickname.toLowerCase(Locale.ROOT))
        .isPresent();
  }

  // 이메일 인증 mailgun 메일발송시 포함될 코드 생성 메서드
  RandomStringGenerator gen = new RandomStringGenerator.Builder()
      .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
      .build();

  public String createCode() {
    return gen.generate(10);
  }

  // 인증 이메일 내용에 들어갈 부분을 작성하는 메서드
  public String buildEmailBody(String name, String verifyLink) {
    return new StringBuilder()
        .append("안녕하세요 ").append(name).append("님!!\n\n")
        .append("회원가입을 완료하려면 아래 링크를 클릭해 이메일을 인증해 주세요: \n")
        .append(verifyLink)
        .toString();
  }

  //회원가입시 메일인증전 가입된정보에 인증정보를 할당해주는 메서드
  @Transactional
  public void changeUserVerifyInfo(Long userId, String code) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    user.setVerificationCode(code);
    user.setVerifyExpiredAt(LocalDateTime.now().plusHours(1));
  }

  // 이메일 인증 링크를 누르면 비교를 통한 코드 및 이메일 인증이 실행될 부분의 실제 비교 로직
  @Transactional
  public void verifyEmail(String email, String code) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    // boolean 의 @getter 는 get~ 아니고 is~
    if (user.isEmailVerified()) {
      throw new CustomException(ALREADY_VERIFY);
    } else if (!user.getVerificationCode().equals(code)) {
      throw new CustomException(WRONG_VERIFICATION);
    } else if (user.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
      throw new CustomException(EXPIRE_CODE);
    }
    user.setEmailVerified(true);
  }


}
