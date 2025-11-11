package com.qb.hubservice.domain.repository;

import com.qb.hubservice.domain.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID> {

    // 1. 삭제되지 않은 단건 정보 조회 (getHub에 사용)

    Optional<Hub> findByIdAndIsDeletedFalse(UUID id);


    Page<Hub> findAllByIsDeletedFalse(Pageable pageable);

    @Query("SELECT h FROM Hub h " +
            "WHERE h.isDeleted = FALSE " +
            "AND (:#{#name} IS NULL OR :#{#name} = '' OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')))")

    Page<Hub> searchHubs(
            @Param("name") String name,
            Pageable pageable);

}
