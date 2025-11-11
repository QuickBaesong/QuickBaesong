package com.qb.hubservice.presentation.controller;

import com.qb.common.response.ApiResponse; // Assuming ApiResponse is here
import com.qb.common.response.PageResponse;
import com.qb.common.response.SuccessCode; // Assuming SuccessCode is here
import com.qb.hubservice.application.service.HubService;
import com.qb.hubservice.presentation.request.CreateHubRequest;
import com.qb.hubservice.presentation.response.GetHubResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Required for ResponseEntity usage
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hub")
public class HubController {

    private final HubService hubService;

    /**
     * 허브 정보 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse<GetHubResponse>> createHub(@RequestBody @Valid CreateHubRequest request) {
        GetHubResponse response = hubService.createHub(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(SuccessCode.CREATED, response));
    }

    /**
     * 허브 단건 정보 조회
     */
    @GetMapping("/{hubId}")
    public ResponseEntity<ApiResponse<GetHubResponse>> getHub(@PathVariable UUID hubId) {
        GetHubResponse response = hubService.getHub(hubId);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.OK, response));
    }


}