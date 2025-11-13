package com.qb.deliveryservice.domain.model;

import com.qb.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_delivery", schema = "delivery_schema")
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "order_id", nullable = false, columnDefinition = "UUID")
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    private DeliveryStatus currentStatus;

    @Column(name = "departure_hub_id", nullable = false, columnDefinition = "UUID")
    private UUID departureHubId; // 출발 허브

    @Column(name = "destination_hub_id", nullable = false, columnDefinition = "UUID")
    private UUID destinationHubId; // 도착 허브

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress; // 주소(배송정보 API에서 통으로 받음)

    @Column(name = "recipient_name", nullable = false)
    private String recipientName; // 수령인

    @Column(name = "recipient_slack_id", nullable = false)
    private String recipientSlackId; // 슬랙ID

    @Column(name = "company_manager_id", columnDefinition = "UUID")
    private UUID companyManagerId; // 업체 배송 담당자 ID (UUID) 나중에 배정

    @Column(name = "last_assigned_hub_sequence")
    private Integer lastAssignedHubSequence; // 마지막으로 배정받은 순서

    @Builder
    private Delivery(
            UUID orderId,
            DeliveryStatus currentStatus,
            UUID departureHubId,
            UUID destinationHubId,
            String deliveryAddress,
            String recipientName,
            String recipientSlackId,
            UUID companyManagerId
    ) {
        this.orderId = orderId;
        this.currentStatus = currentStatus;
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.deliveryAddress = deliveryAddress;
        this.recipientName = recipientName;
        this.recipientSlackId = recipientSlackId;
        this.companyManagerId = companyManagerId;
        this.lastAssignedHubSequence = -1;
    }

    /**
     * 업체 배송 담당자 배정 (나중에 업데이트)
     */
    public void assignCompanyManager(UUID companyManagerId) {
        this.companyManagerId = companyManagerId;
    }

    public void updateLastAssignedHubSequence(Integer sequence) {
        this.lastAssignedHubSequence = sequence;
    }

    public void updateStatus(DeliveryStatus status) {
        this.currentStatus = status;
    }


}
