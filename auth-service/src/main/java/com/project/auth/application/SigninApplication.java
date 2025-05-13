package com.project.auth.application;

import static com.project.common.exception.ErrorCode.LOGIN_CHECK_FAIL;

import com.project.auth.dto.SigninForm;
import com.project.auth.entity.User;
import com.project.auth.service.SigninService;

import com.project.common.exception.CustomException;

import com.project.common.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SigninApplication {

  private final SigninService signinService;
  private final JwtAuthenticationProvider provider;

  public String userLoginToken(SigninForm form) {
    User u = signinService.findValidUser(form.getEmail(), form.getPassword())
        .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

    return provider.createToken(u.getEmail(), u.getId(), u.getRole());
  }
}