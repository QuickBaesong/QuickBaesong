package com.qb.authservice.application.service;

import com.qb.authservice.application.component.AuthComponent;
import com.qb.authservice.domain.entity.User;
import com.qb.authservice.domain.repository.UserRepository;
import com.qb.authservice.exception.AuthCustomException;
import com.qb.authservice.exception.AuthErrorCode;
import com.qb.authservice.presentation.dto.request.SignupRequest;
import com.qb.authservice.presentation.dto.response.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 어드민 관련된 회원 서비스를 처리하는 계층 (마스터 권한)
// 특정 회원 조회, 특정 회원의 권한 조정, 특정 회원의 정보 수정, 회원 생성 등
@Service
@RequiredArgsConstructor
public class UserAdminService {
    private final UserRepository userRepository;
    private final AuthComponent authComponent;

    @Transactional
    public SignupResponse createUser(SignupRequest request) {
        validateDuplicatedUser(request.username());
        String encodedPassword = authComponent.encodePassword(request.password());

        User user = request.toEntity(encodedPassword, true); // 미리 승인 처리
        userRepository.save(user);

        return SignupResponse.from(user); // TODO: 리턴 내용 변경
    }


    public void validateDuplicatedUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AuthCustomException(AuthErrorCode.DUPLICATE_ID);
        }
    }
}

