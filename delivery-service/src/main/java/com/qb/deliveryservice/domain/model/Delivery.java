package com.qb.deliveryservice.domain.model;

import com.qb.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_delivery")
@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    @Column(name = "company_manager_id", nullable = false)
    private String companyManagerId; // 업체배송담당자 ID



}
