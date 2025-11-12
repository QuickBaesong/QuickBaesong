package com.qb.deliveryservice.infrastructure.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponse {
    private String userId;
    private String username;  // 사용자명만 필요
}
