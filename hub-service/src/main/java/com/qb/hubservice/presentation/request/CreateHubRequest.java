package com.qb.hubservice.presentation.request;

import com.qb.hubservice.domain.model.Hub;
import com.qb.hubservice.domain.vo.Location;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
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


    public Location toLocationVo() {
        return Location.builder()
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }

    public Hub toHub() {
        return Hub.builder()
                .hubName(this.hubName)
                .location(this.toLocationVo()) // Location VO 포함
                .build();
    }
}