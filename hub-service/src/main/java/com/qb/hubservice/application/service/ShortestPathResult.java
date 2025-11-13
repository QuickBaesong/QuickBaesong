package com.qb.hubservice.application.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShortestPathResult {
    private final boolean found;           // 경로 찾기 성공 여부
    private final List<UUID> shortestPath; // 최단 경로의 허브 ID 순서 목록
    private final BigDecimal totalDistance; // 총 최단 거리
    private final Long totalDuration;       // 총 소요 시간

    public static ShortestPathResult notFound() {
        return new ShortestPathResult(false, Collections.emptyList(), BigDecimal.ZERO, 0L);
    }

    public static ShortestPathResult success(List<UUID> path, BigDecimal distance, Long totalDuration) {
        return new ShortestPathResult(true, path, distance,totalDuration);
    }
}
