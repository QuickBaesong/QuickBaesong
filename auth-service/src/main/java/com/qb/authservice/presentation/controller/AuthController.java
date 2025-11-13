package com.qb.authservice.presentation.controller;

import com.qb.authservice.application.service.AuthService;
import com.qb.authservice.presentation.dto.request.LoginRequest;
import com.qb.authservice.presentation.dto.request.SignupRequest;
import com.qb.authservice.presentation.dto.response.SignupResponse;
import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public SignupResponse signup(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
//        return ApiResponse.of(SuccessCode.CREATED, authService.signup(request));
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok(authService.login(request));
        return ApiResponse.of(SuccessCode.OK, authService.login(request));
    }
}
