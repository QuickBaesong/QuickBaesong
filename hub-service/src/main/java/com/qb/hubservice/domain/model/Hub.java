package com.qb.hubservice.domain.model;

import com.qb.common.entity.BaseEntity;
import java.util.UUID;

import com.qb.hubservice.domain.vo.Location;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "p_hub")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id", columnDefinition = "UUID")
    private UUID hubId;

    @Column(name = "hub_name", nullable = false)
    private String hubName;

    @Embedded
    private Location location; // location 필드 하나로 위도, 경도, 주소 관리

    @Builder
    public Hub(String hubName, Location location) {
        this.hubName = hubName;
        this.location = location;
    }

}