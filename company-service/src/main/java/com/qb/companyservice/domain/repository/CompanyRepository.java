package com.qb.companyservice.domain.repository;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.enums.CompanyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

  // 업체 ID로 조회 (Soft Delete 고려)
  @Query("SELECT c FROM Company c WHERE c.companyId = :companyId AND c.deletedAt IS NULL")
  Optional<Company> findByCompanyIdAndDeletedAtIsNull(@Param("companyId") UUID companyId);

  // 업체명과 업체 타입으로 검색 (페이징)
  @Query("SELECT c FROM Company c " +
      "WHERE c.deletedAt IS NULL " +
      "AND (:companyName IS NULL OR c.companyName LIKE %:companyName%) " +
      "AND (:companyType IS NULL OR c.companyType = :companyType) " +
      "AND (:hubId IS NULL OR c.hubId = :hubId)")
  Page<Company> findCompaniesWithFilters(
      @Param("companyName") String companyName,
      @Param("companyType") CompanyType companyType,
      @Param("hubId") UUID hubId,
      Pageable pageable);

  // 특정 허브에 속한 업체 수 조회
  @Query("SELECT COUNT(c) FROM Company c WHERE c.hubId = :hubId AND c.deletedAt IS NULL")
  long countByHubIdAndDeletedAtIsNull(@Param("hubId") UUID hubId);

  // 업체명 중복 체크
  @Query("SELECT COUNT(c) > 0 FROM Company c " +
      "WHERE c.companyName = :companyName " +
      "AND c.deletedAt IS NULL " +
      "AND (:excludeCompanyId IS NULL OR c.companyId != :excludeCompanyId)")
  boolean existsByCompanyNameAndDeletedAtIsNull(
      @Param("companyName") String companyName,
      @Param("excludeCompanyId") UUID excludeCompanyId);

  // 사용자 ID로 업체 조회 (한 사용자가 여러 업체를 가질 수 있다고 가정)
  @Query("SELECT c FROM Company c WHERE c.userId = :userId AND c.deletedAt IS NULL")
  Page<Company> findByUserIdAndDeletedAtIsNull(@Param("userId") UUID userId, Pageable pageable);

  // 업체 타입별 조회
  @Query("SELECT c FROM Company c WHERE c.companyType = :companyType AND c.deletedAt IS NULL")
  Page<Company> findByCompanyTypeAndDeletedAtIsNull(@Param("companyType") CompanyType companyType, Pageable pageable);
}