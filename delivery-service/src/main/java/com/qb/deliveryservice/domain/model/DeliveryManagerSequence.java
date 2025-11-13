package com.qb.deliveryservice.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_delivery_manager_sequence")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryManagerSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "sequence_id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "sequence_type", length = 20, nullable = false)
    private String sequenceType; // "HUB" 또는 "COMPANY"

    @Column(name = "hub_id", columnDefinition = "UUID")
    private UUID hubId; // 업체 담당자용 허브 ID (허브 담당자는 null)

    @Column(name = "last_sequence", nullable = false)
    private Integer lastSequence;

    @Builder
    private DeliveryManagerSequence(String sequenceType, UUID hubId, Integer lastSequence) {
        this.sequenceType = sequenceType;
        this.hubId = hubId;
        this.lastSequence = lastSequence;
    }

    public void updateSequence(Integer sequence) {
        this.lastSequence = sequence;
    }
}
