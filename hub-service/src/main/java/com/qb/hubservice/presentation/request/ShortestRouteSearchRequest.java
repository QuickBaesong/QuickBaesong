package com.qb.hubservice.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShortestRouteSearchRequest {

    @NotNull(message = "출발 허브 ID는 필수입니다.")
    private UUID startHubId;


    @NotNull(message = "도착 허브 ID는 필수입니다.")
    private UUID endHubId;


}
