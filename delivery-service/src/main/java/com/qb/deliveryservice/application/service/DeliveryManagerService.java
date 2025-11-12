package com.qb.deliveryservice.application.service;

import com.qb.deliveryservice.application.dto.DeliveryManagerCreateRequest;
import com.qb.deliveryservice.application.dto.DeliveryManagerResponse;
import com.qb.deliveryservice.domain.model.DeliveryManager;
import com.qb.deliveryservice.domain.model.ManagerType;
import com.qb.deliveryservice.domain.repository.DeliveryManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryManagerService {

    private final DeliveryManagerRepository managerRepository;

    // 배송 담당자 각 최대 10명까지만 등록 가능
    private static final int MAX_HUB_MANAGERS = 10;
    private static final int MAX_COMPANY_MANAGERS_PER_HUB = 10;

    @Transactional
    public DeliveryManagerResponse create(DeliveryManagerCreateRequest request) {
        // 중복 확인
        if (managerRepository.existsByUserIdAndNotDeleted(request.getUserId())) {
            throw new IllegalArgumentException("해당 사용자는 이미 배송 담당자로 등록되어 있습니다.");
        }

        // 탕비별 인원 제한 확인
        if (request.getType() == ManagerType.HUB) {
            validateHubManagerLimit();
        } else if (request.getType() == ManagerType.COMPANY) {
            validateCompanyManagerLimit(request.getHubId());
        }

        // 임시 slackid 생성, 실제로는 UserClient을 통해 User Service 에서 조회
        String mockSlackId = "U" + request.getUserId().toString().substring(0, 8);

        // 순번 자동 배정
        Integer nextSequence = getNextSequence(request.getType(), request.getHubId());

        // 허브 배송 담당자는 hubId null로 설정
        UUID hubId = request.getType() == ManagerType.HUB ? null : request.getHubId();

        // 배송 담당자 엔티티 생성
        DeliveryManager manager = request.toEntity(mockSlackId, nextSequence);

        // 저장
        DeliveryManager savedManager = managerRepository.save(manager);

        log.info("배송 담당자 생성 - managerId: {}, type: {}, sequence: {}",
                savedManager.getId(), savedManager.getManagerType(), savedManager.getSequence());

        return DeliveryManagerResponse.from(savedManager);
    }

    // 인원 확인
    private void validateHubManagerLimit() {
        Long currentCount = managerRepository.countHubManagers(ManagerType.HUB);
        if (currentCount >= MAX_HUB_MANAGERS) {
            throw new IllegalArgumentException(
                    String.format("허브 배송 담당자는 최대 %d명까지만 등록 가능합니다.", MAX_HUB_MANAGERS)
            );
        }
    }

    private void validateCompanyManagerLimit(UUID hubId) {
        if (hubId == null) {
            throw new IllegalArgumentException("업체 배송 담당자는 허브 ID가 필수입니다.");
        }

        Long currentCount = managerRepository.countCompanyManagersByHubId(hubId, ManagerType.COMPANY);
        if (currentCount >= MAX_COMPANY_MANAGERS_PER_HUB) {
            throw new IllegalArgumentException(
                    String.format("해당 허브의 업체 배송 담당자는 최대 %d명까지만 등록 가능합니다.",
                            MAX_COMPANY_MANAGERS_PER_HUB)
            );
        }
    }

    // 다음 순번 계산 (마지막 순번+1)
    private Integer getNextSequence(ManagerType type, UUID hubId) {
        if (type == ManagerType.HUB) {
            return managerRepository.findMaxSequenceForHubManagers(ManagerType.HUB) + 1;
        } else {
            return managerRepository.findMaxSequenceByHubId(hubId, ManagerType.COMPANY) + 1;
        }
    }
}