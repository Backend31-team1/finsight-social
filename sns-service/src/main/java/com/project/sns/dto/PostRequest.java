package com.project.sns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
  @NotNull
  private Long userId;

  @NotBlank
  @Size(max = 255)
  private String title;

  @NotBlank
  private String content;

  private String postImageUrl;

  private MultipartFile imageFile;
}
