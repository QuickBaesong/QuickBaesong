package com.qb.hubservice.presentation.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateHubRouteRequest {

    @NotNull(message = "기준 허브 ID는 필수입니다.")
    private UUID hubId;

    // 출발 허브 ID (필수)
    @NotNull(message = "출발 허브 ID는 필수입니다.")
    private UUID startHubId;

    // 도착 허브 ID (필수)
    @NotNull(message = "도착 허브 ID는 필수입니다.")
    private UUID destinationHubId;

    // 경로 거리 (km, 필수)
    @NotNull(message = "거리는 필수 입력 항목입니다.")
    @Min(value = 0, message = "거리는 0 이상이어야 합니다.")
    private BigDecimal distance;

    // 예상 소요 시간 (분, 필수)
    @NotNull(message = "소요 시간은 필수 입력 항목입니다.")
    @Min(value = 1, message = "소요 시간은 1분 이상이어야 합니다.")
    private Long duration;

    public UUID getSourceHubId() {
        return this.startHubId;
    }

    public UUID getDestinationHubId() {
        return this.destinationHubId;
    }

    public UUID getHubId() {
        return this.hubId;
    }
}
