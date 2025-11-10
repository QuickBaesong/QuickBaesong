package com.qb.companyservice.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test")
class CompanyServiceTest {

  @Test
  @DisplayName("임시 테스트")
  void temporaryTest() {
    // DTO와 Service 구현 후 실제 테스트 작성 예정
    System.out.println("Application Layer 구현 후 테스트 예정");
  }
}