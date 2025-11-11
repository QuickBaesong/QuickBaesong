package com.qb.authservice.presentation.dto.response;

import com.qb.authservice.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SignupResponse {
    private UUID id;
    private String role;
    private LocalDateTime createdDate;

    public static SignupResponse from(User user) {
        return SignupResponse.builder()
                .id(user.getId())
                .role(user.getRole().name())
                .createdDate(user.getCreatedAt())
                .build();
    }
}
