package com.qb.itemservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qb.itemservice.domain.entity.Item;

import feign.Param;

public interface ItemRepository extends JpaRepository<Item, UUID> {
	Optional<Item> findByItemIdAndDeletedAtIsNull(UUID itemId);

	List<Item> findAllByItemIdInAndDeletedAtIsNull(List<UUID> itemsIds);

	// 1. (MASTER / DELIVERY / SUPPLIER) 키워드 X (전체 조회)
	@Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL")
	Page<Item> findAllNonDeleted(Pageable pageable);

	// 2. (MASTER / DELIVERY / SUPPLIER) 키워드 O (전체 조회 + 키워드)
	@Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL AND LOWER(i.itemName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	Page<Item> findAllNonDeletedByKeyword(Pageable pageable, @Param("keyword") String keyword);

	// 3. (HUB_MANAGER) 키워드 X (허브 필터링)
	@Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL AND i.hubId = :hubId")
	Page<Item> findByHubIdNonDeleted(Pageable pageable, @Param("hubId") UUID hubId);

	// 4. (HUB_MANAGER) 키워드 O (허브 필터링 + 키워드)
	@Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL AND i.hubId = :hubId AND LOWER(i.itemName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	Page<Item> findByHubIdNonDeletedByKeyword(Pageable pageable, @Param("hubId") UUID hubId, @Param("keyword") String keyword);

}
