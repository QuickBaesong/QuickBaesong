package com.qb.hubservice.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Location {

    @Column(name = "hub_address", nullable = false, length = 255)
    private String address;

    @Column(name = "hub_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "hub_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;


    @Builder
    public Location(String address, BigDecimal latitude, BigDecimal longitude) {
        validate(address, latitude, longitude); // 생성 시 유효성 검사 실행
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    private void validate(String address, BigDecimal latitude, BigDecimal longitude) {

        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("주소는 필수 입력 항목입니다.");
        }
        if (address.length() > 255) {
            throw new IllegalArgumentException("주소는 255자 이하이어야 합니다.");
        }

        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("위도와 경도는 필수 입력 항목입니다.");
        }

        if (latitude.compareTo(BigDecimal.valueOf(-90.0)) < 0 || latitude.compareTo(BigDecimal.valueOf(90.0)) > 0) {
            throw new IllegalArgumentException("위도는 -90.0 이상 90.0 이하이어야 합니다.");
        }

        if (longitude.compareTo(BigDecimal.valueOf(-180.0)) < 0 || longitude.compareTo(BigDecimal.valueOf(180.0)) > 0) {
            throw new IllegalArgumentException("경도는 -180.0 이상 180.0 이하이어야 합니다.");
        }
    }





}
