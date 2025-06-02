package com.project.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthData {

  private String accessToken;
  private Long userId;
  private String email;
}
