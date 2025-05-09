package com.project.auth.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupForm {

  private String email;
  private String password;
  private String name;
  private String nickname;
  private LocalDate birth;

}
