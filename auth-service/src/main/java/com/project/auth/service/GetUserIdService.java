package com.project.auth.service;

import com.project.auth.entity.User;
import com.project.auth.repository.UserRepository;
import com.project.common.dto.UserIdDto;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserIdService {

    private final UserRepository userRepository;

    public UserIdDto getUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return new UserIdDto(user.getId());
    }
}
