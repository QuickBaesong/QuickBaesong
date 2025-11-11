package com.qb.hubservice.application.service;

import com.qb.hubservice.domain.model.Hub;
import com.qb.hubservice.domain.repository.HubRepository;
import com.qb.hubservice.domain.vo.Location;
import com.qb.hubservice.presentation.request.CreateHubRequest;
import com.qb.hubservice.presentation.response.GetHubResponse;
import org.springframework.transaction.annotation.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        if (hub.getDeletedAt() != null) {
            throw new NotFoundException("요청하신 허브(ID: " + hubId + ")는 이미 삭제된 상태입니다.");
        }
        return GetHubResponse.from(hub);
    }

    @Transactional // 저장 작업이므로 @Transactional 필요
    public List<GetHubResponse> createAllDummyHubs() {

        // 1. 전국 17개 시/도 허브 데이터를 리스트로 정의
        List<Hub> dummyHubs = List.of(
                // 서울
                Hub.builder().hubName("서울특별센터")
                        .location(Location.builder().address("서울특별시 송파구 송파대로 55")
                                .latitude(BigDecimal.valueOf(37.5029)).longitude(BigDecimal.valueOf(127.1082)).build()).build(),
                // 경기 북부
                Hub.builder().hubName("경기 북부 센터")
                        .location(Location.builder().address("경기도 고양시 덕양구 권율대로 570")
                                .latitude(BigDecimal.valueOf(37.6499)).longitude(BigDecimal.valueOf(126.8361)).build()).build(),
                // 경기 남부
                Hub.builder().hubName("경기 남부 센터")
                        .location(Location.builder().address("경기도 이천시 덕평로 257-21")
                                .latitude(BigDecimal.valueOf(37.1994)).longitude(BigDecimal.valueOf(127.4277)).build()).build(),
                // 부산
                Hub.builder().hubName("부산광역시 센터")
                        .location(Location.builder().address("부산 동구 중앙대로 206")
                                .latitude(BigDecimal.valueOf(35.1384)).longitude(BigDecimal.valueOf(129.0435)).build()).build(),
                // 대구
                Hub.builder().hubName("대구광역시 센터")
                        .location(Location.builder().address("대구 북구 태평로 161")
                                .latitude(BigDecimal.valueOf(35.8756)).longitude(BigDecimal.valueOf(128.5833)).build()).build(),
                // 인천
                Hub.builder().hubName("인천광역시 센터")
                        .location(Location.builder().address("인천 남동구 장자로 29")
                                .latitude(BigDecimal.valueOf(37.4497)).longitude(BigDecimal.valueOf(126.7021)).build()).build(),
                // 광주
                Hub.builder().hubName("광주광역시 센터")
                        .location(Location.builder().address("광주 북구 서구 빛고을로 111")
                                .latitude(BigDecimal.valueOf(35.1587)).longitude(BigDecimal.valueOf(126.8526)).build()).build(),
                // 대전
                Hub.builder().hubName("대전광역시 센터")
                        .location(Location.builder().address("대전 서구 둔산로 100")
                                .latitude(BigDecimal.valueOf(36.3533)).longitude(BigDecimal.valueOf(127.3879)).build()).build(),
                // 울산
                Hub.builder().hubName("울산광역시 센터")
                        .location(Location.builder().address("울산 남구 중앙로 201")
                                .latitude(BigDecimal.valueOf(35.5388)).longitude(BigDecimal.valueOf(129.3113)).build()).build(),
                // 세종
                Hub.builder().hubName("세종특별자치시 센터")
                        .location(Location.builder().address("세종특별자치시 한누리대로 2130")
                                .latitude(BigDecimal.valueOf(36.4800)).longitude(BigDecimal.valueOf(127.2882)).build()).build(),
                // 강원
                Hub.builder().hubName("강원특별자치도 센터")
                        .location(Location.builder().address("강원특별자치도 춘천시 중앙로 1")
                                .latitude(BigDecimal.valueOf(37.8812)).longitude(BigDecimal.valueOf(127.7291)).build()).build(),
                // 충북
                Hub.builder().hubName("충청북도 센터")
                        .location(Location.builder().address("충북 청주시 상당구 상당로 82")
                                .latitude(BigDecimal.valueOf(36.6433)).longitude(BigDecimal.valueOf(127.4913)).build()).build(),
                // 충남
                Hub.builder().hubName("충청남도 센터")
                        .location(Location.builder().address("충남 홍성군 홍북읍 충남대로 21")
                                .latitude(BigDecimal.valueOf(36.6027)).longitude(BigDecimal.valueOf(126.6800)).build()).build(),
                // 전북
                Hub.builder().hubName("전북특별자치도 센터")
                        .location(Location.builder().address("전북특별자치도 전주시 완산구 효자도로 225")
                                .latitude(BigDecimal.valueOf(35.8174)).longitude(BigDecimal.valueOf(127.1088)).build()).build(),
                // 전남
                Hub.builder().hubName("전라남도 센터")
                        .location(Location.builder().address("전남 무안군 삼향읍 오룡1길 1")
                                .latitude(BigDecimal.valueOf(34.8193)).longitude(BigDecimal.valueOf(126.4674)).build()).build(),
                // 경북
                Hub.builder().hubName("경상북도 센터")
                        .location(Location.builder().address("경북 안동시 동천면 도청대로 455")
                                .latitude(BigDecimal.valueOf(36.5772)).longitude(BigDecimal.valueOf(128.5908)).build()).build(),
                // 경남
                Hub.builder().hubName("경상남도 센터")
                        .location(Location.builder().address("경남 창원시 의창구 중앙대로 300")
                                .latitude(BigDecimal.valueOf(35.2343)).longitude(BigDecimal.valueOf(128.6946)).build()).build()
        );

        // 2. JPA의 saveAll을 사용하여 리스트를 한 번에 저장
        List<Hub> savedHubs = hubRepository.saveAll(dummyHubs);

        // 3. 저장된 엔티티 리스트를 DTO 리스트로 변환하여 반환
        return savedHubs.stream()
                .map(GetHubResponse::from)
                .collect(Collectors.toList());
    }


}
