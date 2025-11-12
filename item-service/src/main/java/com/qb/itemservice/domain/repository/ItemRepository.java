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

public interface ItemRepository extends JpaRepository<Item, UUID> {
	Optional<Item> findByItemIdAndDeletedAtIsNull(UUID itemId);
	List<Item> findAllByHubIdAndDeletedAtIsNull(UUID hubId);
	List<Item> findAllByCompanyIdAndDeletedAtIsNull(UUID companyId);

	List<Item> findAllByItemIdInAndDeletedAtIsNull(List<UUID> itemsIds);

	@Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL ORDER BY i.createdAt DESC")
	Page<Item> findAllAndDeletedAtIsNull(Pageable pageable);

	@Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL AND LOWER(i.itemName) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY i.createdAt DESC")
	Page<Item> findByItemNameContains(Pageable pageable, String keyword);
}
