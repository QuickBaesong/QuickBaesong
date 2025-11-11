package com.qb.hubservice.presentation.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.qb.hubservice.domain.vo.Location;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
public class CreateHubRequest {

    @NotBlank(message = "허브 이름은 필수입니다.")
    @Size(max = 255, message = "허브 이름은 255자 이하이어야 합니다.")
    private String hubName;

    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 255, message = "주소는 255자 이하이어야 합니다.")
    private String address;

    @NotNull(message = "위도(latitude)는 필수입니다.")
    @DecimalMin(value = "-90.0", message = "위도는 -90.0 이상이어야 합니다.")
    @DecimalMax(value = "90.0", message = "위도는 90.0 이하이어야 합니다.")
    private BigDecimal latitude;

    @NotNull(message = "경도(longitude)는 필수입니다.")
    @DecimalMin(value = "-180.0", message = "경도는 -180.0 이상이어야 합니다.")
    @DecimalMax(value = "180.0", message = "경도는 180.0 이하이어야 합니다.")
    private BigDecimal longitude;

    @Builder
    public CreateHubRequest(String hubName, String address, BigDecimal latitude, BigDecimal longitude) {
        this.hubName = hubName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location toLocationVo() {
        return Location.builder()
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }

}