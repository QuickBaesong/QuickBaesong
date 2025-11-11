package com.qb.hubservice.application.service;

import com.qb.hubservice.domain.model.Hub;
import com.qb.hubservice.domain.repository.HubRepository;
import com.qb.hubservice.presentation.request.CreateHubRequest;
import com.qb.hubservice.presentation.response.GetHubResponse;
import org.springframework.transaction.annotation.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubService {
    private final HubRepository hubRepository;

    @Transactional
    public GetHubResponse createHub(CreateHubRequest request) {

        Hub hub = request.toHub();

        Hub savedHub = hubRepository.save(hub);

        return GetHubResponse.from(savedHub);
    }

    public GetHubResponse getHub(UUID hubId) {

        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new NotFoundException("해당하는 허브 정보가 없습니다."));
        return GetHubResponse.from(hub);
    }


}
