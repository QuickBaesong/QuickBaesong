package com.qb.deliveryservice.presentation.controller;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.deliveryservice.application.dto.DeliveryManagerCreateRequest;
import com.qb.deliveryservice.application.dto.DeliveryManagerResponse;
import com.qb.deliveryservice.application.service.DeliveryManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/delivery-managers")
public class DeliveryManagerController {
    private final DeliveryManagerService managerService;

    // 배송 담당자 생성
    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryManagerResponse>> createManager(
            @RequestBody DeliveryManagerCreateRequest request
    ) {
        DeliveryManagerResponse response = managerService.create(request);
        return ResponseEntity.ok(ApiResponse.of(SuccessCode.CREATED, response));
    }
}
