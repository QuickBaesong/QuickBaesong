package com.qb.hubservice.application.service;

import com.qb.common.enums.ErrorCode;
import com.qb.common.exception.CustomException;
import com.qb.hubservice.domain.model.Hub;
import com.qb.hubservice.domain.model.HubRoute;
import com.qb.hubservice.domain.repository.HubRepository;
import com.qb.hubservice.domain.repository.HubRouteRepository;
import com.qb.hubservice.presentation.request.CreateHubRouteRequest;
import com.qb.hubservice.presentation.response.GetHubRouteResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubRouteService {

    private final HubRouteRepository hubRouteRepository;
    private final HubRepository hubRepository;

    public GetHubRouteResponse createHubRoute(CreateHubRouteRequest request) {
        // 허브 조회
        Hub hub = hubRepository.findById(request.getHubId()).orElse(null);
        Hub startHub = hubRepository.findById(request.getStartHubId()).orElse(null);
        Hub destinationHub = hubRepository.findById(request.getDestinationHubId()).orElse(null);

        // 중복 검사
        if (hubRouteRepository.existsByHub_HubIdAndStartHub_HubIdAndDestinationHub_HubId(
                request.getHubId(), request.getStartHubId(), request.getDestinationHubId())) {
            throw new IllegalArgumentException("동일한 경로의 허브 루트가 이미 존재합니다.");
        }

        // 엔티티 생성
        HubRoute hubRoute = HubRoute.of(hub, startHub, destinationHub,
                request.getDuration(),
                request.getDistance());

        // 저장
        HubRoute savedRoute = hubRouteRepository.save(hubRoute);
        log.info("HubRoute created - ID: {}, Hub: {}, Start: {}, Destination: {}",
                savedRoute.getHubRouteId(), hub.getHubId(), startHub.getHubId(), destinationHub.getHubId());

        return GetHubRouteResponse.fromEntity(savedRoute);
    }

}
