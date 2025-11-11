package com.qb.hubservice.application.service;

import com.qb.hubservice.domain.model.Hub;
import com.qb.hubservice.domain.repository.HubRepository;
import com.qb.hubservice.domain.vo.Location;
import com.qb.hubservice.presentation.request.CreateHubRequest;
import com.qb.hubservice.presentation.response.GetHubResponse;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.qb.hubservice.presentation.response.GetHubResponse.from;

@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepository hubRepository;

    @Transactional
    public GetHubResponse createHub(CreateHubRequest request) {

        Location location = request.toLocationVo();

        Hub hub = Hub.builder()
                .hubName(request.getHubName())
                .location(location)
                .build();

        Hub savedHub = hubRepository.save(hub);


        return GetHubResponse.from(savedHub);
    }

    public GetHubResponse getHub(UUID hubId) {

        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new NotFoundException("허브 아이디가 없습니다"));

        // 2. 조회된 엔티티를 응답 DTO로 변환하여 반환
        return GetHubResponse.from(hub);
    }
}
