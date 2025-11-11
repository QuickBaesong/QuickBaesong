package com.qb.hubservice.presentation.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

public class GetHubResponse {

    @Getter
    @Builder
    public static class HubInfo {
        private UUID hubId;
        private String hubName;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String address;
    }

    public static HubInfo of(HubInfo hubInfo) {
        return HubInfo.builder()
                .hubId(hubInfo.getHubId())
                .hubName(hubInfo.getHubName())
                .latitude(hubInfo.getLatitude())
                .longitude(hubInfo.getLongitude())
                .address(hubInfo.getAddress())
                .build();
    }
}
