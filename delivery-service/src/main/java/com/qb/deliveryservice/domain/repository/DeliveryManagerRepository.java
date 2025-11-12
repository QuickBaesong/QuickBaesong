package com.qb.deliveryservice.domain.repository;

import com.qb.deliveryservice.domain.model.DeliveryManager;
import com.qb.deliveryservice.domain.model.ManagerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID> {

    // 삭제되지 않은 배송 담당자 조회
    @Query("SELECT dm FROM DeliveryManager dm WHERE dm.id = :id AND dm.deletedAt IS NULL")
    Optional<DeliveryManager> findByIdAndNotDeleted(@Param("id") UUID id);


    // 사용자 ID로 배송 담당자 존재 여부 확인, 중복 등록 방지용
    @Query("SELECT COUNT(dm) > 0 FROM DeliveryManager dm WHERE dm.userId = :userId AND dm.deletedAt IS NULL")
    boolean existsByUserIdAndNotDeleted(@Param("userId") UUID userId);

    // 특정 허브의 업체 배송 담당자 중 마지막 순번 조회
    @Query("SELECT COALESCE(MAX(dm.sequence), -1) FROM DeliveryManager dm WHERE dm.hubId = :hubId AND dm.managerType = :managerType AND dm.deletedAt IS NULL")
    Integer findMaxSequenceByHubId(@Param("hubId") UUID hubId, @Param("managerType") ManagerType managerType);

    // 허브 배송 담당자의 마지막 순번 조회
    @Query("SELECT COALESCE(MAX(dm.sequence), -1) FROM DeliveryManager dm WHERE dm.managerType = :managerType AND dm.deletedAt IS NULL")
    Integer findMaxSequenceForHubManagers(@Param("managerType") ManagerType managerType);

    // 특정 허브의 업체 배송 담당자 수 확인
    @Query("SELECT COUNT(dm) FROM DeliveryManager dm WHERE dm.hubId = :hubId AND dm.managerType = :managerType AND dm.deletedAt IS NULL")
    Long countCompanyManagersByHubId(@Param("hubId") UUID hubId, @Param("managerType") ManagerType managerType);

    // 허브 배송 담당자 수 확인
    @Query("SELECT COUNT(dm) FROM DeliveryManager dm WHERE dm.managerType = :managerType AND dm.deletedAt IS NULL")
    Long countHubManagers(@Param("managerType") ManagerType managerType);

    // 다음 허브 배송 담당자 조회 (순환)
    @Query(value = "SELECT * FROM p_delivery_manager WHERE manager_type = 'HUB' AND deleted_at IS NULL AND sequence > :lastSequence ORDER BY sequence LIMIT 1", nativeQuery = true)
    Optional<DeliveryManager> findNextHubManager(@Param("lastSequence") Integer lastSequence);

    // 첫 번째 허브 배송 담당자 조회
    @Query("SELECT dm FROM DeliveryManager dm WHERE dm.managerType = com.qb.deliveryservice.domain.model.ManagerType.HUB AND dm.deletedAt IS NULL ORDER BY dm.sequence")
    Optional<DeliveryManager> findFirstHubManager();

    // 다음 업체 배송 담당자 조회 (순환)
    @Query(value = "SELECT * FROM p_delivery_manager WHERE hub_id = :hubId AND manager_type = 'COMPANY' AND deleted_at IS NULL AND sequence > :lastSequence ORDER BY sequence LIMIT 1", nativeQuery = true)
    Optional<DeliveryManager> findNextCompanyManager(@Param("hubId") UUID hubId, @Param("lastSequence") Integer lastSequence);

    // 첫 번째 업체 배송 담당자 조회
    @Query("SELECT dm FROM DeliveryManager dm WHERE dm.hubId = :hubId AND dm.managerType = com.qb.deliveryservice.domain.model.ManagerType.COMPANY AND dm.deletedAt IS NULL ORDER BY dm.sequence")
    Optional<DeliveryManager> findFirstCompanyManager(@Param("hubId") UUID hubId);

}
