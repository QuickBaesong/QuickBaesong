package com.qb.hubservice.presentation.controller;

import com.qb.common.response.ApiResponse;
import com.qb.common.enums.SuccessCode;
import com.qb.hubservice.application.service.HubService;
import com.qb.hubservice.presentation.request.CreateHubRequest;
import com.qb.hubservice.presentation.response.GetHubResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hub")
public class HubController {

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<ApiResponse<GetHubResponse>> createHub(@RequestBody @Valid CreateHubRequest request) {
        GetHubResponse response = hubService.createHub(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(SuccessCode.CREATED, response));
    }


    @GetMapping("/{hubId}")
    public ResponseEntity<ApiResponse<GetHubResponse>> getHub(@PathVariable UUID hubId) {
        GetHubResponse response = hubService.getHub(hubId);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.OK, response));
    }

    //@PostMapping("/dummy")



}