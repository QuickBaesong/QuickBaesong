package com.qb.companyservice.domain.repository;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = "spring.profiles.active=test")
class CompanyRepositoryTest {

  @Autowired
  private CompanyRepository companyRepository;

  @Test
  @DisplayName("업체를 저장하고 조회할 수 있다")
  void saveAndFindCompany() {
    // Given: 새로운 업체 엔티티가 준비되어 있다
    UUID hubId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    Company company = Company.builder()
        .userId(userId)
        .hubId(hubId)
        .companyName("테스트 업체")
        .companyType(CompanyType.SENDER)
        .companyAddress("테스트 주소")
        .build();

    // When: 업체를 저장하고 조회한다
    Company savedCompany = companyRepository.save(company);
    Optional<Company> foundCompany = companyRepository.findByCompanyIdAndDeletedAtIsNull(savedCompany.getCompanyId());

    // Then: 저장된 업체가 조회된다
    assertThat(foundCompany).isPresent();
    assertThat(foundCompany.get().getCompanyName()).isEqualTo("테스트 업체");
    assertThat(foundCompany.get().getCompanyType()).isEqualTo(CompanyType.SENDER);
    assertThat(foundCompany.get().getHubId()).isEqualTo(hubId);
  }

  @Test
  @DisplayName("허브별 업체 목록을 조회할 수 있다")
  void findByHubId() {
    // Given: 같은 허브에 속한 여러 업체가 존재한다
    UUID hubId = UUID.randomUUID();

    Company company1 = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(hubId)
        .companyName("업체1")
        .companyType(CompanyType.SENDER)
        .companyAddress("주소1")
        .build();

    Company company2 = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(hubId)
        .companyName("업체2")
        .companyType(CompanyType.RECEIVER)
        .companyAddress("주소2")
        .build();

    companyRepository.save(company1);
    companyRepository.save(company2);

    // When: 허브 ID로 업체 목록을 조회한다
    List<Company> companies = companyRepository.findByHubIdAndDeletedAtIsNull(hubId);

    // Then: 해당 허브의 업체들이 조회된다
    assertThat(companies).hasSize(2);
    assertThat(companies).extracting(Company::getCompanyName)
        .containsExactlyInAnyOrder("업체1", "업체2");
  }

  @Test
  @DisplayName("업체 타입별로 조회할 수 있다")
  void findByCompanyType() {
    // Given: 다른 타입의 업체들이 존재한다
    Company senderCompany = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(UUID.randomUUID())
        .companyName("생산업체")
        .companyType(CompanyType.SENDER)
        .companyAddress("생산업체 주소")
        .build();

    Company receiverCompany = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(UUID.randomUUID())
        .companyName("수령업체")
        .companyType(CompanyType.RECEIVER)
        .companyAddress("수령업체 주소")
        .build();

    companyRepository.save(senderCompany);
    companyRepository.save(receiverCompany);

    // When: SENDER 타입으로 조회한다
    List<Company> senderCompanies = companyRepository.findByCompanyTypeAndDeletedAtIsNull(CompanyType.SENDER);

    // Then: SENDER 타입의 업체만 조회된다
    assertThat(senderCompanies).hasSize(1);
    assertThat(senderCompanies.get(0).getCompanyName()).isEqualTo("생산업체");
    assertThat(senderCompanies.get(0).isSender()).isTrue();
  }

  @Test
  @DisplayName("복합 조건으로 업체를 검색할 수 있다")
  void findCompaniesWithFilters() {
    // Given: 다양한 조건의 업체들이 존재한다
    UUID hubId = UUID.randomUUID();

    Company company1 = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(hubId)
        .companyName("태양 생산업체")
        .companyType(CompanyType.SENDER)
        .companyAddress("서울시 강남구")
        .build();

    Company company2 = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(hubId)
        .companyName("달빛 수령업체")
        .companyType(CompanyType.RECEIVER)
        .companyAddress("부산시 해운대구")
        .build();

    companyRepository.save(company1);
    companyRepository.save(company2);

    // When: 업체명에 "태양"이 포함된 업체를 검색한다
    Pageable pageable = PageRequest.of(0, 10);
    Page<Company> result = companyRepository.findCompaniesWithFilters("태양", null, null, pageable);

    // Then: 조건에 맞는 업체가 조회된다
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).getCompanyName()).contains("태양");
  }

  @Test
  @DisplayName("업체명 중복을 체크할 수 있다")
  void checkDuplicateCompanyName() {
    // Given: 특정 허브에 업체가 존재한다
    UUID hubId = UUID.randomUUID();

    Company existingCompany = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(hubId)
        .companyName("중복체크업체")
        .companyType(CompanyType.SENDER)
        .companyAddress("기존 주소")
        .build();

    companyRepository.save(existingCompany);

    // When: 같은 허브에서 같은 이름으로 중복 체크를 한다
    boolean isDuplicate = companyRepository.existsByHubIdAndCompanyNameAndDeletedAtIsNull(hubId, "중복체크업체");
    boolean isNotDuplicate = companyRepository.existsByHubIdAndCompanyNameAndDeletedAtIsNull(hubId, "다른업체명");

    // Then: 중복 여부가 정확히 반환된다
    assertThat(isDuplicate).isTrue();
    assertThat(isNotDuplicate).isFalse();
  }
}