package com.qb.authservice.presentation.dto.response;

import com.qb.authservice.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record LoginResponse(
        String username,
        String role,
        String accessToken
) {
    public static LoginResponse of(User user, String accessToken) {
        return LoginResponse.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .accessToken(accessToken)
                .build();
    }
}

