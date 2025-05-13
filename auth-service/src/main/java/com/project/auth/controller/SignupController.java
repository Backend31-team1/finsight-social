package com.project.auth.controller;

import com.project.auth.application.SignupApplication;
import com.project.auth.dto.SignupForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/signup")
@RequiredArgsConstructor
public class SignupController {

  private final SignupApplication signupApplication;

  @PostMapping
  public ResponseEntity<String> userSignup(@RequestBody SignupForm form){
    return ResponseEntity.ok(signupApplication.userSignup(form));
  }

  @GetMapping("/verify")
  public ResponseEntity<String> verifyUser(
      @RequestParam String email,
      @RequestParam String code
  ) {
    signupApplication.userVerify(email, code);
    return ResponseEntity.ok("성공적으로 인증을 완료하였습니다.");
  }
}
