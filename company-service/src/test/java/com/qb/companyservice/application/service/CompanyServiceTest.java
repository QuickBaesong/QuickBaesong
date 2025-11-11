package com.qb.companyservice.application.service;

import com.qb.companyservice.application.dto.CompanyCreateRequest;
import com.qb.companyservice.application.dto.CompanyResponse;
import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import com.qb.companyservice.domain.repository.CompanyRepository;
import com.qb.common.response.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test")
@Transactional
class CompanyServiceTest {

  @Autowired
  private CompanyService companyService;

  @Autowired
  private CompanyRepository companyRepository;

  @Test
  @DisplayName("마스터가 새로운 SENDER 업체를 생성한다")
  void createSenderCompany() {
    // Given: 유효한 허브 ID와 업체 생성 요청 데이터가 준비되어 있다
    UUID hubId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    String userId = "user1234-e89b-12d3-a456-426614174001";

    CompanyCreateRequest request = CompanyCreateRequest.builder()
        .hubId(hubId)
        .companyName("태양 생산업체")
        .companyType(CompanyType.SENDER)
        .companyAddress("서울시 강남구 테헤란로 123")
        .build();

    // When: 업체 생성 요청을 처리한다
    CompanyResponse response = companyService.createCompany(request, userId);

    // Then: 업체가 성공적으로 생성되고 올바른 정보가 반환된다
    assertThat(response.getCompanyId()).isNotNull();
    assertThat(response.getCompanyName()).isEqualTo("태양 생산업체");
    assertThat(response.getCompanyType()).isEqualTo(CompanyType.SENDER);
    assertThat(response.getHubId()).isEqualTo(hubId);
    assertThat(response.getCompanyAddress()).isEqualTo("서울시 강남구 테헤란로 123");
    assertThat(response.getCreatedAt()).isNotNull();

    // 데이터베이스에 실제로 저장되었는지 확인
    Company savedCompany = companyRepository.findByCompanyIdAndDeletedAtIsNull(response.getCompanyId())
        .orElse(null);
    assertThat(savedCompany).isNotNull();
    assertThat(savedCompany.getCompanyName()).isEqualTo("태양 생산업체");
  }

  @Test
  @DisplayName("존재하는 업체를 ID로 조회한다")
  void findCompanyById() {
    // Given: 업체가 이미 존재한다
    Company company = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(UUID.randomUUID())
        .companyName("테스트 업체")
        .companyType(CompanyType.RECEIVER)
        .companyAddress("부산시 해운대구")
        .build();
    Company savedCompany = companyRepository.save(company);

    // When: 업체 ID로 조회 요청을 한다
    CompanyResponse response = companyService.findById(savedCompany.getCompanyId());

    // Then: 해당 업체 정보가 반환된다
    assertThat(response.getCompanyId()).isEqualTo(savedCompany.getCompanyId());
    assertThat(response.getCompanyName()).isEqualTo("테스트 업체");
    assertThat(response.getCompanyType()).isEqualTo(CompanyType.RECEIVER);
    assertThat(response.getCompanyAddress()).isEqualTo("부산시 해운대구");
  }

  @Test
  @DisplayName("업체 목록을 페이징으로 조회한다")
  void findCompaniesWithPaging() {
    // Given: 여러 업체가 존재한다
    UUID hubId = UUID.randomUUID();

    Company company1 = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(hubId)
        .companyName("첫번째 업체")
        .companyType(CompanyType.SENDER)
        .companyAddress("서울시 강남구")
        .build();

    Company company2 = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(hubId)
        .companyName("두번째 업체")
        .companyType(CompanyType.RECEIVER)
        .companyAddress("부산시 해운대구")
        .build();

    companyRepository.save(company1);
    companyRepository.save(company2);

    // When: 페이징 조회 요청을 한다
    PageResponse<CompanyResponse> response = companyService.findCompanies(
        null, null, null, "companyName", 0, 10);

    // Then: 페이징된 업체 목록이 반환된다
    assertThat(response.getContents()).isNotNull();
    assertThat(response.getContents().size()).isGreaterThanOrEqualTo(2);
    assertThat(response.getTotalElements()).isGreaterThanOrEqualTo(2);
    assertThat(response.getPageNumber()).isEqualTo(0);
    assertThat(response.getPageSize()).isEqualTo(10);

    // 정렬 확인 (companyName으로 정렬)
    assertThat(response.getContents().get(0).getCompanyName()).isLessThan(
        response.getContents().get(1).getCompanyName());
  }
}