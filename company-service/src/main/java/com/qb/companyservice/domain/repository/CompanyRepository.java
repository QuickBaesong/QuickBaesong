package com.qb.companyservice.domain.repository;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.enums.CompanyType;
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

  // ========== 기존 메서드들 ==========

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

  // ========== 새로 추가되는 메서드들 ==========

  /**
   * ID로 삭제되지 않은 업체 조회
   */
  @Query("SELECT c FROM Company c WHERE c.companyId = :companyId AND c.deletedAt IS NULL")
  Optional<Company> findByIdAndNotDeleted(@Param("companyId") UUID companyId);

  /**
   * 업체 타입별 조회 (페이징)
   */
  @Query("SELECT c FROM Company c WHERE c.deletedAt IS NULL AND c.companyType = :companyType")
  Page<Company> findByCompanyTypeAndNotDeleted(@Param("companyType") CompanyType companyType,
      Pageable pageable);

  /**
   * 복합 검색: 업체명 + 타입
   */
  @Query("SELECT c FROM Company c WHERE c.deletedAt IS NULL " +
      "AND (:companyName IS NULL OR c.companyName LIKE CONCAT('%', :companyName, '%')) " +
      "AND (:companyType IS NULL OR c.companyType = :companyType)")
  Page<Company> findByCompanyNameAndTypeAndNotDeleted(
      @Param("companyName") String companyName,
      @Param("companyType") CompanyType companyType,
      Pageable pageable);

  /**
   * 복합 검색: 업체명 + 허브
   */
  @Query("SELECT c FROM Company c WHERE c.deletedAt IS NULL " +
      "AND (:companyName IS NULL OR c.companyName LIKE CONCAT('%', :companyName, '%')) " +
      "AND (:hubId IS NULL OR c.hubId = :hubId)")
  Page<Company> findByCompanyNameAndHubIdAndNotDeleted(
      @Param("companyName") String companyName,
      @Param("hubId") UUID hubId,
      Pageable pageable);

  /**
   * 복합 검색: 타입 + 허브
   */
  @Query("SELECT c FROM Company c WHERE c.deletedAt IS NULL " +
      "AND (:companyType IS NULL OR c.companyType = :companyType) " +
      "AND (:hubId IS NULL OR c.hubId = :hubId)")
  Page<Company> findByCompanyTypeAndHubIdAndNotDeleted(
      @Param("companyType") CompanyType companyType,
      @Param("hubId") UUID hubId,
      Pageable pageable);

  /**
   * 전체 복합 검색: 업체명 + 타입 + 허브
   */
  @Query("SELECT c FROM Company c WHERE c.deletedAt IS NULL " +
      "AND (:companyName IS NULL OR c.companyName LIKE CONCAT('%', :companyName, '%')) " +
      "AND (:companyType IS NULL OR c.companyType = :companyType) " +
      "AND (:hubId IS NULL OR c.hubId = :hubId)")
  Page<Company> findByAllConditionsAndNotDeleted(
      @Param("companyName") String companyName,
      @Param("companyType") CompanyType companyType,
      @Param("hubId") UUID hubId,
      Pageable pageable);

  /**
   * 허브별 업체 수 조회
   */
  @Query("SELECT COUNT(c) FROM Company c WHERE c.deletedAt IS NULL AND c.hubId = :hubId")
  long countByHubIdAndNotDeleted(@Param("hubId") UUID hubId);

  /**
   * 타입별 업체 수 조회
   */
  @Query("SELECT COUNT(c) FROM Company c WHERE c.deletedAt IS NULL AND c.companyType = :companyType")
  long countByCompanyTypeAndNotDeleted(@Param("companyType") CompanyType companyType);
}