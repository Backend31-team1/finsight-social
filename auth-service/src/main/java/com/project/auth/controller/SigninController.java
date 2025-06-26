package com.project.auth.controller;

import com.project.auth.application.SigninApplication;
import com.project.auth.dto.AuthData;
import com.project.auth.dto.AuthResponse;
import com.project.auth.dto.SigninForm;
import com.project.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SigninController {

  private final SigninApplication signinApplication;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/signin")
  public ResponseEntity<AuthResponse> signinUser(@RequestBody SigninForm form) {
    AuthData authData = signinApplication.userLoginToken(form);
    AuthResponse result = new AuthResponse(
        authData.getAccessToken(),
        refreshTokenService.createToken(authData.getUserId(), authData.getEmail())
    );
    return ResponseEntity.ok(result);
  }
}
