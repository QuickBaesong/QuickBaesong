package com.qb.deliveryservice.domain.model;

import com.qb.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_delivery_manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryManager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_manager_id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true, columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "hub_id", nullable = true, columnDefinition = "UUID")
    private UUID hubId;

    @Enumerated(EnumType.STRING)
    @Column(name = "manager_type", nullable = false, length = 20)
    private ManagerType managerType; // 배송 담당자 타입

    @Column(name = "slack_id", nullable = false, length = 50)
    private String slackId;

    /**
     * 배송 순번 (0, 1, 2, ..., 9)
     * - HUB 타입: 전체 시스템 기준으로 순번 배정
     * - COMPANY 타입: 각 허브 기준으로 순번 배정
     * 순차 배정: 0 → 1 → 2 → ... → 9 → 0 (순환)
     */
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Builder
    private DeliveryManager(
            UUID userId,
            UUID hubId,
            ManagerType managerType,
            String slackId,
            Integer sequence
    ) {
        this.userId = userId;
        this.hubId = hubId;
        this.managerType = managerType;
        this.slackId = slackId;
        this.sequence = sequence;
    }
}
