package com.project.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // 회원가입 관련 예외
  ALREADY_REGISTER_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다."),
  ALREADY_REGISTER_NICKNAME(HttpStatus.BAD_REQUEST, "이미 가입된 닉네임입니다."),
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "인증정보가 일치하는 회원을 찾을 수 없습니다."),
  ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증된 이메일 입니다."),
  WRONG_VERIFICATION(HttpStatus.BAD_REQUEST, "인증코드가 일치하지 않습니다."),
  EXPIRE_CODE(HttpStatus.BAD_REQUEST, "인증 제한시간을 초과하였습니다."),

  //로그인 관련 예외
  LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "아이디 또는 패스워드를 확인해 주세요"),

  //포트폴리오 관련 예외
  NOT_FOUNT_PORTFOLIO(HttpStatus.BAD_REQUEST, "존재하지 않는 포트폴리오입니다.."),

  //종목 관련 예외
  NOT_FOUND_ASSET(HttpStatus.BAD_REQUEST, "존재하지 않는 종목입니다."),

  //거래 관련 예외
  NOT_FOUND_TRANSACTION(HttpStatus.BAD_REQUEST, "존재하지 않는 거래입니다."),

  //리프레쉬토큰 및 로그아웃 관련 예외
  INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"리프레쉬토큰 발급을 위한 토큰이 존재하지 않습니다."),
  REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "리프레쉬토큰이 만료되었습니다.");
  private final HttpStatus httpStatus;
  private final String detail;
}

