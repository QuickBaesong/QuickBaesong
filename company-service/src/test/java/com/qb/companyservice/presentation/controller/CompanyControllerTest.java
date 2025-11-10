package com.qb.companyservice.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qb.companyservice.application.dto.CompanyCreateRequest;
import com.qb.companyservice.domain.entity.CompanyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestMvc
@TestPropertySource(properties = "spring.profiles.active=test")
class CompanyControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("POST /v1/company - 업체 생성 API 테스트")
  void createCompanyApi() throws Exception {
    // Given: 유효한 업체 생성 요청 데이터가 준비되어 있다
    CompanyCreateRequest request = CompanyCreateRequest.builder()
        .hubId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
        .companyName("태양 생산업체")
        .companyType(CompanyType.SENDER)
        .companyAddress("서울시 강남구 테헤란로 123")
        .build();

    // When & Then: API 요청을 보내고 201 상태코드가 반환된다
    mockMvc.perform(post("/v1/company")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated()); // 현재는 404 에러 발생할 예정
  }

  @Test
  @DisplayName("GET /v1/company/{companyId} - 업체 단건 조회 API 테스트")
  void getCompanyByIdApi() throws Exception {
    // Given: 조회할 업체 ID가 준비되어 있다
    UUID companyId = UUID.fromString("87654321-f6e5-4321-abcd-ef0123456789");

    // When & Then: API 요청을 보내고 200 상태코드가 반환된다
    mockMvc.perform(get("/v1/company/{companyId}", companyId))
        .andExpect(status().isOk()); // 현재는 404 에러 발생할 예정
  }

  @Test
  @DisplayName("GET /v1/company - 업체 목록 조회 API 테스트")
  void getCompaniesApi() throws Exception {
    // Given: 페이징 파라미터가 준비되어 있다

    // When & Then: API 요청을 보내고 200 상태코드가 반환된다
    mockMvc.perform(get("/v1/company")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk()); // 현재는 404 에러 발생할 예정
  }
}