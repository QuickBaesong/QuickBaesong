package com.qb.orderservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qb.orderservice.domain.entity.Order;

import feign.Param;

public interface OrderRepository extends JpaRepository<Order, UUID> {

	Optional<Order> findByOrderIdAndDeletedAtIsNull(UUID id);

	@Query("SELECT o FROM Order o WHERE o.deletedAt IS NULL ORDER BY o.createdAt DESC")
	Page<Order> findAllByDeletedAtIsNull(Pageable pageable);

	// 2. 허브 관리자용: 특정 허브 소속 주문만 조회 (Hub Filter) + 생성일 DESC
	@Query("SELECT o FROM Order o WHERE o.deletedAt IS NULL AND o.hubId = :hubId ORDER BY o.createdAt DESC")
	Page<Order> findByHubIdAndDeletedAtIsNull(@Param("hubId") UUID hubId, Pageable pageable);

	// 3. 일반 사용자/업체 담당자용: 특정 사용자(주문자)의 주문만 조회 (User Filter) + 생성일 DESC
	@Query("SELECT o FROM Order o WHERE o.deletedAt IS NULL AND o.userId = :userId ORDER BY o.createdAt DESC")
	Page<Order> findByUserIdAndDeletedAtIsNull(@Param("userId") UUID userId, Pageable pageable);

}
