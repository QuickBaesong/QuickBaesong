package com.qb.companyservice.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qb.companyservice.application.dto.CompanyCreateRequest;
import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import com.qb.companyservice.domain.repository.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CompanyController 통합 테스트
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.profiles.active=test")
class CompanyControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CompanyRepository companyRepository;

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

    String url = "http://localhost:" + port + "/v1/company";

    // When: API 요청을 보낸다
    ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

    // Then: 201 상태코드가 반환된다
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).contains("태양 생산업체");
    assertThat(response.getBody()).contains("SENDER");
    assertThat(response.getBody()).contains("CREATED");
  }

  @Test
  @DisplayName("GET /v1/company/{companyId} - 업체 단건 조회 API 테스트")
  void getCompanyByIdApi() throws Exception {
    // Given: 조회할 업체가 데이터베이스에 존재한다
    Company company = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(UUID.fromString("456e7890-e89b-12d3-a456-426614174001"))
        .companyName("달빛 수령업체")
        .companyType(CompanyType.RECEIVER)
        .companyAddress("부산시 해운대구 해운대로 456")
        .build();

    Company savedCompany = companyRepository.saveAndFlush(company);
    UUID companyId = savedCompany.getCompanyId();

    String url = "http://localhost:" + port + "/v1/company/" + companyId;

    // When: API 요청을 보낸다
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    // Then: 200 상태코드가 반환된다
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("달빛 수령업체");
    assertThat(response.getBody()).contains("RECEIVER");
    assertThat(response.getBody()).contains(companyId.toString());
  }

  @Test
  @DisplayName("GET /v1/company - 업체 목록 조회 API 테스트")
  void getCompaniesApi() throws Exception {
    // Given: 여러 업체가 데이터베이스에 존재한다
    UUID hubId = UUID.fromString("789e0123-e89b-12d3-a456-426614174002");

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

    companyRepository.saveAndFlush(company1);
    companyRepository.saveAndFlush(company2);

    String url = "http://localhost:" + port + "/v1/company?page=0&size=10&sortBy=companyName";

    // When: API 요청을 보낸다
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    // Then: 200 상태코드가 반환된다
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("첫번째 업체");
    assertThat(response.getBody()).contains("두번째 업체");
  }

  @Test
  @DisplayName("GET /v1/company - 업체명으로 검색 API 테스트")
  void searchCompaniesByNameApi() throws Exception {
    // Given: 검색 대상 업체가 존재한다
    Company company = Company.builder()
        .userId(UUID.randomUUID())
        .hubId(UUID.randomUUID())
        .companyName("태양광발전소")
        .companyType(CompanyType.SENDER)
        .companyAddress("제주도 제주시")
        .build();

    companyRepository.saveAndFlush(company);

    String url = "http://localhost:" + port + "/v1/company?name=태양&page=0&size=10";

    // When: 업체명 검색 API를 호출한다
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    // Then: 검색 결과가 반환된다
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("태양광발전소");
  }

  @Test
  @DisplayName("GET /v1/company - 존재하지 않는 업체 조회 시 404 에러")
  void getCompanyNotFound() throws Exception {
    // Given: 존재하지 않는 업체 ID
    UUID nonExistentCompanyId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    String url = "http://localhost:" + port + "/v1/company/" + nonExistentCompanyId;

    // When: API 요청을 보낸다
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    // Then: 404 에러가 반환된다
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}