package com.qb.deliveryservice.application.service;

import com.qb.deliveryservice.application.dto.*;
import com.qb.deliveryservice.domain.model.*;
import com.qb.deliveryservice.domain.repository.DeliveryManagerRepository;
import com.qb.deliveryservice.domain.repository.DeliveryManagerSequenceRepository;
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
    private final DeliveryManagerRepository managerRepository;
    private final DeliveryManagerSequenceRepository sequenceRepository;

    // 배송 생성
    @Transactional
    public DeliveryResponse create(DeliveryCreateRequest req) {
        // 1. 배송 엔티티 생성
        Delivery delivery = req.toEntity();
        Delivery savedDelivery = deliveryRepository.save(delivery);

        log.info("배송 생성 - deliveryId: {}, orderId: {}", savedDelivery.getId(), req.getOrderId());

        // ✨ 2. 목적지 허브 업체 담당자 자동 배정 (순환)
        DeliveryManagerSequence destinationSeq = sequenceRepository
                .findBySequenceTypeAndHubId("COMPANY", req.getDestinationHubId())
                .orElseGet(() -> {
                    DeliveryManagerSequence newSeq = DeliveryManagerSequence.builder()
                            .sequenceType("COMPANY")
                            .hubId(req.getDestinationHubId())
                            .lastSequence(-1)
                            .build();
                    return sequenceRepository.save(newSeq);
                });

        DeliveryManager destinationCompanyManager = getNextCompanyManager(
                req.getDestinationHubId(),
                destinationSeq.getLastSequence()
        );
        destinationSeq.updateSequence(destinationCompanyManager.getSequence());
        savedDelivery.assignCompanyManager(destinationCompanyManager.getId());

        log.info("목적지 허브 업체 담당자 배정 - hubId: {}, managerId: {}, sequence: {}",
                req.getDestinationHubId(),
                destinationCompanyManager.getId(),
                destinationCompanyManager.getSequence());

        // ✨ 3. 허브 담당자 자동 배정 (순환)
        DeliveryManagerSequence hubSeq = sequenceRepository
                .findBySequenceTypeAndHubIdIsNull("HUB")
                .orElseGet(() -> {
                    DeliveryManagerSequence newSeq = DeliveryManagerSequence.builder()
                            .sequenceType("HUB")
                            .hubId(null)
                            .lastSequence(-1)
                            .build();
                    return sequenceRepository.save(newSeq);
                });

        DeliveryManager hubManager = getNextHubManager(hubSeq.getLastSequence());
        hubSeq.updateSequence(hubManager.getSequence());
        savedDelivery.updateLastAssignedHubSequence(hubManager.getSequence());

        log.info("허브 담당자 배정 - managerId: {}, sequence: {}",
                hubManager.getId(),
                hubManager.getSequence());

        // 4. 배송 상태 변경
        savedDelivery.updateStatus(DeliveryStatus.HUB_MOVING);

        log.info("배송 생성 완료 - deliveryId: {}, orderId: {}",
                savedDelivery.getId(), req.getOrderId());

        // ✨ 5. 담당자 2명만 반환
        return DeliveryResponse.from(
                savedDelivery,
                hubManager,
                destinationCompanyManager
        );
    }

    private DeliveryManager getNextHubManager(Integer lastSequence) {
        return managerRepository.findNextHubManager(lastSequence)
                .orElseGet(() -> managerRepository.findFirstHubManager()
                        .orElseThrow(() -> new IllegalStateException("허브 배송 담당자가 등록되어 있지 않습니다.")));
    }

    private DeliveryManager getNextCompanyManager(UUID hubId, Integer lastSequence) {
        return managerRepository.findNextCompanyManager(hubId, lastSequence)
                .orElseGet(() -> managerRepository.findFirstCompanyManager(hubId)
                        .orElseThrow(() -> new IllegalStateException("업체 배송 담당자가 등록되어 있지 않습니다.")));
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
