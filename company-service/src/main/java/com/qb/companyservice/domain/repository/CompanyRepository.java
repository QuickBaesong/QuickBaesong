package com.qb.companyservice.domain.repository;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 업체 Repository
 * 업체 데이터 접근을 담당합니다.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

  /**
   * 삭제되지 않은 업체를 ID로 조회
   */
  Optional<Company> findByCompanyIdAndDeletedAtIsNull(UUID companyId);

  /**
   * 특정 허브에 속한 삭제되지 않은 업체 목록 조회
   */
  List<Company> findByHubIdAndDeletedAtIsNull(UUID hubId);

  /**
   * 업체 타입별 삭제되지 않은 업체 목록 조회
   */
  List<Company> findByCompanyTypeAndDeletedAtIsNull(CompanyType companyType);

  /**
   * 복합 조건으로 업체 검색 (페이징 지원)
   */
  @Query("SELECT c FROM Company c WHERE c.deletedAt IS NULL " +
      "AND (:name IS NULL OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
      "AND (:type IS NULL OR c.companyType = :type) " +
      "AND (:hubId IS NULL OR c.hubId = :hubId)")
  Page<Company> findCompaniesWithFilters(@Param("name") String name,
      @Param("type") CompanyType type,
      @Param("hubId") UUID hubId,
      Pageable pageable);

  /**
   * 업체명 중복 체크 (같은 허브 내에서)
   */
  boolean existsByHubIdAndCompanyNameAndDeletedAtIsNull(UUID hubId, String companyName);

  /**
   * 특정 사용자가 관리하는 업체 조회
   */
  Optional<Company> findByUserIdAndDeletedAtIsNull(UUID userId);
}