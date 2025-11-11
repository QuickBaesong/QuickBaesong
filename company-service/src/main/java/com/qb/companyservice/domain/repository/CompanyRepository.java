package com.qb.companyservice.domain.repository;

import com.qb.companyservice.domain.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

  // 업체명 중복 검증 (CompanyService에서 사용)
  @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Company c " +
      "WHERE c.companyName = :companyName " +
      "AND c.deletedAt IS NULL " +
      "AND (:excludeCompanyId IS NULL OR c.companyId != :excludeCompanyId)")
  boolean existsByCompanyNameAndDeletedAtIsNull(
      @Param("companyName") String companyName,
      @Param("excludeCompanyId") UUID excludeCompanyId);

  // 업체명으로 검색 (페이징)
  @Query("SELECT c FROM Company c " +
      "WHERE c.deletedAt IS NULL " +
      "AND (:companyName IS NULL OR c.companyName LIKE CONCAT('%', :companyName, '%'))")
  Page<Company> findByCompanyNameContainingAndDeletedAtIsNull(
      @Param("companyName") String companyName,
      Pageable pageable);

  // 삭제되지 않은 업체만 조회
  @Query("SELECT c FROM Company c WHERE c.deletedAt IS NULL")
  Page<Company> findAllActiveCompanies(Pageable pageable);

  // 특정 허브의 업체 조회
  @Query("SELECT c FROM Company c " +
      "WHERE c.deletedAt IS NULL " +
      "AND c.hubId = :hubId")
  Page<Company> findByHubIdAndDeletedAtIsNull(
      @Param("hubId") UUID hubId,
      Pageable pageable);
}