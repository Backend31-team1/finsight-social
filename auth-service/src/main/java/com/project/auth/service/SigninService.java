package com.project.auth.service;



import com.project.auth.entity.User;
import com.project.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SigninService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public Optional<User> findValidUser(String email, String rawPassword) {
    return userRepository.findByEmail(email)
        .stream()
        .filter(
            user -> passwordEncoder.matches(rawPassword, user.getPassword())
                && user.isEmailVerified()
        )
        .findFirst();
  }

}
