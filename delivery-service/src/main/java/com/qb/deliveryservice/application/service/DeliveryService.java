package com.qb.deliveryservice.application.service;

import com.qb.deliveryservice.application.dto.*;
import com.qb.deliveryservice.domain.model.Delivery;
import com.qb.deliveryservice.domain.repository.DeliveryRepository;
import com.qb.deliveryservice.domain.specification.DeliverySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    // 배송 생성
    @Transactional
    public DeliveryResponse create(DeliveryCreateRequest req) {
        Delivery delivery = req.toEntity();
        Delivery saved = deliveryRepository.save(delivery);
        return DeliveryResponse.from(saved);
    }

    // 배송 조회
    public DeliveryDetailResponse getDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findByIdAndNotDeleted(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("배송 정보를 찾을 수 없습니다."));


        // Mock 데이터 사용
        return DeliveryDetailResponse.builder()
                .deliveryId(delivery.getId())
                .order(DeliveryDetailResponse.OrderInfo.builder()
                        .orderId(delivery.getOrderId())
                        .orderQuantity(50)
                        .requiredDeliveryAt(LocalDateTime.now().plusDays(7))
                        .build())
                .deliveryManagerId(delivery.getCompanyManagerId())
                .deliveryManagerName("미배정")
                .companyId(UUID.randomUUID())
                .companyName("테스트 업체")
                .userId("test-user")
                .userName("테스트 사용자")
                .itemId(UUID.randomUUID())
                .itemName("테스트 상품")
                .hubId(delivery.getDestinationHubId())
                .hubName("테스트 허브")
                .currentStatus(delivery.getCurrentStatus())
                .deliveryAddress(delivery.getDeliveryAddress())
                .recipientName(delivery.getRecipientName())
                .recipientSlackId(delivery.getRecipientSlackId())
                .createdAt(delivery.getCreatedAt())
                .createdBy(delivery.getCreatedBy())
                .updatedAt(delivery.getUpdatedAt())
                .updatedBy(delivery.getUpdatedBy())
                .build();
    }

    public Page<DeliveryListResponse> searchDeliveries(DeliverySearchCondition condition, Pageable pageable) {
        Specification<Delivery> spec = DeliverySpecification.searchWith(condition);
        Page<Delivery> deliveryPage = deliveryRepository.findAll(spec, pageable);

        return deliveryPage.map(delivery -> DeliveryListResponse.builder()
                .deliveryId(delivery.getId())
                .orderId(delivery.getOrderId())
                .currentStatus(delivery.getCurrentStatus())
                .companyName("테스트 업체")
                .itemName("테스트 상품")
                .destinationHubName("테스트 허브")
                .deliveryManagerName("미배정")
                .recipientName(delivery.getRecipientName())
                .createdAt(delivery.getCreatedAt())
                .build());

    }

}
