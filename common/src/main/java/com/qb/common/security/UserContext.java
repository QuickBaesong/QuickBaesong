package com.qb.common.security;

import com.qb.common.enums.UserRole;

import java.util.UUID;

public record UserContext(
        UUID userId,
        String username,
        UserRole role
) {}
