package com.qb.companyservice.domain.repository;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = "spring.profiles.active=test")
class CompanyRepositoryTest {

  // @Autowired
  // private CompanyRepository companyRepository; // 나중에 주입 예정

  @Test
  @DisplayName("업체를 저장하고 조회할 수 있다")
  void saveAndFindCompany() {
    // Given: 새로운 업체 엔티티가 준비되어 있다
    // Company company = Company.builder()
    //         .userId(UUID.randomUUID())
    //         .hubId(UUID.randomUUID())
    //         .companyName("테스트 업체")
    //         .companyType(CompanyType.SENDER)
    //         .companyAddress("테스트 주소")
    //         .build();

    // When: 업체를 저장하고 조회한다
    // Company savedCompany = companyRepository.save(company);
    // Optional<Company> foundCompany = companyRepository.findById(savedCompany.getCompanyId());

    // Then: 저장된 업체가 조회된다
    // assertThat(foundCompany).isPresent();
    // assertThat(foundCompany.get().getCompanyName()).isEqualTo("테스트 업체");

    // 현재는 구현되지 않았으므로 테스트 실패
    throw new RuntimeException("Company Entity와 Repository가 아직 구현되지 않았습니다.");
  }
}