package com.qb.deliveryservice.presentation.controller;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.common.response.PageResponse;
import com.qb.deliveryservice.application.dto.DeliveryDetailResponse;
import com.qb.deliveryservice.application.dto.DeliveryListResponse;
import com.qb.deliveryservice.application.dto.DeliveryResponse;
import com.qb.deliveryservice.application.dto.DeliverySearchCondition;
import com.qb.deliveryservice.application.service.DeliveryService;
import com.qb.deliveryservice.domain.model.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    // 단건 조회
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDetailResponse> getDelivery(
            @PathVariable UUID deliveryId
    ) {
        DeliveryDetailResponse response = deliveryService.getDelivery(deliveryId);
        return ResponseEntity.ok(response);
    }

    // 다건 조회
    @GetMapping
    public ResponseEntity<PageResponse<DeliveryListResponse>> searchDeliveries(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID managerId,
            @RequestParam(required = false) UUID hubId,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        DeliverySearchCondition condition = DeliverySearchCondition.builder()
                .status(status != null ? DeliveryStatus.valueOf(status) : null)
                .managerId(managerId)
                .hubId(hubId)
                .build();

        Page<DeliveryListResponse> page = deliveryService.searchDeliveries(condition, pageable);
        PageResponse<DeliveryListResponse> response = PageResponse.from(page);

        return ResponseEntity.ok(response);
    }
}
