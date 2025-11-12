package com.qb.authservice.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
    @NotBlank(message = "아이디는 필수 입력 사항입니다.")
    String username,

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    String password
) {}
