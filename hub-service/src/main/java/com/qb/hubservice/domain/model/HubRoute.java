package com.qb.hubservice.domain.model;

import com.qb.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "p_hub_route")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HubRoute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_route_id", columnDefinition = "UUID")
    private UUID hubRouteId;

    // 기준 허브 (p_hub_route.hub_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    // 출발 허브 (p_hub_route.start_hub_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "start_hub_id", nullable = false)
    private Hub startHub;

    // 도착 허브 (p_hub_route.destination_hub_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_hub_id", nullable = false)
    private Hub destinationHub;

    // 소요시간 (BIGINT)
    @Column(name = "duration", nullable = false)
    private Long duration;

    // 이동거리 (DECIMAL)
    @Column(name = "distance", nullable = false, precision = 12, scale = 3)
    private BigDecimal distance;

}
