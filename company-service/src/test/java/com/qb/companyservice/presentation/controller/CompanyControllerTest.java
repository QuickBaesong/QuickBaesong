package com.qb.companyservice.presentation.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.profiles.active=test")
class CompanyControllerTest {

  @Test
  @DisplayName("임시 테스트")
  void temporaryTest() {
    // Controller 구현 후 실제 테스트 작성 예정
    System.out.println("Presentation Layer 구현 후 테스트 예정");
  }
}