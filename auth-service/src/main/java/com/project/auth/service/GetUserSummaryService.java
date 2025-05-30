package com.project.auth.service;

import com.project.auth.entity.User;
import com.project.auth.repository.UserRepository;
import com.project.common.dto.UserSummaryDto;
import com.project.common.exception.CustomException;
import com.project.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserSummaryService {

    private final UserRepository userRepository;

    public UserSummaryDto getUserSummary(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return new UserSummaryDto(user.getId(), user.getNickname());
    }
}
