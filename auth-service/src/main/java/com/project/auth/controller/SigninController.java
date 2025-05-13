package com.project.auth.controller;

import com.project.auth.application.SigninApplication;
import com.project.auth.dto.SigninForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/signin")
@RequiredArgsConstructor
public class SigninController {

  private final SigninApplication signinApplication;

  @PostMapping()
  public ResponseEntity<String> signinUser(@RequestBody SigninForm form) {
    return ResponseEntity.ok(signinApplication.userLoginToken(form));
  }

}
