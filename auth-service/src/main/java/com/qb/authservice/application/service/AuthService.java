package com.qb.authservice.application.service;

import com.qb.authservice.application.component.AuthComponent;
import com.qb.authservice.domain.entity.User;
import com.qb.authservice.domain.repository.UserRepository;
import com.qb.authservice.presentation.dto.request.SignupRequest;
import com.qb.authservice.presentation.dto.response.SignupResponse;
import com.qb.common.enums.ErrorCode;
import com.qb.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthComponent authComponent;
    /**
     * 회원 가입
     * - 회원 가입 여부 확인(중복 회원 검증)
     * - 비밀번호 암호화
     * @param request 아이디, 비밀번호, 이름, 업체(허브)ID, 슬랙 아이디, 직책
     * @return
     */
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        validateDuplicatedUser(request.username());
        String encodedPassword = authComponent.encodePassword(request.password());

        User user = request.toEntity(encodedPassword, request.role().isDefaultApproval());
        userRepository.save(user);

        return SignupResponse.from(user);
    }

    public void validateDuplicatedUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.DUPLICATE_ID);
        }
    }
}
