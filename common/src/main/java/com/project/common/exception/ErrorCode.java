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
  NOT_FOUNT_PORTFOLIO(HttpStatus.BAD_REQUEST, "존재하지 않는 포트폴리오입니다."),
  PORTFOLIO_ACCESS_DENIED(HttpStatus.FORBIDDEN, "포트폴리오 접근 권한이 없습니다."),

  //종목 관련 예외
  NOT_FOUND_ASSET(HttpStatus.BAD_REQUEST, "존재하지 않는 종목입니다."),

  //거래 관련 예외
  NOT_FOUND_TRANSACTION(HttpStatus.BAD_REQUEST, "존재하지 않는 거래입니다."),
  CANNOT_CANCEL_ORDER(HttpStatus.BAD_REQUEST, "취소할 수 없는 주문 상태입니다."),

  //채팅방 관련 예외
  NOT_FOUND_CHATROOM(HttpStatus.BAD_REQUEST, "존재하지 않는 채팅방입니다."),

  //리프레쉬토큰 및 로그아웃 관련 예외
  INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레쉬토큰 발급을 위한 토큰이 존재하지 않습니다."),
  REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "리프레쉬토큰이 만료되었습니다."),

  // 권한 관련 예외
  FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),

  // 게시글 관련 예외
  NOT_FOUND_POST(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다."),

  // 댓글 관련 예외
  NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "존재하지 않는 댓글입니다."),
  COMMENT_TOO_LONG(HttpStatus.BAD_REQUEST, "댓글이 너무 깁니다."),
  EMPTY_COMMENT(HttpStatus.BAD_REQUEST, "댓글 내용이 비어있습니다."),
  COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "댓글 접근 권한이 없습니다."),

  // 알림 관련 예외
  NOT_FOUND_NOTIFICATION(HttpStatus.BAD_REQUEST, "존재하지 않는 알림입니다."),
  NOTIFICATION_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "알림 전송에 실패했습니다."),
  NOTIFICATION_ACCESS_DENIED(HttpStatus.FORBIDDEN, "알림 접근 권한이 없습니다."),

  // 주문 관련 예외
  INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "수량은 1 이상이어야 합니다."),
  INVALID_PRICE(HttpStatus.BAD_REQUEST, "가격은 0보다 커야 합니다."),
  INVALID_ORDER_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 주문 유형입니다."),
  ORDER_ALREADY_FILLED(HttpStatus.BAD_REQUEST, "이미 체결된 주문입니다."),
  ORDER_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다."),
  INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),
  INVALID_TTL(HttpStatus.BAD_REQUEST, "유효하지 않은 TTL 값입니다."),
  ORDER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "주문 접근 권한이 없습니다."),

  // 파일 업로드 관련 예외
  FILE_TOO_LARGE(HttpStatus.BAD_REQUEST, "파일 크기가 너무 큽니다."),
  INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
  FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),

  S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일업로드에 실패했습니다.");


  private final HttpStatus httpStatus;
  private final String detail;
}