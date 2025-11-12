package com.qb.authservice.application.service;

import com.qb.authservice.application.component.AuthComponent;
import com.qb.authservice.application.component.JwtTokenProvider;
import com.qb.authservice.domain.entity.User;
import com.qb.authservice.domain.repository.UserRepository;
import com.qb.authservice.exception.AuthCustomException;
import com.qb.authservice.exception.AuthErrorCode;
import com.qb.authservice.presentation.dto.request.LoginRequest;
import com.qb.authservice.presentation.dto.request.SignupRequest;
import com.qb.authservice.presentation.dto.response.LoginResponse;
import com.qb.authservice.presentation.dto.response.SignupResponse;
import com.qb.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthComponent authComponent;
    private final JwtTokenProvider jwtTokenProvider;

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

    /**
     * 로그인
     * - 회원 검증
     * - 비밀번호 검증
     * - 승인된 사용자인지 검증
     * @param request 아이디, 비밀번호
     * @return
     */
    public LoginResponse login(LoginRequest request) {

        // TODO: 예외 처리 메소드로 분리
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new CustomException(AuthErrorCode.NOT_FOUND_USER));
        if (!authComponent.passwordMatches(request.password(), user.getPassword())) {
            throw new CustomException(AuthErrorCode.NOT_VALID_PASSWORD);
        }
        if (!user.isApproved()) {
            throw new CustomException(AuthErrorCode.NOT_APPROVED_USER);
        }

        String token = jwtTokenProvider.createAccessToken(user.getId(),user.getUsername(), user.getRole());

        return LoginResponse.of(user, token);
    }





    public void validateDuplicatedUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AuthCustomException(AuthErrorCode.DUPLICATE_ID);
        }
    }
}
