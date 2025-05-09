package com.project.auth.application;

import com.project.auth.client.SendMailForm;
import com.project.auth.client.mailgun.MailgunClient;
import com.project.auth.dto.SignupForm;
import com.project.auth.entity.User;
import com.project.auth.service.SignupService;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class SignupApplication {

  private final SignupService signupService;
  private final MailgunClient mailgunClient;

  //배포시 환경변수 재설정
  @Value("${app.base-url}")
  private String baseUrl;

  @Value("${mailgun.api.domain}")
  private String domain;

  //회원가입 메서드
  @Transactional
  public String userSignup(SignupForm form) {

    //이메일 중복 검사
    if (signupService.isEmailExist(form.getEmail())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTER_EMAIL);
    }
    //닉네임 중복 검사
    if (signupService.isNicknameExist(form.getNickname())) {
      throw new CustomException(ErrorCode.ALREADY_REGISTER_NICKNAME);
    }

    // 검사 완료 후 DB에 가입 회원의 정보저장
    User user = signupService.userSignup(form);

    // mailgun 을 통한 이메일 인증을 위해 랜덤코드 생성
    String code = signupService.createCode();

    // 랜덤코드를 담은 전송할 검증용 링크 생성
    String verifyLink = UriComponentsBuilder
        .fromUriString(baseUrl)
        .path("/signup/verify")
        .queryParam("email", form.getEmail())
        .queryParam("code", code)
        .toUriString();

    // mailgun 으로 실제 전송할 메일 내용 작성
    SendMailForm sendMailForm = SendMailForm.builder()
        .from("FinSight-Social 인증 <postmaster@sandbox4cbc225f64694fff9e4669348b36c4bd.mailgun.org>")
        .to(form.getEmail())
        .subject("FinSight-Social Verification Email!")
        .text(signupService.buildEmailBody(form.getName(), verifyLink))
        .build();

    // mailgun 을 통해 메일 회원가입 메일 발송
    mailgunClient.sendEmail(domain, sendMailForm).getBody();

    //DB에 가입유저의 검증코드 저장 및 인증 제한시간 설정
    signupService.changeUserVerifyInfo(user.getId(), code);

    return "회원가입을 성공적으로 완료하였습니다. 이메일 인증을 진행해주세요.";
  }

  // 이메일 인증 링크를 누르면 비교를 통한 코드 및 이메일 인증이 실행될 부분
  public void userVerify(String email, String code) {
    signupService.verifyEmail(email, code);
  }

}
