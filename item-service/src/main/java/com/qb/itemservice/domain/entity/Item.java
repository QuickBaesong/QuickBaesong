package com.qb.itemservice.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.qb.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_item")
public class Item extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "item_id")
	private UUID itemId;

	@Column(nullable = false, name = "company_id")
	private UUID companyId;

	@Column(nullable = false, name = "hub_id")
	private UUID hubId;

	@Column(nullable = false, name = "item_name", length = 20)
	private String itemName;

	@Column(nullable = false)
	private Long price;

	@Column(nullable = false)
	private Long quantity;

	@Builder
	public Item(UUID companyId, UUID hubId, String itemName, Long price, Long quantity) {
		this.companyId = companyId;
		this.hubId = hubId;
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}

	public void decreaseStock(Long newQuantity){
		this.quantity -= newQuantity;
	}

	public void increaseStock(Long newQuantity){
		this.quantity += newQuantity;
	}

	public void updatePrice(Long newPrice){
		this.price = newPrice;
	}

}
