package com.project.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler({
      CustomException.class
  })
  public ResponseEntity<ExceptionResponse> customRequestException(final CustomException c) {
    log.warn("api Exception : {}", c.getErrorCode());
    return ResponseEntity.badRequest().body(new ExceptionResponse(c.getMessage(), c.getErrorCode()));
  }

  @Getter
  @ToString
  @AllArgsConstructor
  public static class ExceptionResponse {

    private String message;
    private ErrorCode errorCode;
  }
}