package com.qb.deliveryservice.presentation.controller;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.deliveryservice.application.dto.DeliveryCreateRequest;
import com.qb.deliveryservice.application.dto.DeliveryResponse;
import com.qb.deliveryservice.application.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/deliveries")
// 나중에 SecurityConfig에 외부에서는 접근 못하도록 추가
public class DeliveryInternalController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryResponse>> createDelivery(@RequestBody DeliveryCreateRequest req) {
        DeliveryResponse resp = deliveryService.create(req);
        return ResponseEntity.ok(
                ApiResponse.of(SuccessCode.CREATED, resp)
        );
    }
}
