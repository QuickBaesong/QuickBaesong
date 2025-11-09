package com.qb.authservice.presentation.dto.response;

import com.qb.authservice.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class SignupResponse {
    private UUID id;
    private String username;
    private String userKname;
    private UUID companyId;
    private String slackId;
    private String role;
    private boolean isApproved;

    public static SignupResponse from(User user) {
        return SignupResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userKname(user.getUserKname())
                .companyId(user.getCompanyId())
                .slackId(user.getSlackId())
                .role(user.getRole().name())
                .isApproved(user.isApproved())
                .build();
    }
}
