package com.qb.hubservice.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortestRouteResponse {

    private BigDecimal totalDistance;


    private Long totalDuration;


    private List<RouteSegment> segments;

    //내부 클래스 -
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteSegment {

        private UUID startHubId;


        private UUID destinationHubId;


        private BigDecimal distance;


        private Long duration;
    }

}
