package com.qb.authservice.application.service;

import com.qb.authservice.application.component.AuthComponent;
import com.qb.authservice.domain.entity.User;
import com.qb.authservice.domain.entity.UserRole;
import com.qb.authservice.domain.repository.UserRepository;
import com.qb.authservice.presentation.dto.request.SignupRequest;
import com.qb.authservice.presentation.dto.response.SignupResponse;
import com.qb.common.enums.ErrorCode;
import com.qb.common.exception.CustomException;
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

    //TODO: createBy에 master id 받아서 입력
    @Transactional
    public SignupResponse createUser(SignupRequest request) {
//        validateMasterRole(role); // request의 role이 아니라 로그인한 유저의 role
        validateDuplicatedUser(request.username());
        String encodedPassword = authComponent.encodePassword(request.password());

        User user = request.toEntity(encodedPassword, true); // 미리 승인 처리
        userRepository.save(user);

        return SignupResponse.from(user); // TODO: 리턴 내용 변경
    }


    public void validateDuplicatedUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.DUPLICATE_ID);
        }
    }
    // MASTER 권한 검증
    public void validateMasterRole(UserRole role) {
        if (role != UserRole.MASTER) {
            throw new CustomException(ErrorCode.INVALID_MASTER_ROLE);
        }
    }
}

