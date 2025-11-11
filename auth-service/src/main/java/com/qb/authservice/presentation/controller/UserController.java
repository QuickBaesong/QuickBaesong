package com.qb.authservice.presentation.controller;

import com.qb.authservice.application.service.UserAdminService;
import com.qb.authservice.application.service.UserService;
import com.qb.authservice.presentation.dto.request.SignupRequest;
import com.qb.authservice.presentation.dto.response.SignupResponse;
import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserAdminService userAdminService;

    @PreAuthorize("hasRole('MASTER')")
    @PostMapping
    public ApiResponse<?> createUser(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = userAdminService.createUser(request);
        return ApiResponse.of(SuccessCode.CREATED, response);
    }
}
