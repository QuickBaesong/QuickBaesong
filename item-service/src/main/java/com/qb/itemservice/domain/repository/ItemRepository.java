package com.qb.itemservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qb.itemservice.domain.entity.Item;

public interface ItemRepository extends JpaRepository<Item, UUID> {
	Optional<Item> findByItemIdAndDeletedAtIsNull(UUID itemId);
	List<Item> findAllByHubIdAndDeletedAtIsNull(UUID hubId);
	List<Item> findAllByCompanyIdAndDeletedAtIsNull(UUID companyId);

	List<Item> findAllByItemIdInAndDeletedAtIsNull(List<UUID> itemsIds);
}
