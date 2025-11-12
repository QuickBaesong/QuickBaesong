package com.qb.hubservice.presentation.response;

import com.qb.hubservice.domain.model.HubRoute;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class GetHubRouteResponse {

    private UUID hubId;

    private final UUID hubRouteId;

    private final UUID startHubId;

    private final UUID destinationHubId;

    private final BigDecimal distance;

    private final Long duration;
    public static GetHubRouteResponse fromEntity(HubRoute hubRoute) {
        return GetHubRouteResponse.builder()
                .hubRouteId(hubRoute.getHubRouteId())
                .hubId(hubRoute.getHub().getHubId())
                .startHubId(hubRoute.getStartHub().getHubId())
                .destinationHubId(hubRoute.getDestinationHub().getHubId())
                .duration(hubRoute.getDuration())
                .distance(hubRoute.getDistance())
                .build();
    }


}
