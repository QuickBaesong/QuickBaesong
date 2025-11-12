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

    private final UUID hubRouteId;

    private final UUID startHubId;

    private final UUID destinationHubId;

    private final BigDecimal distance;

    private final Long duration;

    public static GetHubRouteResponse from(HubRoute hubRoute) {
        return GetHubRouteResponse.builder()
                .hubRouteId(hubRoute.getHubRouteId())
                .startHubId(hubRoute.getStartHub().getHubId())
                .destinationHubId(hubRoute.getDestinationHub().getHubId())
                .distance(hubRoute.getDistance())
                .duration(hubRoute.getDuration())
                .build();

    }

}
