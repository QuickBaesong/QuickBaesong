package com.qb.itemservice.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.qb.common.entity.BaseEntity;
import com.qb.itemservice.exception.ItemCustomException;
import com.qb.itemservice.exception.ItemErrorCode;

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

	public Item(UUID itemId, Long quantity) {
		this.itemId = itemId;
		this.quantity = quantity;
	}

	public Item(UUID itemId, Long price, String itemName) {
		this.itemId = itemId;
		this.price = price;
		this.itemName = itemName;
	}

	public void decreaseStock(Long amount){
		if (amount <= 0){
			throw new ItemCustomException(ItemErrorCode.INVALID_INPUT);
		}
		if(this.quantity < amount){
			throw new ItemCustomException(ItemErrorCode.OUT_OF_STOCK);
		}

		this.quantity -= amount;
	}

	public void increaseStock(Long amount){
		this.quantity += amount;
	}

	public void updateInfo(Long newPrice, String newItemName){
		boolean isPriceUnchanged = this.price.equals(newPrice);
		boolean isItemNameUnchanged = this.itemName.equals(newItemName);

		if(isPriceUnchanged && isItemNameUnchanged){
			throw new ItemCustomException(ItemErrorCode.INVALID_INPUT);
		}

		this.price = newPrice;
		this.itemName = newItemName;
	}

}
