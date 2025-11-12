package com.qb.hubservice.application.service;

import com.qb.common.enums.ErrorCode;
import com.qb.common.exception.CustomException;
import com.qb.hubservice.domain.model.Hub;
import com.qb.hubservice.domain.model.HubRoute;
import com.qb.hubservice.domain.repository.HubRepository;
import com.qb.hubservice.domain.repository.HubRouteRepository;
import com.qb.hubservice.exception.HubCustomException;
import com.qb.hubservice.exception.HubErrorCode;
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

    @Transactional
    public GetHubRouteResponse createHubRoute(CreateHubRouteRequest request) {
        // 1. 필수 허브 조회 및 존재 여부 검사 (NullPointerException 방지)
        Hub hub = hubRepository.findById(request.getHubId())
                .orElseThrow(() -> new HubCustomException(HubErrorCode.INVALID_HUB_ID, "hubId: 기존 허브 ID를 찾을 수 없습니다."));

        Hub startHub = hubRepository.findById(request.getStartHubId())
                .orElseThrow(() -> new HubCustomException(HubErrorCode.INVALID_START_HUB_ID, "startHubId: 시작 허브 ID를 찾을 수 없습니다."));

        Hub destinationHub = hubRepository.findById(request.getDestinationHubId())
                .orElseThrow(() -> new HubCustomException(HubErrorCode.INVALID_DESTINATION_HUB_ID, "destinationHubId: 도착 허브 ID를 찾을 수 없습니다."));

        // 2. 중복 검사 및 예외 처리
        if (hubRouteRepository.existsByHub_HubIdAndStartHub_HubIdAndDestinationHub_HubId(
                request.getHubId(), request.getStartHubId(), request.getDestinationHubId())) {
            // 중복 리소스에 대한 구체적인 에러 코드 사용 권장 (예시: DUPLICATE_RESOURCE)
            throw new HubCustomException(HubErrorCode.DUPLICATE_HUB_ROUTE, "동일한 경로의 허브 루트가 이미 존재합니다.");
        }

        // 3. 엔티티 생성
        HubRoute hubRoute = HubRoute.of(hub, startHub, destinationHub,
                request.getDuration(),
                request.getDistance());

        // 4. 저장 및 로그
        HubRoute savedRoute = hubRouteRepository.save(hubRoute);
        log.info("HubRoute created - ID: {}, Hub: {}, Start: {}, Destination: {}",
                savedRoute.getHubRouteId(), hub.getHubId(), startHub.getHubId(), destinationHub.getHubId());

        return GetHubRouteResponse.from(savedRoute);
    }

}
