package com.qb.hubservice.domain.repository;

import com.qb.hubservice.domain.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID> {

    Page<Hub> findByDeletedAtIsNull(Pageable pageable);

    @Query("SELECT h FROM Hub h " +
            "WHERE h.deletedAt IS NULL " + // 논리적 삭제 조건
            "AND ( " +
            "    (:hubName IS NULL OR :hubName = '' OR LOWER(h.hubName) LIKE LOWER(CONCAT('%', :hubName, '%'))) " +
            "    OR " + // 이름 또는 주소 중 하나라도 일치
            "    (:hubAddress IS NULL OR :hubAddress = '' OR LOWER(h.location.address) LIKE LOWER(CONCAT('%', :hubAddress, '%'))) " +
            ")")
    Page<Hub> findByComplexConditions(
            @Param("hubName") String hubName,
            @Param("hubAddress") String hubAddress,
            Pageable pageable
    );

}
