package com.qb.companyservice.application.service;

import com.qb.companyservice.application.dto.CompanyCreateRequest;
import com.qb.companyservice.application.dto.CompanyResponse;
import com.qb.companyservice.domain.entity.CompanyType;
import com.qb.common.response.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test")
class CompanyServiceTest {

  // @Autowired
  // private CompanyService companyService; // 나중에 주입 예정

  @Test
  @DisplayName("마스터가 새로운 SENDER 업체를 생성한다")
  void createSenderCompany() {
    // Given: 유효한 허브 ID와 업체 생성 요청 데이터가 준비되어 있다
    UUID hubId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    String userId = "user1234"; // 현재는 String으로 처리

    CompanyCreateRequest request = CompanyCreateRequest.builder()
        .hubId(hubId)
        .companyName("태양 생산업체")
        .companyType(CompanyType.SENDER)
        .companyAddress("서울시 강남구 테헤란로 123")
        .build();

    // When: 업체 생성 요청을 처리한다
    // CompanyResponse response = companyService.createCompany(request, userId);

    // Then: 업체가 성공적으로 생성되고 올바른 정보가 반환된다
    // assertThat(response.getCompanyId()).isNotNull();
    // assertThat(response.getCompanyName()).isEqualTo("태양 생산업체");
    // assertThat(response.getCompanyType()).isEqualTo(CompanyType.SENDER);
    // assertThat(response.getHubId()).isEqualTo(hubId);
    // assertThat(response.getCompanyAddress()).isEqualTo("서울시 강남구 테헤란로 123");

    // 현재는 구현되지 않았으므로 테스트 실패
    throw new RuntimeException("CompanyService가 아직 구현되지 않았습니다.");
  }

  @Test
  @DisplayName("존재하는 업체를 ID로 조회한다")
  void findCompanyById() {
    // Given: 업체가 이미 존재한다
    UUID companyId = UUID.fromString("87654321-f6e5-4321-abcd-ef0123456789");

    // When: 업체 ID로 조회 요청을 한다
    // CompanyResponse response = companyService.findById(companyId);

    // Then: 해당 업체 정보가 반환된다
    // assertThat(response.getCompanyId()).isEqualTo(companyId);
    // assertThat(response.getCompanyName()).isNotNull();

    // 현재는 구현되지 않았으므로 테스트 실패
    throw new RuntimeException("CompanyService가 아직 구현되지 않았습니다.");
  }

  @Test
  @DisplayName("업체 목록을 페이징으로 조회한다")
  void findCompaniesWithPaging() {
    // Given: 여러 업체가 존재하고 검색 조건이 준비되어 있다
    String nameFilter = null; // 전체 조회
    CompanyType typeFilter = null; // 전체 조회
    UUID hubIdFilter = null; // 전체 조회
    int page = 0;
    int size = 10;

    // When: 페이징 조회 요청을 한다
    // PageResponse<CompanyResponse> response = companyService.findCompanies(
    //     nameFilter, typeFilter, hubIdFilter, page, size);

    // Then: 페이징된 업체 목록이 반환된다
    // assertThat(response.getContent()).isNotNull();
    // assertThat(response.getContent().size()).isLessThanOrEqualTo(size);

    // 현재는 구현되지 않았으므로 테스트 실패
    throw new RuntimeException("CompanyService가 아직 구현되지 않았습니다.");
  }
}