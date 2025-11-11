package com.qb.companyservice.domain.repository;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

  /**
   * 업체 ID로 삭제되지 않은 업체 조회
   * @param companyId 업체 ID
   * @return 업체 정보
   */
  Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId);

  /**
   * 삭제되지 않은 모든 업체 조회 (페이징)
   * @param pageable 페이징 정보
   * @return 업체 목록
   */
  Page<Company> findByDeletedAtIsNull(Pageable pageable);

  /**
   * 업체명으로 검색 (대소문자 무시, 부분 일치)
   * @param name 업체명
   * @param pageable 페이징 정보
   * @return 업체 목록
   */
  Page<Company> findByDeletedAtIsNullAndCompanyNameContainingIgnoreCase(String name, Pageable pageable);

  /**
   * 업체 타입으로 검색
   * @param type 업체 타입
   * @param pageable 페이징 정보
   * @return 업체 목록
   */
  Page<Company> findByDeletedAtIsNullAndCompanyType(CompanyType type, Pageable pageable);

  /**
   * 허브 ID로 검색
   * @param hubId 허브 ID
   * @param pageable 페이징 정보
   * @return 업체 목록
   */
  Page<Company> findByDeletedAtIsNullAndHubId(UUID hubId, Pageable pageable);

  /**
   * 복합 조건 검색 (동적 쿼리)
   * @param name 업체명 (선택)
   * @param type 업체 타입 (선택)
   * @param hubId 허브 ID (선택)
   * @param pageable 페이징 정보
   * @return 업체 목록
   */
  @Query("SELECT c FROM Company c WHERE " +
      "(:name IS NULL OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
      "(:type IS NULL OR c.companyType = :type) AND " +
      "(:hubId IS NULL OR c.hubId = :hubId) AND " +
      "c.deletedAt IS NULL")
  Page<Company> findCompaniesWithFilters(@Param("name") String name,
      @Param("type") CompanyType type,
      @Param("hubId") UUID hubId,
      Pageable pageable);
}