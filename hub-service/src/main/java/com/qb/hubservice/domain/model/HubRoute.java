package com.qb.hubservice.domain.model;

import com.qb.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "p_hub_route", schema = "hub_schema")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubRoute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_route_id", columnDefinition = "UUID")
    private UUID hubRouteId;

    // 기준 허브 (p_hub_route.hub_id)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "start_hub_id", nullable = false)
    private Hub startHub;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_hub_id", nullable = false)
    private Hub destinationHub;


    @Column(name = "duration", nullable = false)
    private Long duration;


    @Column(name = "distance", nullable = false, precision = 12, scale = 3)
    private BigDecimal distance;

    public static HubRoute of(Hub hub, Hub startHub, Hub destinationHub,
                              Long duration, BigDecimal distance) {
        HubRoute hubRoute = new HubRoute();
        hubRoute.hub = hub;
        hubRoute.startHub = startHub;
        hubRoute.destinationHub = destinationHub;
        hubRoute.duration = duration;
        hubRoute.distance = distance;
        return hubRoute;
    }


}
