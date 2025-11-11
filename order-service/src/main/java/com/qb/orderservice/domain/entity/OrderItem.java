package com.qb.orderservice.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.qb.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "order_item_id")
	private UUID orderItemId;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "item_id",nullable = false)
	private UUID itemId;

	@Column(nullable = false)
	private Long quantity;

	@Column(nullable = false)
	private Long price;

	@Builder
	public OrderItem(UUID itemId, Long quantity, Long price) {
		this.itemId = itemId;
		this.quantity = quantity;
		this.price = price;
	}

	public void setOrder(Order order){
		this.order=order;
	}

	public void updateOrderItemPrice(Long price){ this.price=price; }

}
