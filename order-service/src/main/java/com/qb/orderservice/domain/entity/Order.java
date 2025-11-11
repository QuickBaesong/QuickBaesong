package com.qb.orderservice.domain.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.qb.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_order")
public class Order extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "order_id")
	private UUID orderId;

	@OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = false)
	private List<OrderItem> orderItems = new ArrayList<>();

	@Column(nullable = false)
	private UUID sender;

	@Column(nullable = false)
	private UUID receiver;

	@Column(name = "hub_id", nullable = false)
	private UUID hubId;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "delivery_id")
	private UUID deliveryId;

	@Column(name = "order_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(name = "required_delivery_at", nullable = false)
	private LocalDateTime requiredDeliveryAt;

	@Builder
	public Order(UUID sender, UUID receiver, UUID hubId, UUID userId, LocalDateTime requiredDeliveryAt) {
		this.sender = sender;
		this.receiver = receiver;
		this.hubId = hubId;
		this.userId = userId;
		this.requiredDeliveryAt = requiredDeliveryAt;
		this.orderStatus = OrderStatus.CREATED;
	}

	public void addOrderItem(OrderItem orderItem)
	{
		orderItem.setOrder(this);
		orderItems.add(orderItem);
	}

	@Override
	public void softDelete(String userName){
		super.softDelete(userName);
		orderItems.forEach(orderItem -> orderItem.softDelete(userName));
	}

	public void canceledOrder(String userName){
		this.orderStatus = orderStatus.CANCELED;
		this.softDelete(userName);
	}

	public void updateDeliveryInfo(UUID deliveryId){
		this.deliveryId = deliveryId;
	}

	public String calculateTotalPrice(){
		BigDecimal totalPrice = orderItems.stream()
			.filter(item -> !item.isDeleted())
			.map(item -> BigDecimal.valueOf(item.getPrice())
				.multiply(BigDecimal.valueOf(item.getQuantity())))
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(totalPrice);
	}

	public String calculateTotalAmount(){
		BigDecimal totalAmount = orderItems.stream()
			.filter(orderItem -> !orderItem.isDeleted())
			.map(orderItem -> BigDecimal.valueOf(orderItem.getQuantity()))
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(totalAmount);

	}

}
