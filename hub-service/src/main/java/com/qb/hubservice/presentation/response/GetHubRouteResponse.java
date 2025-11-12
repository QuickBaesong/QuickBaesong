package com.qb.hubservice.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.qb.hubservice.domain.model.HubRoute;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetHubRouteResponse {
    private UUID hubRouteId;
    private UUID hubId;
    private UUID startHubId;
    private UUID destinationHubId;
    private Long duration;
    private BigDecimal distance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GetHubRouteResponse from(HubRoute hubRoute) {
        if (hubRoute == null) {
            return null;
        }

        return GetHubRouteResponse.builder()
                .hubRouteId(hubRoute.getHubRouteId())
                .hubId(hubRoute.getHub() != null ? hubRoute.getHub().getHubId() : null)
                .startHubId(hubRoute.getStartHub() != null ? hubRoute.getStartHub().getHubId() : null)
                .destinationHubId(hubRoute.getDestinationHub() != null ? hubRoute.getDestinationHub().getHubId() : null)
                .duration(hubRoute.getDuration())
                .distance(hubRoute.getDistance())
                .createdAt(hubRoute.getCreatedAt())
                .updatedAt(hubRoute.getUpdatedAt())
                .build();
    }


}
