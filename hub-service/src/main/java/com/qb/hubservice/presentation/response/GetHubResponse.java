package com.qb.hubservice.presentation.response;

import com.qb.hubservice.domain.model.Hub;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class GetHubResponse {

    private UUID hubId;
    private String hubName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;


    public static GetHubResponse from(Hub hub) {
        return GetHubResponse.builder() // GetHubResponse 클래스의 빌더 사용
                .hubId(hub.getHubId())
                .hubName(hub.getHubName())
                .latitude(hub.getLocation().getLatitude())
                .longitude(hub.getLocation().getLongitude())
                .address(hub.getLocation().getAddress())
                .build();
    }

}
