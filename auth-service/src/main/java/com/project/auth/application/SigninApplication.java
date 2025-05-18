package com.project.auth.application;

import com.project.auth.dto.AuthData;
import com.project.auth.dto.SigninForm;
import com.project.auth.entity.User;
import com.project.auth.service.SigninService;

import com.project.common.exception.CustomException;

import com.project.common.exception.ErrorCode;
import com.project.common.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SigninApplication {

  private final SigninService signinService;
  private final JwtAuthenticationProvider provider;

  public AuthData userLoginToken(SigninForm form) {
    User u = signinService.findValidUser(form.getEmail(), form.getPassword())
        .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));
    String access = provider.createAccessToken(u.getEmail(), u.getId(), u.getRole());
    return new AuthData(access, u.getId(), u.getEmail());
  }
}