package com.qb.authservice.application.service;

import com.qb.authservice.domain.entity.User;
import com.qb.authservice.domain.repository.UserRepository;
import com.qb.authservice.presentation.dto.request.SignupRequest;
import com.qb.authservice.presentation.dto.response.SignupResponse;
import com.qb.common.enums.ErrorCode;
import com.qb.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse signup(SignupRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.DUPLICATE_ID);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.createUser(
                request.getUsername(),
                encodedPassword,
                request.getUserKname(),
                request.getCompanyId(),
                request.getSlackId(),
                request.getRole()
        );
        userRepository.save(user);
        return SignupResponse.from(user);
    }
}
