package com.qb.companyservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyListResponse {

  private List<CompanyResponse> companies;
  private PageInfo pageInfo;
  private SearchInfo searchInfo;

  /**
   * Page 객체로부터 CompanyListResponse 생성
   */
  public static CompanyListResponse from(Page<CompanyResponse> page, CompanySearchRequest searchRequest) {
    return CompanyListResponse.builder()
        .companies(page.getContent())
        .pageInfo(PageInfo.from(page))
        .searchInfo(SearchInfo.from(searchRequest))
        .build();
  }

  /**
   * 페이징 정보
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PageInfo {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;

    public static PageInfo from(Page<?> page) {
      return PageInfo.builder()
          .currentPage(page.getNumber())
          .totalPages(page.getTotalPages())
          .totalElements(page.getTotalElements())
          .pageSize(page.getSize())
          .hasNext(page.hasNext())
          .hasPrevious(page.hasPrevious())
          .isFirst(page.isFirst())
          .isLast(page.isLast())
          .build();
    }
  }

  /**
   * 검색 정보
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SearchInfo {
    private String companyName;
    private String companyType;
    private String hubId;
    private String sortBy;
    private String sortDir;

    public static SearchInfo from(CompanySearchRequest request) {
      return SearchInfo.builder()
          .companyName(request.getCleanCompanyName())
          .companyType(request.hasCompanyType() ? request.getCompanyType().name() : null)
          .hubId(request.hasHubId() ? request.getHubId().toString() : null)
          .sortBy(request.getValidSortBy())
          .sortDir(request.getValidSortDir())
          .build();
    }
  }

  /**
   * 응답 요약 정보
   */
  public String getSummary() {
    return String.format("총 %d개 업체 (페이지 %d/%d)",
        pageInfo.getTotalElements(),
        pageInfo.getCurrentPage() + 1,
        pageInfo.getTotalPages());
  }

  /**
   * 빈 응답인지 확인
   */
  public boolean isEmpty() {
    return companies == null || companies.isEmpty();
  }

  /**
   * 검색 조건이 적용된 응답인지 확인
   */
  public boolean isSearchResult() {
    return searchInfo != null &&
        (searchInfo.getCompanyName() != null ||
            searchInfo.getCompanyType() != null ||
            searchInfo.getHubId() != null);
  }
}