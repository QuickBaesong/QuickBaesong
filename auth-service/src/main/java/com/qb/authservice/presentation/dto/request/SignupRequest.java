package com.qb.authservice.presentation.dto.request;

import com.qb.authservice.domain.entity.User;
import com.qb.authservice.domain.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record SignupRequest (
    @NotBlank(message = "아이디는 필수 입력 사항입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,10}$",
            message = "아이디는 알파벳 소문자(a~z)와 숫자만 가능합니다.")
    @Size(min = 4, max = 10, message = "아이디는 최소 4자 이상, 10자 이하입니다.")
    String username,

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,15}$",
            message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자(@$!%*?&#)를 포함해야 합니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상, 15자 이하입니다.")
    String password,

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    String userKname,

    @NotNull(message = "회사 혹은 허브 ID는 필수 입력 사항입니다.")
    UUID companyId,

    @NotBlank(message = "슬랙 ID는 필수 입력 사항입니다.")
    String slackId,

    @NotNull(message = "직책은 필수 입력 사항입니다.")
    UserRole role
) {
    public User toEntity(String encodedPassword, boolean approved) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .userKname(userKname)
                .companyId(companyId)
                .slackId(slackId)
                .role(role)
                .isApproved(approved)
                .isPublic(true)
                .build();
    }
}
